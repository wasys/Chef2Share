package br.com.chef2share.adapter;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.ColorUtils;
import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.AcompanharEventoActivity;
import br.com.chef2share.activity.AcompanharEventoActivity_;
import br.com.chef2share.activity.BuscaEventosActivity;
import br.com.chef2share.activity.CriarEventoActivity_;
import br.com.chef2share.activity.DetalheEventoActivity;
import br.com.chef2share.activity.DetalheEventoActivity_;
import br.com.chef2share.activity.EventosQueOferecoActivity;
import br.com.chef2share.domain.BuscaEventoQueOfereco;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.ChefEvento;
import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroEventosQueOfereco;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroOptions;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGeocoder;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.view.LinearLayoutAnimator;
import br.com.chef2share.view.RoundedImageView;

/**
 * Created by Jonas on 21/11/2015.
 */
public class EventoQueOferecoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FILTROS = 0;
    private static final int EVENTO = 1;

    private EventosQueOferecoActivity context;
    private BuscaEventoQueOfereco buscaVo;
    private OnSelectedBuscaFiltroEventosQueOfereco onSelectedBuscaFiltroOptionslistener;
    private ChefEvento chefEvento;

    public EventoQueOferecoRecyclerViewAdapter(EventosQueOferecoActivity context, OnSelectedBuscaFiltroEventosQueOfereco onSelectedBuscaFiltroOptionslistener) {
        this.context = context;
        this.onSelectedBuscaFiltroOptionslistener = onSelectedBuscaFiltroOptionslistener;
    }

    public void refresh(ChefEvento chefEvento, BuscaEventoQueOfereco buscaVo){
        this.chefEvento = chefEvento;
        this.buscaVo = buscaVo;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            //só o primeiro será BANNER
            case 0: return FILTROS;

            //o resto é EVENTO
            default: return EVENTO;
        }
    }

    public class ViewHolderPesquisa extends RecyclerView.ViewHolder {

        public TextView txtDataInicio;
        public TextView txtDataFinal;
        public LinearLayout layoutDataInicio;
        public LinearLayout layoutDataFinal;
        public Button btnFiltrar;
        private DatePickerDialog datePickerDataInicial;
        private DatePickerDialog datePickerDataFinal;

        public ViewHolderPesquisa(View view) {
            super(view);

            txtDataInicio = (TextView) view.findViewById(R.id.txtDataInicio);
            txtDataFinal = (TextView) view.findViewById(R.id.txtDataFinal);
            layoutDataInicio = (LinearLayout) view.findViewById(R.id.layoutDataInicio);
            layoutDataFinal = (LinearLayout) view.findViewById(R.id.layoutDataFinal);
            btnFiltrar = (Button) view.findViewById(R.id.btnFiltrar);

            btnFiltrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectedBuscaFiltroOptionslistener.onClickPesquisarFiltro();
                }
            });

            layoutDataInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDataInicial.show();
                }
            });

            layoutDataFinal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDataFinal.show();
                }
            });
        }

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolderEvento extends RecyclerView.ViewHolder {

        private Evento evento;

        public ImageView imgBackground;
        public TextView txtTituloEvento;
        public TextView txtDataInicio;
        public TextView txtDataTermino;
        public TextView txtConvidados;
        public TextView txtStatusEvento;
        public Button btnEditar;
        public ProgressBar progressImgBackgroundEvento;

        public ViewHolderEvento(View v) {
            super(v);

            imgBackground = (ImageView) v.findViewById(R.id.imgBackground);
            txtTituloEvento = (TextView) v.findViewById(R.id.txtTituloEvento);
            txtDataInicio = (TextView) v.findViewById(R.id.txtDataInicio);
            txtDataTermino = (TextView) v.findViewById(R.id.txtDataTermino);
            txtConvidados = (TextView) v.findViewById(R.id.txtConvidados);
            txtStatusEvento = (TextView) v.findViewById(R.id.txtStatusEvento);
            btnEditar = (Button) v.findViewById(R.id.btnEditar);
            progressImgBackgroundEvento = (ProgressBar) v.findViewById(R.id.progressImgBackgroundEvento);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle params = new Bundle();
                    params.putString("eventoId", getEvento().getId());
                    SuperUtil.show(v.getContext(), AcompanharEventoActivity_.class, params);
                }
            });

            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SuperApplication.getSuperCache().setEvento(null);
                    SuperApplication.getSuperCache().setCadastro(null);

                    Bundle params = new Bundle();
                    params.putString("eventoId", getEvento().getId());
                    SuperUtil.show(v.getContext(), CriarEventoActivity_.class, params);
                }
            });
        }

        public void setEvento(final Evento evento) {
            this.evento = evento;
        }

        public Evento getEvento() {
            return evento;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FILTROS: {
                View row = inflater.inflate(R.layout.item_filtro_busca_eventos_que_ofereco, parent, false);
                return new ViewHolderPesquisa(row);
            }

            case EVENTO: {
                View row = inflater.inflate(R.layout.item_evento_que_ofereco, parent, false);
                return new ViewHolderEvento(row);
            }
        }

        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolderPesquisa) {
            final ViewHolderPesquisa viewHolderPesquisa = (ViewHolderPesquisa) holder;

            long dataMin = DateUtils.toDate(buscaVo.getPeriodoDe(), "yyyy-MM-dd").getTime();
            long dataMax = DateUtils.toDate(buscaVo.getPeriodoAte(), "yyyy-MM-dd").getTime();

            setDateTimeField(viewHolderPesquisa, dataMin, dataMax);

        }else {

            final ViewHolderEvento viewHolderEvento = (ViewHolderEvento) holder;

            Evento evento = buscaVo.getEventos().get(position - 1);
            viewHolderEvento.setEvento(evento);

            viewHolderEvento.imgBackground.getLayoutParams().height = SuperUtils.getHeightImagemFundo(context);

            Passo2 passo2 = evento.getPasso2();
            if (passo2 != null && StringUtils.isNotEmpty(passo2.getTitulo())) {
                viewHolderEvento.txtTituloEvento.setText(passo2.getTitulo());
//                viewHolderEvento.txtTituloEvento.setPaintFlags(viewHolderEvento.txtTituloEvento.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                viewHolderEvento.progressImgBackgroundEvento.setVisibility(View.GONE);

                if (passo2.getImagensPasso2() != null) {
                    ImagemPasso imagemPasso = passo2.getImagensPasso2().get(0);
                    if (imagemPasso != null) {
                        viewHolderEvento.progressImgBackgroundEvento.setVisibility(View.VISIBLE);
                        String url = SuperCloudinery.getUrl(context, imagemPasso.getImagem(), SuperUtils.getWidthImagemFundo(context), SuperUtils.getHeightImagemFundo(context));
                        if (StringUtils.isNotEmpty(url)) {
                            Picasso.with(context).load(url).resize(SuperUtils.getWidthImagemFundo(context), SuperUtils.getHeightImagemFundo(context)).centerCrop().into(viewHolderEvento.imgBackground, new Callback() {
                                @Override
                                public void onSuccess() {
                                    viewHolderEvento.progressImgBackgroundEvento.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    viewHolderEvento.progressImgBackgroundEvento.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }
            }else{
                viewHolderEvento.txtTituloEvento.setText(R.string.nao_informado);
                viewHolderEvento.imgBackground.setVisibility(View.GONE);
                viewHolderEvento.progressImgBackgroundEvento.setVisibility(View.GONE);
            }

            Passo3 passo3 = evento.getPasso3();
            if(passo3 != null && StringUtils.isNotEmpty(passo3.getDataInicio())){
                viewHolderEvento.txtDataInicio.setText(passo3.getDataInicioFormatadaPasso3());
                viewHolderEvento.txtDataTermino.setText(passo3.getDataTerminoFormatadaPasso3());
                viewHolderEvento.txtConvidados.setText(evento.getComprados() + "/" + passo3.getMaximo());
            }else{
                viewHolderEvento.txtDataInicio.setText(R.string.nao_informado);
                viewHolderEvento.txtDataTermino.setText(R.string.nao_informado);
                viewHolderEvento.txtConvidados.setText(R.string.nao_informado);
            }

            viewHolderEvento.txtStatusEvento.setText(chefEvento.getDicionario().get(evento.getStatus()));

            if(StringUtils.equals(evento.getStatus(), "RASCUNHO")){
                viewHolderEvento.txtStatusEvento.setVisibility(View.GONE);
                viewHolderEvento.txtStatusEvento.setTextColor(ColorUtils.getColor(context, R.color.chef_laranja));
            }else{
                viewHolderEvento.txtStatusEvento.setVisibility(View.VISIBLE);
                viewHolderEvento.txtStatusEvento.setTextColor(ColorUtils.getColor(context, R.color.chef_verde_limao));
            }

            if(evento.getComprados() == 0 && !StringUtils.equals(evento.getStatus(), "REALIZADO")){
                viewHolderEvento.btnEditar.setVisibility(View.VISIBLE);
            }else{
                viewHolderEvento.btnEditar.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        List<Evento> eventos = buscaVo.getEventos();
        if(eventos != null){
            return eventos.size() + 1;
        }

        return 1;
    }

    private void setDateTimeField(final ViewHolderPesquisa viewHolderPesquisa, long dataMin, long dataMax) {

        Date dataMinima = new Date(dataMin);
        if(DateUtils.isIgual(dataMinima, new Date())){
            viewHolderPesquisa.txtDataInicio.setText("Hoje");
        }else{
            viewHolderPesquisa.txtDataInicio.setText(DateUtils.toString(dataMinima, DateUtils.DATE));
        }
        Date dataMaxima = new Date(dataMax);
        viewHolderPesquisa.txtDataFinal.setText(DateUtils.toString(dataMaxima, DateUtils.DATE));

        Calendar newCalendar = Calendar.getInstance();

        viewHolderPesquisa.datePickerDataInicial = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Date date = newDate.getTime();

                if(DateUtils.isIgual(date, new Date())){
                    viewHolderPesquisa.txtDataInicio.setText("Hoje");
                }else{
                    viewHolderPesquisa.txtDataInicio.setText(DateUtils.toString(date, DateUtils.DATE));
                }

                /**
                 * Manda la pra activity
                 */
                onSelectedBuscaFiltroOptionslistener.onSelectedMin(DateUtils.toString(date, "yyyy-MM-dd"));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        viewHolderPesquisa.datePickerDataFinal = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                Date date = newDate.getTime();

                if(DateUtils.isIgual(date, new Date())){
                    viewHolderPesquisa.txtDataFinal.setText("Hoje");
                }else{
                    viewHolderPesquisa.txtDataFinal.setText(DateUtils.toString(date, DateUtils.DATE));
                }

                /**
                 * Manda la pra activity
                 */
                onSelectedBuscaFiltroOptionslistener.onSelectedMax(DateUtils.toString(date, "yyyy-MM-dd"));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        viewHolderPesquisa.datePickerDataInicial.getDatePicker().setMinDate(dataMin);
        viewHolderPesquisa.datePickerDataInicial.getDatePicker().setMaxDate(dataMax);

        viewHolderPesquisa.datePickerDataFinal.getDatePicker().setMinDate(dataMin);
        viewHolderPesquisa.datePickerDataFinal.getDatePicker().setMaxDate(dataMax);

        /**
         * Valores selecionados anteriormente
         */
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().max)) {
            viewHolderPesquisa.txtDataFinal.setText(DateUtils.toString(DateUtils.toDate(SuperApplication.getSuperCache().getPesquisaEvento().max, "yyyy-MM-dd"), DateUtils.DATE));
        }
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().min)) {
            viewHolderPesquisa.txtDataInicio.setText(DateUtils.toString(DateUtils.toDate(SuperApplication.getSuperCache().getPesquisaEvento().min, "yyyy-MM-dd"), DateUtils.DATE));
        }
    }

}