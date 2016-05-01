package br.com.chef2share.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
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
import br.com.chef2share.domain.Imagem;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.UploadImagem;
import br.com.chef2share.domain.listener.OnClickFotoLocal;
import br.com.chef2share.domain.request.ExcluirImagemLocal;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.fragment.FragmentViewPagerCriaEventoFotosLocal;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.CirclePageIndicator;

@EActivity(R.layout.activity_criar_evento_passo_um)
public class CriarEventoPasso1Activity extends SuperActivityMainMenu implements OnClickFotoLocal {

    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;

    @ViewById public ViewPager viewPagerFotosLocal;
    @ViewById public CirclePageIndicator pageIndicatorFotosLocal;
    @ViewById public Button btnProximo;
    
    @ViewById public LinearLayout layoutCampos;

    private static File newfile;
    private final int TAKE_PHOTO_CODE = 999;
    private final int SELECT_PHOTO_CODE = 777;
    public static final int INPUT_DADO = 888;
    private String diretorio;
    private Passo1 passo1;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtSubTitulo.setVisibility(View.VISIBLE);

        setTextString(txtTitulo, getString(R.string.titulo_criar_evento));
        setTextString(txtSubTitulo, getString(R.string.criar_evento_passo_um_desc));

        viewPagerFotosLocal.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getBaseContext());

        diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/" + new Date().getTime() + "/";

        passo1 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso1();

        criaCampos(passo1);
        criaViewPagerFotos(passo1);
    }

    private void criaViewPagerFotos(Passo1 passo1) {
        List<Fragment> fragmentsFotosLocal = getFragmentsFotoLocal(passo1.getImagensPasso1());
        if (fragmentsFotosLocal != null && fragmentsFotosLocal.size() > 0) {
            viewPagerFotosLocal.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentsFotosLocal));

            viewPagerFotosLocal.setVisibility(View.VISIBLE);
            pageIndicatorFotosLocal.setViewPager(viewPagerFotosLocal);

        }else{
            viewPagerFotosLocal.setVisibility(View.GONE);
        }
    }

    private List<Fragment> getFragmentsFotoLocal(List<ImagemPasso> list) {
        if(list != null && list.size() > 0) {
            List<Fragment> listFragment = new ArrayList<Fragment>();
            for (ImagemPasso img : list) {
                listFragment.add(FragmentViewPagerCriaEventoFotosLocal.newInstance(img, this));
            }
            return listFragment;
        }
        return null;
    }

    private void criaCampos(Passo1 passo1) {

        layoutCampos.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        
        List<Passo1.Campo> campos = passo1.campos();
        for (Passo1.Campo campo : campos){
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_campo_criar_evento, null);
            TextView txtNomeCampo = (TextView) layout.findViewById(R.id.txtNomeCampo);
            TextView txtCampoObrigatorio = (TextView) layout.findViewById(R.id.txtCampoObrigatorio);
            TextView txtValorCampo = (TextView) layout.findViewById(R.id.txtValorCampo);

            txtCampoObrigatorio.setVisibility(campo.isObrigatorio() ? View.VISIBLE : View.GONE);

            switch (campo){
                case NOME:
                    txtNomeCampo.setText(R.string.cadastro_local_nome);
                    txtValorCampo.setText(passo1.getNome());
                    break;

                case DESCRICAO:
                    txtNomeCampo.setText(R.string.cadastro_local_descricao);
                    txtValorCampo.setText(passo1.getDescricao());
                    break;

                case ESTACIONAMENTO:
                    txtNomeCampo.setText(R.string.cadastro_local_estacionamento);
                    txtValorCampo.setText(passo1.getEstacionamento());
                    break;

                case ENDERECO:
                    txtNomeCampo.setText(R.string.cadastro_local_endereco);
                    txtValorCampo.setText(passo1.getEnderecoDesc());
                    break;
            }
            layout.setOnClickListener(onClickCampo(campo));
            layoutCampos.addView(layout);
        }
    }

    private View.OnClickListener onClickCampo(final Passo1.Campo campo) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = null;

                switch (campo){

                    case ENDERECO:
                        i = new Intent(activity, EnderecoLocalActivity_.class);
                        break;

                    default:
                        i = new Intent(activity, AlertInputDadoActivity_.class);
                        break;
                }


                Bundle param = new Bundle();
                param.putSerializable("campo", campo);
                i.putExtras(param);
                startActivityForResult(i, INPUT_DADO);
            }
        };
    }

    @Click({R.id.btnAddFotosLocal})
    public void onClickTirarFoto(View v){

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

                    doInBackground(uploadUploadImagem(newfile), true, R.string.msg_aguarde_upload_imagem_local, false);

                }catch(Exception e){
                    toast(e.getMessage());
                }
            }
        }

        if (resultCode == RESULT_OK && requestCode == INPUT_DADO) {
            criaCampos(passo1);
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

                    doInBackground(uploadUploadImagem(newfile), true, R.string.msg_aguarde_upload_imagem_local, false);
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
                passo1.addImagemPasso(imagemPasso);

                SuperApplication.getSuperCache().getEvento().setPasso1(passo1);

                criaViewPagerFotos(passo1);
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

        SuperUtil.toast(superActivity, imagemPasso.getTemp());

        SuperUtil.alertDialogConfirmacao(activity, getResources().getString(R.string.msg_confirma_deletar_foto), R.string.sim, new Runnable() {
            @Override
            public void run() {


                passo1.removeImagem(imagemPasso);
                SuperApplication.getSuperCache().getEvento().setPasso1(passo1);
                criaViewPagerFotos(passo1);

            }
        }, R.string.nao, new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    @Override
    public void onClickAddFoto() {
        onClickTirarFoto(null);
    }

    public Transacao transacaoDeletarImagem(final ImagemPasso imagemPasso){
        return new Transacao() {
            @Override
            public void onError(String msgErro) {

            }

            @Override
            public void onSuccess(Response response) {
                Passo1 passo1 = SuperApplication.getSuperCache().getEvento().getPasso1();
                criaCampos(passo1);
                criaViewPagerFotos(passo1);

            }

            @Override
            public void execute() throws Exception {
//                ExcluirImagemLocal excluir = new ExcluirImagemLocal();
//
//                Imagem imagem = new Imagem();
//                imagem.setId(imagemPasso.getImagem().getId());
//                excluir.setImagem(imagem);
//
//                Passo1 p = new Passo1();
//                p.setId(passo1.getId());
//                excluir.setPasso1(p);
//
//                JSONObject json = SuperGson.toJSONObject(excluir);
//                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.EXCLUIR_IMAGEM_LOCAL, json);

                if(passo1.getImagensPasso1() != null && passo1.getImagensPasso1().size() > 0) {
                    passo1.getImagensPasso1().remove(imagemPasso);
                    SuperApplication.getSuperCache().getEvento().setPasso1(passo1);
                }

            }
        };
    }

    @Click({R.id.btnProximo})
    public void onClickProximo(View v){
        if(!passo1.isInformacoesCompletas()){
            SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msg_informe_dados_local));
            return;
        }

        doInBackground(transacaoProsseguir(), true, R.string.msg_aguarde_salvando_local_evento, false);
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
                evento.setPasso2(null);
                evento.setPasso3(null);
                JSONObject json = SuperGson.toJSONObject(evento);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.PASSO1_PROSSEGUIR, json);
            }
        };
    }

    /**
     * Caso o usuario clique em voltar zera o local
     * @param v
     */
    @Override
    public void onClickVoltar(View v) {
        Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
        evento.setPasso1(null);
        super.onClickVoltar(v);
    }

    @Override
    public void onBackPressed() {
        Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
        evento.setPasso1(null);
        super.onBackPressed();
    }
}
