package br.com.chef2share.domain;

import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.chef2share.SuperApplication;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Passo1 implements Serializable {

    private String id;
    private String nome;
    private String descricao;
    private String estacionamento;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String logradouro;
    private String numero;
    private double latitude;
    private double longitude;
    private List<ImagemPasso> imagensPasso1;

    //Cache mobile
    private Local local;

    public String getEnderecoDesc() {
        StringBuffer sb = new StringBuffer("");

        if(StringUtils.isNotEmpty(logradouro)){
            sb.append(logradouro).append(", ");
        }

        if(StringUtils.isNotEmpty(numero)){
            sb.append(numero).append(" - ");
        }

        if(StringUtils.isNotEmpty(bairro)){
            sb.append(bairro).append(" - ");
        }
        if(StringUtils.isNotEmpty(cidade)){
            sb.append(cidade).append(" - ");
        }
        if(StringUtils.isNotEmpty(estado)){
            sb.append(estado);
        }

        return sb.toString();
    }

    public List<Campo> campos() {
        List<Campo> list = new ArrayList<Campo>();
        list.add(Campo.NOME);
        list.add(Campo.DESCRICAO);
        list.add(Campo.ESTACIONAMENTO);
        list.add(Campo.ENDERECO);
        return list;
    }

    public boolean isInformacoesCompletas() {
        boolean ok = true;

        ok &= StringUtils.isNotEmpty(nome);
//        ok &= StringUtils.isNotEmpty(descricao);
        ok &= StringUtils.isNotEmpty(logradouro);
        ok &= StringUtils.isNotEmpty(cep);
        ok &= StringUtils.isNotEmpty(bairro);
        ok &= StringUtils.isNotEmpty(numero);
        ok &= StringUtils.isNotEmpty(cidade);
        ok &= StringUtils.isNotEmpty(estado);
        return ok;
    }

    /**
     * Popula o Passo1 com as informações do Local selecionado
     * @param local
     */
    public void setLocal(Local local) {
        //só precisa do ID para ter referencia que qual cardapio é
        this.local = new Local();
        this.local.setId(local.getId());

        setBairro(local.getBairro());
        setLogradouro(local.getLogradouro());
        setNumero(local.getNumero());
        setCidade(local.getCidade());
        setCep(local.getCep());
        setDescricao(local.getDescricao());
        setEstacionamento(local.getEstacionamento());
        setEstado(local.getEstado());
        setLatitude(local.getLatitude());
        setLongitude(local.getLongitude());
        setNome(local.getNome());

        List<ImagemLocal> imagensLocal = local.getImagensLocal();
        if(imagensLocal == null){
            imagensLocal = new ArrayList<ImagemLocal>();
        }
        for(ImagemLocal imgLocal : imagensLocal){
            ImagemPasso imgPasso = new ImagemPasso();
            imgPasso.setId(imgLocal.getId());
            imgPasso.setImagem(imgLocal.getImagem());
            addImagemPasso(imgPasso);
        }
    }

    public void removeImagem(ImagemPasso imagemPasso) {
        if(imagensPasso1 != null && imagensPasso1.size() > 0) {

            List<ImagemPasso> nova = new ArrayList<ImagemPasso>();
            for (ImagemPasso img : imagensPasso1){
                if(!StringUtils.equals(img.getTemp(), imagemPasso.getTemp())){
                    nova.add(img);
                }
            }

            imagensPasso1 = nova;
        }
    }

    public enum Campo{
        NOME(true),
        DESCRICAO(true),
        ESTACIONAMENTO(false),
        ENDERECO(true),
        CEP(true),
        ESTADO(true),
        CIDADE(true),
        BAIRRO(true),
        LOGRADOURO(true),
        NUMERO(true),
        LATITUDE(true),
        LONGITUDE(true);

        Campo(boolean obrigatorio){
            this.obrigatorio = obrigatorio;
        }

        private boolean obrigatorio;

        public boolean isObrigatorio() {
            return obrigatorio;
        }
    }

    public void addImagemPasso(ImagemPasso imgPasso){
        if(imagensPasso1 == null){
            imagensPasso1 = new ArrayList<ImagemPasso>();
        }
        imagensPasso1.add(imgPasso);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getEstacionamento() {
        return estacionamento;
    }

    public List<ImagemPasso> getImagensPasso1() {
        return imagensPasso1;
    }

    public void setImagensPasso1(List<ImagemPasso> imagensPasso1) {
        this.imagensPasso1 = imagensPasso1;
    }

    public void setEstacionamento(String estacionamento) {
        this.estacionamento = estacionamento;
    }

    public Local getLocal() {
        return local;
    }

    public String getLocalEvento() {
        StringBuffer sb = new StringBuffer();
        sb.append(cidade).append("\n").append(bairro);
        return sb.toString();
    }

    public String getEnderecoCompleto(boolean showNumeroCasa) {
        StringBuffer sb = new StringBuffer();
        sb.append(logradouro);

        if(showNumeroCasa){
            sb.append(", ").append(numero);
        }

        sb.append(", ").append(bairro);
        sb.append(", ").append(cidade);
        sb.append(", ").append(estado);
        return sb.toString();
    }
}
