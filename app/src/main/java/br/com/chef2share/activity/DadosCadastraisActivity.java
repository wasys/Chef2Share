package br.com.chef2share.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.ImageUtils;
import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Imagem;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.UploadImagem;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.request.AlterarSenha;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.enums.Genero;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.TelefoneMaskEditTextChangeListener;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_dados_cadastrais)
public class DadosCadastraisActivity extends SuperActivity  {

    @ViewById public TextView txtTitulo;
    @ViewById public EditText txtNome;
    @ViewById public EditText txtEmail;
    @ViewById public EditText txtRedeSocialFacebook;
    @ViewById public EditText txtRedeSocialGooglePlus;
    @ViewById public EditText txtRedeSocialWhatsApp;
    @ViewById public Spinner spinnerGenero;
    @ViewById public Button btnDataNascimento;
    @ViewById public EditText tNovaSenha;
    @ViewById public EditText tConfirmaNovaSenha;
    @ViewById public RoundedImageView imgFotoPerfil;

    @AnimationRes
    public Animation fadeIn;

    private static File newfile;

    private final int SELECT_PHOTO_CODE = 777;
    private final int TAKE_PHOTO_CODE = 999;

    private DatePickerDialog datePicker;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtTitulo.setText(R.string.titulo_cadastro);

        txtRedeSocialWhatsApp.addTextChangedListener(new TelefoneMaskEditTextChangeListener(txtRedeSocialWhatsApp));

        Usuario usuario = UsuarioService.getUsuario(getBaseContext());
        setDadosUsuario(usuario);
    }

    private void setDadosUsuario(Usuario usuario) {
        ArrayAdapter adapter = new ArrayAdapter<Genero>(this, R.layout.item_spinner, Genero.values());
        adapter.setDropDownViewResource(R.layout.item_spinner_drodown);
        spinnerGenero.setAdapter(adapter);

        setDateTimeField();

        setTextString(txtNome, usuario.getNome());
        setTextString(txtRedeSocialFacebook, usuario.getFacebook());
        setTextString(txtRedeSocialGooglePlus, usuario.getGoogle());
        setTextString(txtRedeSocialWhatsApp, usuario.getWhatsapp());
        setTextString(txtEmail, usuario.getEmail());
        setTextString(btnDataNascimento, DateUtils.toString(usuario.getDataNascimentoDate(), DateUtils.DATE));
        if(usuario.getSexo() != null){
            spinnerGenero.setSelection(usuario.getSexo().ordinal());
        }


        String urlMeuPerfil = SuperCloudinery.getUrlImgPessoa(getBaseContext(), usuario.getImagem());
        if(StringUtils.isNotEmpty(urlMeuPerfil)) {
            Picasso.with(getBaseContext()).load(urlMeuPerfil).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgFotoPerfil);

        }else{
            imgFotoPerfil.setImageResource(R.drawable.userpic);
        }
    }


    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();

        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                btnDataNascimento.setText(DateUtils.toString(newDate.getTime(), DateUtils.DATE));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Click(R.id.btnDataNascimento)
     public void onClickDataNascimento(View v){
        datePicker.show();
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
        String facebook = getTextString(txtRedeSocialFacebook);
        String googlePlus = getTextString(txtRedeSocialGooglePlus);
        String whatsapp = getTextString(txtRedeSocialWhatsApp);
        String dataNascimento = getTextString(btnDataNascimento);
        Genero genero = (Genero) spinnerGenero.getSelectedItem();

        if(StringUtils.isEmpty(nome)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_nome);
            return;
        }

        Usuario usuario = UsuarioService.getUsuario(getBaseContext());
        usuario.setNome(nome);
        usuario.setDataNascimento(dataNascimento);
        usuario.setFacebook(facebook);
        usuario.setGoogle(googlePlus);
        usuario.setWhatsapp(whatsapp);
        usuario.setSexo(genero);

        doInBackground(getTransacaoAtualizaDadosCadastrais(usuario), false);
    }

    private Transacao getTransacaoAtualizaDadosCadastrais(final Usuario usuario) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                UsuarioService.saveOrUpdate(getBaseContext(), response.getUsuario());
                SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msg_dados_atualizados_com_sucesso));

                Usuario usuario = UsuarioService.getUsuario(getBaseContext());
                setDadosUsuario(usuario);
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(usuario);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.USUARIO_ATUALIZAR, json);
            }
        };
    }

    @Click(R.id.btnAlterarSenha)
    public void onClickAlterarSenha(View v){
        AppUtil.toast(getBaseContext(), "Alterar senha");

        String novaSenha = getTextString(tNovaSenha);
        String confirmaNovaSenha = getTextString(tConfirmaNovaSenha);

        if(StringUtils.isEmpty(novaSenha)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_senha);
            return;
        }
        if(StringUtils.isEmpty(confirmaNovaSenha)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_senha_confirmacao);
            return;
        }

        if(!StringUtils.equals(novaSenha, confirmaNovaSenha)){
            toast("Senha inválida");
            return;
        }

        AlterarSenha alterarSenha = new AlterarSenha();
        alterarSenha.setNova(novaSenha);
        alterarSenha.setRepeticao(confirmaNovaSenha);
        doInBackground(getTransacaoAlterarSenha(alterarSenha), false);
    }

    private Transacao getTransacaoAlterarSenha(final AlterarSenha alterarSenha) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                UsuarioService.saveOrUpdate(getBaseContext(), response.getUsuario());
                SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msg_senha_alterada_com_sucesso));
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(alterarSenha);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.ALTERAR_SENHA, json);
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
                doInBackground(getDadosCadastraisServer(), true, R.string.msg_atualizando_dados_usuario, false);
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(imagem);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.USUARIO_ATUALIZAR_FOTO, json);
            }
        };
    }

    private Transacao getDadosCadastraisServer() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                Usuario usuario = response.getUsuario();
                UsuarioService.saveOrUpdate(getBaseContext(), usuario);

                Usuario u = UsuarioService.getUsuario(getBaseContext());
                setDadosUsuario(u);
            }

            @Override
            public void execute() throws Exception {
                Usuario usuario = UsuarioService.getUsuario(getBaseContext());
                superService.getDadosUsuario(getBaseContext(), superActivity, superActivity, usuario);
            }
        };
    }
}
