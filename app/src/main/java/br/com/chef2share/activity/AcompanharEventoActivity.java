package br.com.chef2share.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.AcompanhaEvento;
import br.com.chef2share.domain.Convite;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Ingresso;
import br.com.chef2share.domain.QRCodeResult;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_acompanhar_evento)
public class AcompanharEventoActivity extends SuperActivity {

    @ViewById
    public TextView txtTitulo;
    @ViewById
    public TextView txtTituloEvento;
    @ViewById
    public TextView txtDataInicio;
    @ViewById
    public TextView txtDataTermino;
    @ViewById
    public TextView txtValor;
    @ViewById
    public TextView txtVisualizados;
    @ViewById
    public TextView txtReservados;
    @ViewById
    public TextView txtPagos;
    @ViewById
    public ProgressBar progress;
    @ViewById
    public LinearLayout layoutAguarde;
    @ViewById
    public LinearLayout layoutParticipantes;
    @ViewById
    public Button btnChecarConvites;

    @ViewById
    public ProgressBar progressImgChef;
    @ViewById
    public ImageView imgBackground;
    @ViewById
    public TextView txtNomeChef;

    @Extra("eventoId")
    public String eventoId;

    private AcompanhaEvento acompanhaEvento;

    private static final int CONFIRMA_CHECKIN_RESULT = 123;
    private static final int QRCODE_RESULT = 234;

    @Override
    public void init() {
        super.init();

        layoutAguarde.setVisibility(View.VISIBLE);

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getResources().getString(R.string.titulo_acompanhe));

        doInBackground(getTransacaoDetalhesEvento(eventoId), false, R.string.msg_aguarde_carregando_detalhes_evento, false, layoutAguarde, null);
    }

    private Transacao getTransacaoDetalhesEvento(final String eventoId) {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.acompanhaEvento(getBaseContext(), superActivity, superActivity, eventoId);
            }

            @Override
            public void onSuccess(Response response) {
                populaTela(response.getAcompanhe());
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro, new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };
    }

    private void populaTela(AcompanhaEvento acompanhaEvento) {
        this.acompanhaEvento = acompanhaEvento;

        Evento evento = acompanhaEvento.getEvento();

        SuperUtil.setUnderlineTextView(txtTituloEvento);

        setTextString(txtTituloEvento, evento.getPasso2().getTitulo());
        setTextString(txtDataInicio, evento.getPasso3().getDataInicioFormatadaPasso3());
        setTextString(txtDataTermino, evento.getPasso3().getDataTerminoFormatadaPasso3());
        setTextString(txtValor, evento.getPasso3().getValorFormatado(true));
        setTextString(txtReservados, acompanhaEvento.getReservados());
        setTextString(txtPagos, acompanhaEvento.getPagos());
        setTextString(txtVisualizados, acompanhaEvento.getVisualizados());

        btnChecarConvites.setVisibility(acompanhaEvento.isCheckinEnabled() ? View.VISIBLE : View.GONE);

        if (evento.getPasso2().getImagensPasso2() != null && evento.getPasso2().getImagensPasso2().size() > 0) {
            ImagemPasso imagemPasso = evento.getPasso2().getImagensPasso2().get(0);
            if (imagemPasso != null) {
                String url = SuperCloudinery.getUrl(getBaseContext(), imagemPasso.getImagem(), SuperUtils.getWidthImagemFundo(getBaseContext()), SuperUtils.getHeightImagemFundo(getBaseContext()));
                if (StringUtils.isNotEmpty(url)) {
                    Picasso.with(getBaseContext()).load(url).resize(SuperUtils.getWidthImagemFundo(getBaseContext()), SuperUtils.getHeightImagemFundo(getBaseContext())).centerCrop().into(imgBackground, new Callback() {
                        @Override
                        public void onSuccess() {
                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progress.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }

        List<Usuario> participantes = acompanhaEvento.getParticipantes();
        if (participantes != null && participantes.size() > 0) {
            addParticipantes(participantes);
        }
    }

    private void addParticipantes(List<Usuario> participantes) {
        layoutParticipantes.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

        for (final Usuario participante : participantes) {
            LinearLayout layoutParticipante = (LinearLayout) inflater.inflate(R.layout.item_convidado_acompanha_evento, null);
            TextView txtNomeConvidado = (TextView) layoutParticipante.findViewById(R.id.txtNomeConvidado);
            TextView txtEmail = (TextView) layoutParticipante.findViewById(R.id.txtEmail);

            SuperUtil.setUnderlineTextView(txtEmail);

            txtEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{participante.getEmail()});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Chef2Share - " + acompanhaEvento.getEvento().getPasso2().getTitulo());
                    startActivity(Intent.createChooser(i, "Enviar email..."));
                }
            });

            RoundedImageView imgConvidado = (RoundedImageView) layoutParticipante.findViewById(R.id.imgConvidado);
            final ProgressBar progressImgConvidado = (ProgressBar) layoutParticipante.findViewById(R.id.progressImgConvidado);

            String url = SuperCloudinery.getUrlImgPessoa(getBaseContext(), participante.getImagem());
            if (StringUtils.isNotEmpty(url)) {
                Picasso.with(getBaseContext()).load(url).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgConvidado, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressImgConvidado.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressImgConvidado.setVisibility(View.GONE);
                    }
                });

            } else {
                imgConvidado.setImageResource(R.drawable.userpic);
                progressImgConvidado.setVisibility(View.GONE);
            }

            setTextString(txtNomeConvidado, participante.getNome());
            setTextString(txtEmail, participante.getEmail());

            LinearLayout layoutConvites = (LinearLayout) layoutParticipante.findViewById(R.id.layoutConvites);

            List<Ingresso> ingressos = participante.getIngressos();
            if (ingressos != null && ingressos.size() > 0) {
                for (Ingresso i : ingressos) {

                    for (Convite entrada : i.getEntradas()) {

                        LinearLayout layoutIngresso = (LinearLayout) inflater.inflate(R.layout.item_ingresso_acompanha_evento, null);

                        TextView txtTipoIngresso = (TextView) layoutIngresso.findViewById(R.id.txtTipoIngresso);
                        TextView txtStatus = (TextView) layoutIngresso.findViewById(R.id.txtStatus);
                        TextView txtNumeroIngresso = (TextView) layoutIngresso.findViewById(R.id.txtNumeroIngresso);
                        final ImageView imgCheckVerde = (ImageView) layoutIngresso.findViewById(R.id.imgCheckVerde);
                        final ImageView imgCheckCinza = (ImageView) layoutIngresso.findViewById(R.id.imgCheckCinza);

                        imgCheckVerde.setOnClickListener(onClickDesfazerCheckin(imgCheckCinza, imgCheckVerde, i, entrada, participante));
                        imgCheckCinza.setOnClickListener(onClickCheckin(imgCheckCinza, imgCheckVerde, i, entrada, participante));

                        setTextString(txtTipoIngresso, entrada.getNome());
                        setTextString(txtStatus, acompanhaEvento.getDicionario().get(i.getStatus()));
                        setTextString(txtNumeroIngresso, entrada.getReferencia());

                        layoutConvites.removeView(layoutIngresso);
                        layoutConvites.addView(layoutIngresso);

                        layoutParticipante.removeView(layoutConvites);
                        layoutParticipante.addView(layoutConvites);

                        if(!acompanhaEvento.isCheckinEnabled()) {
                            imgCheckCinza.setVisibility(View.GONE);
                            imgCheckVerde.setVisibility(View.GONE);

                        }else if(!SuperUtils.isPermitidoChekin(i.getStatus())){
                            imgCheckCinza.setVisibility(View.GONE);
                            imgCheckVerde.setVisibility(View.GONE);
                        }else{
                            imgCheckCinza.setVisibility(View.GONE);
                            imgCheckVerde.setVisibility(View.GONE);

                            if(entrada.getCheckin() != null){
                                imgCheckVerde.setVisibility(View.VISIBLE);
                            }else{
                                imgCheckCinza.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
            layoutParticipantes.addView(layoutParticipante);
        }
    }

    private View.OnClickListener onClickCheckin(final ImageView imgCheckCinza, final ImageView imgCheckVerde, final Ingresso i, final Convite entrada, final Usuario participante) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoCheckinCheckoutEnabled(imgCheckCinza, imgCheckVerde, false);
                doInBackground(getTransacaoCheckin(imgCheckCinza, imgCheckVerde, true, i.getId(), entrada, participante.getId()), true, R.string.msg_aguarde_realizando_checkin, false);
            }
        };
    }

    private void botaoCheckinCheckoutEnabled(ImageView imgCheckCinza, ImageView imgCheckVerde, boolean b) {
        imgCheckCinza.setEnabled(b);
        imgCheckVerde.setEnabled(b);
    }

    private View.OnClickListener onClickDesfazerCheckin(final ImageView imgCheckCinza, final ImageView imgCheckVerde, final Ingresso i, final Convite entrada, final Usuario participante) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoCheckinCheckoutEnabled(imgCheckCinza, imgCheckVerde, false);
                doInBackground(getTransacaoCheckin(imgCheckCinza, imgCheckVerde, false, i.getId(), entrada, participante.getId()), true, R.string.msg_aguarde_cancelando_checkin, false);
            }
        };
    }

    private Transacao getTransacaoCheckin(final ImageView imgCheckCinza, final ImageView imgCheckVerde, final boolean checkin, final String ingressoId, final Convite entrada, final String usuarioId) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
                if(checkin){
                    imgCheckCinza.setVisibility(View.VISIBLE);
                    imgCheckVerde.setVisibility(View.GONE);
                }else{
                    imgCheckCinza.setVisibility(View.GONE);
                    imgCheckVerde.setVisibility(View.VISIBLE);
                }
                botaoCheckinCheckoutEnabled(imgCheckCinza, imgCheckVerde, true);
            }

            @Override
            public void onSuccess(Response response) {

                entrada.setCheckin(response.getCheckin());
                if(checkin){
                    imgCheckCinza.setVisibility(View.GONE);
                    imgCheckVerde.setVisibility(View.VISIBLE);
                }else{
                    imgCheckCinza.setVisibility(View.VISIBLE);
                    imgCheckVerde.setVisibility(View.GONE);
                }

                botaoCheckinCheckoutEnabled(imgCheckCinza, imgCheckVerde, true);
                SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msgs_checkin_realizado_sucesso));
            }

            @Override
            public void execute() throws Exception {
                if (checkin) {
                    superService.checkinConvite(getBaseContext(), superActivity, superActivity, usuarioId, ingressoId, entrada.getNumero());
                } else {
                    superService.checkoutConvite(getBaseContext(), superActivity, superActivity, usuarioId, ingressoId, entrada.getNumero());
                }
            }
        };
    }


    @Click({R.id.txtTituloEvento, R.id.imgBackground})
    public void onClickDetalhesEvento(View v) {
        Bundle params = new Bundle();
        params.putString("eventoId", acompanhaEvento.getEvento().getId());
        SuperUtil.show(v.getContext(), DetalheEventoActivity_.class, params);
    }

    @Click(R.id.btnChecarConvites)
    public void onClickChecarConvite(View v) {
        try {
            //new IntentIntegrator(QrReaderActity.this).initiateScan();
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, QRCODE_RESULT);
        } catch (ActivityNotFoundException exception) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android&hl=pt_BR"));
            startActivity(i);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCODE_RESULT && resultCode == RESULT_OK) {
            String scanResult = data.getStringExtra("SCAN_RESULT");
            String scanResultFormat = data.getStringExtra("SCAN_RESULT_FORMAT");

            SuperUtil.log("SCAN_RESULT: " + scanResult);
            SuperUtil.log("SCAN_RESULT_FORMAT: " + scanResultFormat);

            QRCodeResult qRCodeResult = getEntradaByQRCode(scanResult);

            if(qRCodeResult == null){
                SuperUtil.alertDialog(superActivity, getResources().getString(R.string.msg_entrada_nao_localizada, scanResult));
                return;
            }

            Bundle param = new Bundle();
            param.putSerializable("qrCodeResult", qRCodeResult);
            param.putSerializable("acompanhaEvento", acompanhaEvento);

            Intent i = new Intent(this, AlertEntradaQRCode_.class);
            i.putExtras(param);
            startActivityForResult(i, CONFIRMA_CHECKIN_RESULT);

        }else if (requestCode == CONFIRMA_CHECKIN_RESULT && resultCode == RESULT_OK){

            QRCodeResult qrCodeResult = (QRCodeResult) data.getSerializableExtra("qrCodeResult");

            if(qrCodeResult.entrada.getCheckin() == null) {
                doInBackground(getTransacaoCheckinQRCode(qrCodeResult.ingresso.getId(), qrCodeResult.entrada, qrCodeResult.participante.getId()), false, R.string.msg_aguarde_carregando_detalhes_evento, false, layoutAguarde, null);
            }
        }
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
    }

    public QRCodeResult getEntradaByQRCode(String valorQRCode) {

        List<Usuario> participantes = acompanhaEvento.getParticipantes();

        for (final Usuario participante : participantes) {

            List<Ingresso> ingressos = participante.getIngressos();
            if (ingressos != null && ingressos.size() > 0) {
                for (Ingresso i : ingressos) {
                    for (Convite entrada : i.getEntradas()) {
                        if(StringUtils.equals(entrada.getReferencia(), valorQRCode)){
                            QRCodeResult qrCodeResult = new QRCodeResult();
                            qrCodeResult.entrada = entrada;
                            qrCodeResult.ingresso = i;
                            qrCodeResult.participante = participante;

                           return qrCodeResult;
                        }
                    }
                }
            }
        }

        return null;
    }

    private Transacao getTransacaoCheckinQRCode(final String ingressoId, final Convite entrada, final String usuarioId) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msgs_checkin_realizado_sucesso));
                doInBackground(getTransacaoDetalhesEvento(eventoId), false, R.string.msg_aguarde_carregando_detalhes_evento, false, layoutAguarde, null);
            }

            @Override
            public void execute() throws Exception {
                superService.checkinConvite(getBaseContext(), superActivity, superActivity, usuarioId, ingressoId, entrada.getNumero());
            }
        };
    }
}
