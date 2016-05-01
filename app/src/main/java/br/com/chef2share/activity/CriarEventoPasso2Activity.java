package br.com.chef2share.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.ViewPagerAdapter;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.UploadImagem;
import br.com.chef2share.domain.listener.OnClickFotoCardapio;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.fragment.FragmentViewPagerCriaEventoFotosCardapio;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.CirclePageIndicator;

@EActivity(R.layout.activity_criar_evento_passo_dois)
public class CriarEventoPasso2Activity extends SuperActivityMainMenu implements OnClickFotoCardapio {

    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;

    @ViewById public ViewPager viewPagerFotosCardapio;
    @ViewById public CirclePageIndicator pageIndicatorFotosCardapio;
    @ViewById public Button btnProximo;
    @ViewById public ScrollView scrollView;

    @ViewById public LinearLayout layoutCampos;
    private ViewPagerAdapter adapter;

    private static File newfile;
    private final int TAKE_PHOTO_CODE = 999;
    private final int SELECT_PHOTO_CODE = 777;
    public static final int INPUT_DADO = 888;
    private String diretorio;
    private Passo2 passo2;

    @Extra("idxImg")
    public int idxViewPager;

    @Extra("refresh")
    public boolean refresh;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtSubTitulo.setVisibility(View.VISIBLE);

        setTextString(txtTitulo, getString(R.string.titulo_criar_evento));
        setTextString(txtSubTitulo, getString(R.string.criar_evento_passo_dois_desc));

        viewPagerFotosCardapio.getLayoutParams().height = (int)SuperUtils.getHeightImagemFundo(getBaseContext()) + (int)AndroidUtils.toPixels(getBaseContext(), 44);

        diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/" + new Date().getTime() + "/";

        passo2 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso2();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerFotosCardapio.setAdapter(adapter);

        criaCampos(passo2);
        criaViewPagerFotos(passo2);

        viewPagerFotosCardapio.setCurrentItem(idxViewPager, true);
        if(refresh) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, btnProximo.getBottom());
                }
            });
        }
    }

    private void criaViewPagerFotos(Passo2 passo2) {
        List<Fragment> fragmentsFotosCardapio = getFragmentsFotoCardapio(passo2.getImagensPasso2());
        if (fragmentsFotosCardapio != null && fragmentsFotosCardapio.size() > 0) {
            adapter.refresh(fragmentsFotosCardapio);
            viewPagerFotosCardapio.setAdapter(adapter);

            viewPagerFotosCardapio.setVisibility(View.VISIBLE);
            pageIndicatorFotosCardapio.setViewPager(viewPagerFotosCardapio);

        }else{
            viewPagerFotosCardapio.setVisibility(View.GONE);
        }
    }

    private List<Fragment> getFragmentsFotoCardapio(List<ImagemPasso> list) {
        if(list != null && list.size() > 0) {
            List<Fragment> listFragment = new ArrayList<Fragment>();
            for (ImagemPasso img : list) {
                listFragment.add(FragmentViewPagerCriaEventoFotosCardapio.newInstance(img, this));
            }
            return listFragment;
        }
        return null;
    }


    private void criaCampos(Passo2 passo2) {

        layoutCampos.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        
        List<Passo2.Campo> campos = passo2.campos();
        for (Passo2.Campo campo : campos){
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_campo_criar_evento, null);
            TextView txtNomeCampo = (TextView) layout.findViewById(R.id.txtNomeCampo);
            TextView txtCampoObrigatorio = (TextView) layout.findViewById(R.id.txtCampoObrigatorio);
            TextView txtValorCampo = (TextView) layout.findViewById(R.id.txtValorCampo);

            txtCampoObrigatorio.setVisibility(campo.isObrigatorio() ? View.VISIBLE : View.GONE);

            switch (campo){
                case TITULO:
                    txtNomeCampo.setText(R.string.cadastro_cardapio_titulo);
                    txtValorCampo.setText(passo2.getTitulo());
                    break;

                case TIPO:
                    txtNomeCampo.setText(R.string.cadastro_cardapio_tipo);
                    if(passo2.getTipo() != null) {
                        br.com.chef2share.enums.TipoEvento tipoEvento = br.com.chef2share.enums.TipoEvento.valueOf(passo2.getTipo());
                        txtValorCampo.setText(tipoEvento.getStringLabel());
                    }
                    break;

                case MENU:
                    txtNomeCampo.setText(R.string.cadastro_cardapio_menu);
                    txtValorCampo.setText(passo2.getDescricao());
                    break;

                case DESCRICAO:
                    txtNomeCampo.setText(R.string.cadastro_cardapio_descricao);
                    txtValorCampo.setText(passo2.getDescricao());
                    break;

                case BEBIDAS:
                    txtNomeCampo.setText(R.string.cadastro_cardapio_bebidas);
                    txtValorCampo.setText(passo2.getBebida());
                    break;

                case TIPO_COZINHA:
                    txtNomeCampo.setText(R.string.cadastro_cardapio_tipo_cozinha);
                    if(passo2.getCozinha() != null) {
                        txtValorCampo.setText(passo2.getCozinha().getLabel());
                    }
                    break;

                case OUTRAS_INFORMACOES:
                    txtNomeCampo.setText(R.string.cadastro_cardapio_outras_informacoes);
                    txtValorCampo.setText(passo2.getOutros());
                    break;
            }
            layout.setOnClickListener(onClickCampo(campo));
            layoutCampos.addView(layout);
        }
    }

    private View.OnClickListener onClickCampo(final Passo2.Campo campo) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, AlertInputDadoPasso2Activity_.class);
                Bundle param = new Bundle();
                param.putSerializable("campo", campo);
                i.putExtras(param);
                startActivityForResult(i, INPUT_DADO);
            }
        };
    }

    @Click({R.id.btnAddFotosCardapio})
    public void onClickTirarFotoCardapio(View v){

        final CharSequence[] items = { "Capturar com a câmera", "Importar do dispositivo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Adicionar imagem...");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capturar com a câmera")) {

                    capturarComCamera();

                } else if (items[item].equals("Importar do dispositivo")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Selecione a imagem..."), SELECT_PHOTO_CODE);
                }
            }
        });
        builder.show();
    }

    private void capturarComCamera() {
        File newdir = new File(diretorio);
        newdir.mkdirs();
        try {
            newfile = File.createTempFile(String.valueOf(new Date().getTime()), ".jpg", newdir);

        } catch (IOException e) {
            toast(e.getMessage());
            return;
        }

        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            if(newfile != null && newfile.exists()){
                try {

                    doInBackground(uploadUploadImagem(newfile), true, R.string.msg_aguarde_upload_imagem_cardapio, false);
                }catch(Exception e){
                    toast(e.getMessage());
                }
            }
        }

        if (resultCode == RESULT_OK && requestCode == INPUT_DADO) {
            passo2 = (Passo2) data.getSerializableExtra("passo2");
            criaCampos(passo2);
        }

        if (resultCode == RESULT_OK && requestCode == SELECT_PHOTO_CODE) {
            Uri selectedImageUri = data.getData();
            String[] projection = { MediaStore.MediaColumns.DATA };
            CursorLoader cursorLoader = new CursorLoader(this,selectedImageUri, projection, null, null, null);
            Cursor cursor =cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            newfile = new File(selectedImagePath);

            if(newfile != null && newfile.exists()){
                try {

                    doInBackground(uploadUploadImagem(newfile), true, R.string.msg_aguarde_upload_imagem_cardapio, false);
                }catch(Exception e){
                    toast(e.getMessage());
                }
            }

        }
    }

    private Transacao uploadUploadImagem(final File newfile) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                ImagemPasso imagemPasso = new ImagemPasso();
                imagemPasso.setTemp(response.getNames().get(0));
                imagemPasso.setTempFile(newfile);
                imagemPasso.setId(newfile.getName());
                passo2.addImagemPasso(imagemPasso);

                SuperApplication.getSuperCache().getEvento().setPasso2(passo2);

                criaViewPagerFotos(passo2);
            }

            @Override
            public void execute() throws Exception {

                UploadImagem uploadImagem = new UploadImagem();
                uploadImagem.setExtensao("jpeg");
                uploadImagem.setBase64(SuperUtils.getBase64(newfile));
                JSONObject json = SuperGson.toJSONObject(uploadImagem);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.IMAGEM_UPLOAD, json);
            }
        };
    }

    @Override
    public void onClickDelete(final ImagemPasso imagemPasso) {

        SuperUtil.alertDialogConfirmacao(activity, getResources().getString(R.string.msg_confirma_deletar_foto), R.string.sim, new Runnable() {
            @Override
            public void run() {

                //doInBackground(transacaoDeletarImagem(imagemPasso), true, R.string.msg_aguarde_deletando_imgem_cardapio, false);

            }
        }, R.string.nao, new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    public Transacao transacaoDeletarImagem(final ImagemPasso imagemPasso){
        return new Transacao() {
            @Override
            public void onError(String msgErro) {

            }

            @Override
            public void onSuccess(Response response) {

            }

            @Override
            public void execute() throws Exception {
//                ExcluirImagemLocal excluir = new ExcluirImagemLocal();
//
//                Imagem imagem = new Imagem();
//                imagem.setId(imagemPasso.getImagem().getId());
//                excluir.setImagem(imagem);
//
//                Passo2 p = new Passo2();
//                p.setId(passo2.getId());
//                excluir.setPasso2(p);
//
//                JSONObject json = SuperGson.toJSONObject(excluir);
//                superService.sendRequest(getBaseContext(), superActivity, SuperService.TipoTransacao.EXCLUIR_IMAGEM_LOCAL, json);
            }
        };
    }

    @Override
    public void onClickFotoDivulgacao(ImagemPasso imagemPasso, boolean isChecked) {
        List<ImagemPasso> list = passo2.getImagensPasso2();
        List<ImagemPasso> nova = new ArrayList<ImagemPasso>();
        for (ImagemPasso img : list){
            if(StringUtils.equals(img.getTemp(), imagemPasso.getTemp()) && isChecked){
                img.setDivulgacao(true);
            }else{
                img.setDivulgacao(false);
            }
            nova.add(img);
        }

        passo2.setImagensPasso2(nova);
        SuperApplication.getSuperCache().getEvento().setPasso2(passo2);

        Bundle param = new Bundle();
        param.putInt("idxImg", list.indexOf(imagemPasso));
        param.putBoolean("refresh", true);
        SuperUtil.show(getBaseContext(), CriarEventoPasso2Activity_.class, param);
        finish();
    }

    @Override
    public void onClickFotoPrincipal(ImagemPasso imagemPasso, boolean isChecked) {
        List<ImagemPasso> list = passo2.getImagensPasso2();
        List<ImagemPasso> nova = new ArrayList<ImagemPasso>();
        for (ImagemPasso img : list){
            if(StringUtils.equals(img.getTemp(), imagemPasso.getTemp()) && isChecked){
                img.setPrincipal(true);
            }else{
                img.setPrincipal(false);
            }
            nova.add(img);
        }

        passo2.setImagensPasso2(nova);
        SuperApplication.getSuperCache().getEvento().setPasso2(passo2);

        Bundle param = new Bundle();
        param.putInt("idxImg", list.indexOf(imagemPasso));
        param.putBoolean("refresh", true);
        SuperUtil.show(getBaseContext(), CriarEventoPasso2Activity_.class, param);
        finish();
    }

    @Override
    public void onClickAddFoto() {
        onClickTirarFotoCardapio(null);
    }

    @Click({R.id.btnProximo})
    public void onClickProximo(View v){
        if(!passo2.isInformacoesCompletas()){
            SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msg_informe_dados_cardapio));
            return;
        }

        doInBackground(transacaoProsseguir(), true, R.string.msg_aguarde_salvando_cardapio, false);
    }

    private Transacao transacaoProsseguir() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {

            }

            @Override
            public void onSuccess(Response response) {
                SuperApplication.getSuperCache().setCadastro(response.getCadastro());
                SuperApplication.getSuperCache().setEvento(response.getCadastro().getEvento());
                finish();
            }

            @Override
            public void execute() throws Exception {
                Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
                evento.setPasso1(null);
                evento.setPasso3(null);
                JSONObject json = SuperGson.toJSONObject(evento);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.PASSO2_PROSSEGUIR, json);
            }
        };
    }

    /**
     * Caso o usuario clique em voltar zera o cardapio
     * @param v
     */
    @Override
    public void onClickVoltar(View v) {
        Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
        evento.setPasso2(null);
        super.onClickVoltar(v);
    }

    @Override
    public void onBackPressed() {
        Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
        evento.setPasso2(null);
        super.onBackPressed();
    }
}
