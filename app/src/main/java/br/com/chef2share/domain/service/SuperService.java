package br.com.chef2share.domain.service;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.android.utils.lib.utils.FileUtils;
import com.android.utils.lib.utils.IOUtils;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.json.JSONObject;

import br.com.chef2share.BuildConfig;
import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.SuperActivity;
import br.com.chef2share.domain.Busca;
import br.com.chef2share.domain.Detalhes;
import br.com.chef2share.domain.Filtro;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.infra.Chef2ShareJSONRequest;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.infra.TransacaoCallback;

/**
 * Created by Jonas on 20/11/2015.
 */
@EBean
public class SuperService {

//    @Bean
//    public TransacaoCallback transacaoCallback;

    public static final String URL = BuildConfig.URL;
    public static final String PATH_MOBILE = "/mb";
    public static final String PATH_AJUDA = "/ajuda.xhtml?client=webview";
    public static final String PATH_TERMOS = "/termos.xhtml?client=webview";
    public static final String PATH_FUNCIONALIDADES = "/funcionalidades.xhtml?client=webview&login=";
    public static final String PATH_FERRAMENTAS = "/ferramentas.xhtml?client=webview&login=";


    public static final boolean SIMULA_SERVER = false;

    public enum TipoTransacao {
        LOGIN("/login"),
        DADOS_USUARIO("/usuario"),
        LOGIN_GOOGLE("/login/google"),
        LOGIN_FACEBOOK("/login/facebook"),
        HOME("/home"),
        EVENTO_FILTRO("/evento/filtro"),
        USUARIO_CADASTRO("/usuario/cadastrar"),
        USUARIO_ATUALIZAR("/usuario/atualizar"),
        USUARIO_ATUALIZAR_FOTO("/usuario/foto/salvar"),
        EVENTO_DETALHE("/evento/detalhe"),
        EVENTO_BUSCA("/evento/busca"),
        ALTERAR_SENHA("/usuario/senha"),
        EVENTO_COMPRAR("/compra/confirmar"),
        EVENTOS_QUE_VOU("/usuario/pedido/filtrar"),
        PEDIDO_RESUMO("/pedido/resumo"),
        AVALIAR_EVENTO("/avaliacao/avaliar"),
        EVENTO_CADASTRO("/evento/cadastro"),
        IMAGEM_UPLOAD("/imagem/upload"),
        PASSO1_PROSSEGUIR("/passo1/prosseguir"),
        PASSO2_PROSSEGUIR("/passo2/prosseguir"),
        PASSO3_PROSSEGUIR("/passo3/prosseguir"),
        PASSO4_PUBLICAR("/passo4/publicar"),
        EXCLUIR_IMAGEM_LOCAL("/imagem/passo1/excluir"),
        CONSULTA_MENSAGEM("/mensagem"),
        MENSAGEM_DETALHES("/mensagem/procurar"),
        MENSAGEM_CONVERSA("/mensagem/conversa"),
        MENSAGEM_ENVIAR("/mensagem/enviar"),
        CHEF_EVENTO("/chef/evento"),
        CHEF_EVENTO_FILTRAR("/chef/evento/filtrar"),
        ACOMPANHA_EVENTO("/acompanhe"),
        ACOMPANHA_EVENTO_CHECKIN("/acompanhe/checkin"),
        ACOMPANHA_EVENTO_CHECKIN_DESFAZER("/acompanhe/desfazer"),
        PERFIL_CHEF_DADOS("/chef"),
        PERFIL_CHEF_ATUALIZAR("/chef/atualizar"),
        PERFIL_CHEF_ATUALIZAR_FOTO("/chef/foto/salvar"),

        /**
         * Mehorias
         */
        USUARIO_RECUPERAR_SENHA("/usuario/recuperarSenha"),

        ;


        String path;

        TipoTransacao(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public void sendRequest(final Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, TipoTransacao tipoTransacao) {
        sendRequest(context, responseListener, errorListener, tipoTransacao, null);
    }

    public void sendRequest(final Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, TipoTransacao tipoTransacao, final JSONObject jsonParam) {
        String url = getURLMobile() + tipoTransacao.getPath();
        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, jsonParam, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }

    private String getURLMobile() {
        return URL + PATH_MOBILE;
    }

    public String getURLTermosDeUso() {
        return URL + PATH_TERMOS;
    }

    public String getURLAjuda() {
        return URL + PATH_AJUDA;
    }

    public String getURLFuncionalidades(Context context) {
        return URL + PATH_FUNCIONALIDADES + UsuarioService.getUsuario(context).getEmail();
    }

    public String getURLFerramentas(Context context) {
        return URL + PATH_FERRAMENTAS + UsuarioService.getUsuario(context).getEmail();
    }


    public void detalhesEvento(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String eventoId) {
        TipoTransacao tipoTransacao = TipoTransacao.EVENTO_DETALHE;
        String url = getURLMobile() + tipoTransacao.getPath() + "/" + eventoId;

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }

    public void detalhesRascunho(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String eventoId) {
        TipoTransacao tipoTransacao = TipoTransacao.EVENTO_CADASTRO;
        String url = getURLMobile() + tipoTransacao.getPath() + "/" + eventoId;

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }
    public void acompanhaEvento(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String eventoId) {
        TipoTransacao tipoTransacao = TipoTransacao.ACOMPANHA_EVENTO;
        String url = getURLMobile() + tipoTransacao.getPath() + "/" + eventoId;

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }

    public void detalhesPedido(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String pedidoId) {
        TipoTransacao tipoTransacao = TipoTransacao.PEDIDO_RESUMO;
        String url = getURLMobile() + tipoTransacao.getPath() + "/" + pedidoId;

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }
    public void mensagemConversa(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String idRemetente) {
        TipoTransacao tipoTransacao = TipoTransacao.MENSAGEM_CONVERSA;
        String url = getURLMobile() + tipoTransacao.getPath() + "/" + idRemetente;

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }
    public void publicarEvento(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String eventoId) {
        TipoTransacao tipoTransacao = TipoTransacao.PASSO4_PUBLICAR;
        String url = getURLMobile() + tipoTransacao.getPath() + "/" + eventoId;

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }
    public void checkinConvite(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String usuarioId, String ingressoId, String numeroEntrada) {
        TipoTransacao tipoTransacao = TipoTransacao.ACOMPANHA_EVENTO_CHECKIN;

        StringBuffer sb = new StringBuffer(getURLMobile());
        sb.append(tipoTransacao.getPath()).append("/").append(usuarioId).append("/").append(ingressoId).append("/").append(numeroEntrada);

        String url = sb.toString();

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }

    public void checkoutConvite(Context context, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener, String usuarioId, String ingressoId, String numeroEntrada) {
        TipoTransacao tipoTransacao = TipoTransacao.ACOMPANHA_EVENTO_CHECKIN_DESFAZER;

        StringBuffer sb = new StringBuffer(getURLMobile());
        sb.append(tipoTransacao.getPath()).append("/").append(usuarioId).append("/").append(ingressoId).append("/").append(numeroEntrada);

        String url = sb.toString();

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }

    public void getDadosUsuario(Context context, SuperActivity responseListener, SuperActivity errorListener, Usuario usuario) {
        TipoTransacao tipoTransacao = TipoTransacao.DADOS_USUARIO;

        StringBuffer sb = new StringBuffer(getURLMobile());
        sb.append(tipoTransacao.getPath()).append("/").append(usuario.getId());

        String url = sb.toString();

        SuperUtil.log("REQUEST " + url);

        JsonObjectRequest chef2ShareJSONRequest = new Chef2ShareJSONRequest(context, Request.Method.POST, url, null, responseListener, errorListener);
        SuperApplication.getInstance().addToRequestQueue(chef2ShareJSONRequest, tipoTransacao.name());
    }

    public void cancelRequest(TipoTransacao tipoTransacao){
        SuperApplication.getInstance().getRequestQueue().cancelAll(tipoTransacao.name());
    }


    /**
     * @param context
     * @return
     */
    public static String getPushID(Context context) {
        String msg = "";
        try {

            InstanceID instanceID = InstanceID.getInstance(context);
            String regId = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            /*
            GoogleCloudMessaging instance = GoogleCloudMessaging.getInstance(context);
            String regId = instance.register(context.getResources().getString(R.string.gcm_sender_id));
            */
            SuperUtil.log("PUSH ID: " + regId);
            return regId;

        } catch (Exception e) {
            SuperUtil.logError(e.getMessage(), e);
        }

        return msg;
    }

    /**
     * ***************************************************
     * ***************************************************
     * ***FAKE FAKE FAKE FAKE FAKE FAKE FAKE FAKE FAKE ***
     * ***************************************************
     * ***************************************************
     * @param context
     * @param transacao
     * @param tipoTransacao
     * @return
     */
//    private TransacaoCallback getTransacaoCallbackFake(final Context context, final Transacao transacao, final TipoTransacao tipoTransacao) {
//
//        if(!SuperService.SIMULA_SERVER){
//            return null;
//        }
//
//        return new TransacaoCallback(){
//
//            @Override
//            public void onResponse(JSONObject json) {
//                transacao.onSuccess(criaResponse());
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                transacao.onSuccess(criaResponse());
//            }
//
//            private Response criaResponse(){
//                Response response = null;
//                try{
//
//                    switch (tipoTransacao) {
//                        case EVENTO_DETALHE:
//                            String detalhesEvento = IOUtils.toString(FileUtils.readRawFile(context, R.raw.detalhes_evento));
//                            response = SuperGson.fromJson(detalhesEvento, Response.class);
//                            break;
//                        case EVENTO_BUSCA:
//                            String eventoBusca = IOUtils.toString(FileUtils.readRawFile(context, R.raw.evento_busca));
//                            response = SuperGson.fromJson(eventoBusca, Response.class);
//                            break;
//                        case EVENTO_FILTRO:
//                            String eventoFiltro = IOUtils.toString(FileUtils.readRawFile(context, R.raw.evento_filtro));
//                            response = SuperGson.fromJson(eventoFiltro, Response.class);
//                            break;
//                        case HOME:
//                            String home = IOUtils.toString(FileUtils.readRawFile(context, R.raw.home));
//                            response = SuperGson.fromJson(home, Response.class);
//                            break;
//                        case LOGIN:
//                            String login = IOUtils.toString(FileUtils.readRawFile(context, R.raw.login));
//                            response = SuperGson.fromJson(login, Response.class);
//                            break;
//                    }
//
//
//                }catch(Exception e){
//                    SuperUtil.logError(e.getMessage(), e);
//                    transacao.onError(e.getMessage());
//                }
//
//                return response;
//            }
//        };
//    }

}
