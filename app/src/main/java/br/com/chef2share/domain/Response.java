package br.com.chef2share.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 18/11/2015.
 */
public class Response {

    private int code;
    private Status status;
    private List<String> messages;
    private Usuario usuario;
    private Home home;
    private Filtro filtro;
    private Busca busca;
    private Detalhes detalhe;
    private Reserva realizado;
    private UsuarioPedido usuarioPedido;
    private Resumo resumo;
    private Cadastro cadastro;
    private List<String> names;
    private ConsultaMensagem result;
    private ChefEvento chefEvento;
    private AcompanhaEvento acompanhe;
    private PerfilChef cadastroChef;
    private Checkin checkin;

    public static final int STATUS_200 = 200;

    public static Response getDeafult() {
        Response r = new Response();
        r.setCode(STATUS_200);
        return r;
    }

    public static Response getErro(String message) {
        Response r = new Response();
        r.setCode(STATUS_200);
        r.addMessage(message);
        return r;
    }

    public enum Status{
        SUCCESS,
        FAILURE
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    public Detalhes getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(Detalhes detalhe) {
        this.detalhe = detalhe;
    }

    public Busca getBusca() {
        return busca;
    }

    public void setBusca(Busca busca) {
        this.busca = busca;
    }

    public Reserva getRealizado() {
        return realizado;
    }

    public void setRealizado(Reserva realizado) {
        this.realizado = realizado;
    }

    public UsuarioPedido getUsuarioPedido() {
        return usuarioPedido;
    }

    public void setUsuarioPedido(UsuarioPedido usuarioPedido) {
        this.usuarioPedido = usuarioPedido;
    }

    public PerfilChef getCadastroChef() {
        return cadastroChef;
    }

    public void setCadastroChef(PerfilChef cadastroChef) {
        this.cadastroChef = cadastroChef;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public Resumo getResumo() {
        return resumo;
    }

    public void setResumo(Resumo resumo) {
        this.resumo = resumo;
    }

    public ConsultaMensagem getResult() {
        return result;
    }

    public void setResult(ConsultaMensagem result) {
        this.result = result;
    }

    public Cadastro getCadastro() {
        return cadastro;
    }

    public void setCadastro(Cadastro cadastro) {
        this.cadastro = cadastro;
    }

    public ChefEvento getChefEvento() {
        return chefEvento;
    }

    public AcompanhaEvento getAcompanhe() {
        return acompanhe;
    }

    public void setAcompanhe(AcompanhaEvento acompanhe) {
        this.acompanhe = acompanhe;
    }

    public void setChefEvento(ChefEvento chefEvento) {
        this.chefEvento = chefEvento;
    }

    public Checkin getCheckin() {
        return checkin;
    }

    public void setCheckin(Checkin checkin) {
        this.checkin = checkin;
    }
    //    public Map<String, String> getDicionario() {
//        return dicionario;
//    }
//
//    public void setDicionario(Map<String, String> dicionario) {
//        this.dicionario = dicionario;
//    }

    public String getMensagemCompleta(){
        StringBuffer sb = new StringBuffer("");
        if(messages != null){
            for (String msg : messages) {
                sb.append(msg).append("\n\n");
            }
        }
        return sb.toString();
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String msg){
        if(messages == null){
            messages = new ArrayList<String>();
        }
        messages.add(msg);
    }
}
