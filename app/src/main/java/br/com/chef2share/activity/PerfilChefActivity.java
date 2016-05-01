package br.com.chef2share.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Imagem;
import br.com.chef2share.domain.PerfilChef;
import br.com.chef2share.domain.PerfilChefLocal;
import br.com.chef2share.domain.PerfilChefTratamento;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.UploadImagem;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_perfil_chef)
public class PerfilChefActivity extends SuperActivity  {

    @ViewById public TextView txtTitulo;
    @ViewById public EditText txtNome;
    @ViewById public EditText txtResumo;
    @ViewById public EditText txtRedeSocialYoutube;
    @ViewById public EditText txtRedeSocialTwitter;
    @ViewById public EditText txtRedeSocialLinkedin;
    @ViewById public EditText txtRedeSocialInstagram;
    @ViewById public Spinner spinnerTratamento;
    @ViewById public Spinner spinnerLocalPrincipal;
    @ViewById public RoundedImageView imgFotoPerfil;
    @ViewById public RatingBar ratingAvaliacaoAtendimento;
    @ViewById public RatingBar ratingAvaliacaoComida;
    @ViewById public RatingBar ratingAvaliacaoLocal;

    private PerfilChef perfilChef;

    private static File newfile;
    private final int SELECT_PHOTO_CODE = 777;
    private final int TAKE_PHOTO_CODE = 999;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtTitulo.setText(R.string.titulo_perfil_chef);

        doInBackground(getPerfilChef(), true, R.string.msg_aguarde_carregando_perfil_chef, false);
    }

    private Transacao getPerfilChef() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {

            }

            @Override
            public void onSuccess(Response response) {
                setPerfilChef(response.getCadastroChef());
            }

            @Override
            public void execute() throws Exception {
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.PERFIL_CHEF_DADOS, null);
            }
        };
    }

    private void setPerfilChef(PerfilChef perfilChef) {
        this.perfilChef = perfilChef;
        Chef chef = perfilChef.getChef();

        ArrayAdapter adapterTratamento = new ArrayAdapter<PerfilChefTratamento>(this, R.layout.item_spinner, perfilChef.getTratamentos());
        adapterTratamento.setDropDownViewResource(R.layout.item_spinner_drodown);
        spinnerTratamento.setAdapter(adapterTratamento);
        spinnerTratamento.setSelection(perfilChef.getPositionTratamento(chef.getTratamento()));

        ArrayAdapter adapterLocal = new ArrayAdapter<PerfilChefLocal>(this, R.layout.item_spinner, perfilChef.getLocais());
        adapterLocal.setDropDownViewResource(R.layout.item_spinner_drodown);
        spinnerLocalPrincipal.setAdapter(adapterLocal);
        spinnerLocalPrincipal.setSelection(perfilChef.getPositionLocalPrincipal(chef.getLocalPrincipal()));


        setTextString(txtNome, chef.getNome());
        setTextString(txtResumo, chef.getResumo());
        setTextString(txtRedeSocialInstagram, chef.getInstagram());
        setTextString(txtRedeSocialLinkedin, chef.getLinkedin());
        setTextString(txtRedeSocialTwitter, chef.getTwitter());
        setTextString(txtRedeSocialYoutube, chef.getYoutube());


        String urlMeuPerfil = SuperCloudinery.getUrlImgPessoa(getBaseContext(), chef.getImagem());
        if(StringUtils.isNotEmpty(urlMeuPerfil)) {
            Picasso.with(getBaseContext()).load(urlMeuPerfil).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgFotoPerfil);
        }else{
            imgFotoPerfil.setImageResource(R.drawable.userpic);
        }

        ratingAvaliacaoAtendimento.setRating((float) SuperUtil.toDoublePrimitivo(chef.getAvaliacaoAtendimento()));
        ratingAvaliacaoComida.setRating((float) SuperUtil.toDoublePrimitivo(chef.getAvaliacaoComida()));
        ratingAvaliacaoLocal.setRating((float) SuperUtil.toDoublePrimitivo(chef.getAvaliacaoLocal()));
    }


    @Override
    public void onClickVoltar(View v) {
        finish();
    }

    @Click({R.id.btnTirarFoto, R.id.imgFotoPerfil})
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

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
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

    @Click(R.id.btnSalvar)
    public void onClickSalvar(View v){
        String nome = getTextString(txtNome);
        String resumo = getTextString(txtResumo);
        String youtube = getTextString(txtRedeSocialYoutube);
        String twitter = getTextString(txtRedeSocialTwitter);
        String instagram = getTextString(txtRedeSocialInstagram);
        String linkedin = getTextString(txtRedeSocialLinkedin);
        PerfilChefTratamento tratamento = (PerfilChefTratamento) spinnerTratamento.getSelectedItem();
        PerfilChefLocal localPrincipal = (PerfilChefLocal) spinnerLocalPrincipal.getSelectedItem();

        if(StringUtils.isEmpty(nome)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_nome);
            return;
        }

        Chef chef = new Chef();
        chef.setId(this.perfilChef.getChef().getId());
        chef.setNome(nome);
        chef.setResumo(resumo);
        chef.setTratamento(tratamento != null ? tratamento.getValue() : null);
        chef.setLocalPrincipal(localPrincipal);
        chef.setInstagram(instagram);
        chef.setYoutube(youtube);
        chef.setTwitter(twitter);
        chef.setLinkedin(linkedin);

        doInBackground(getTransacaoAtualizaPerfilChef(chef), true, R.string.msg_aguarde_atualizando_perfil_chef, false);
    }

    private Transacao getTransacaoAtualizaPerfilChef(final Chef chef) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msg_dados_atualizados_com_sucesso));
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(chef);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.PERFIL_CHEF_ATUALIZAR, json);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            if(newfile != null && newfile.exists()){
                try {

                    doInBackground(uploadUploadImagem(newfile), true, R.string.msg_aguarde_upload_imagem_cadastro, false);
                }catch(Exception e){
                    toast(e.getMessage());
                }
            }
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

                    doInBackground(uploadUploadImagem(newfile), true, R.string.msg_aguarde_upload_imagem_cadastro, false);
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
                Imagem imagem = new Imagem();
                imagem.setTemp(response.getNames().get(0));
                doInBackground(getTransacaoAtualizaFoto(imagem), true, R.string.msg_aguarde_upload_imagem_cadastro, false);
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

    private Transacao getTransacaoAtualizaFoto(final Imagem imagem) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                doInBackground(getPerfilChef(), true, R.string.msg_aguarde_carregando_perfil_chef, false);
            }

            @Override
            public void execute() throws Exception {
                Imagem img = new Imagem();
                img.setTemp(imagem.getTemp());
                JSONObject json = SuperGson.toJSONObject(img);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.PERFIL_CHEF_ATUALIZAR_FOTO, json);
            }
        };
    }
}
