package br.com.chef2share.domain;

import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Passo2 implements Serializable {

    private String id;
    private String tipo; //TODO Esta vindo somente o valueOf do Enum, aqui esta label/value
    private String titulo;
    private String bebida;
    private String menu;
    private String outros;
    private String descricao;
    private Cardapio cardapio;
    private Cozinha cozinha; //TODO esta vindo o objeto com id, aqui esta label/value
    private List<ImagemPasso> imagensPasso2;

    public List<Campo> campos() {
        List<Campo> list = new ArrayList<Campo>();
        list.add(Campo.TITULO);
        list.add(Campo.TIPO);
        list.add(Campo.DESCRICAO);
//        list.add(Campo.MENU);
//        list.add(Campo.BEBIDAS);
//        list.add(Campo.TIPO_COZINHA);
        list.add(Campo.OUTRAS_INFORMACOES);
        return list;
    }

    public void setCardapio(Cardapio cardapio) {
        //só precisa do ID para ter referencia que qual cardapio é
        this.cardapio = new Cardapio();
        this.cardapio.setId(cardapio.getId());

        this.setBebida(cardapio.getBebida());
        this.setTitulo(cardapio.getTitulo());
        this.setTipo(cardapio.getTipo());
        this.setDescricao(cardapio.getDescricao());

        List<ImagemCardapio> imagensCardapio = cardapio.getImagensCardapio();
        for(ImagemCardapio imgCardapio: imagensCardapio){
            ImagemPasso imgPasso = new ImagemPasso();
            imgPasso.setId(imgCardapio.getId());
            imgPasso.setImagem(imgCardapio.getImagem());
            imgPasso.setPrincipal(imgCardapio.isPrincipal());
            imgPasso.setDivulgacao(imgCardapio.isDivulgacao());
            addImagemPasso(imgPasso);
        }

    }

    public enum Campo{
        TITULO(true),
        TIPO(true),
        MENU(true),
        DESCRICAO(true),
        BEBIDAS(false),
        TIPO_COZINHA(false),
        OUTRAS_INFORMACOES(false);

        Campo(boolean obrigatorio){
            this.obrigatorio = obrigatorio;
        }

        private boolean obrigatorio;

        public boolean isObrigatorio() {
            return obrigatorio;
        }

    }

    public Cardapio getCardapio() {
        return cardapio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getBebida() {
        return bebida;
    }

    public void setBebida(String bebida) {
        this.bebida = bebida;
    }

    public String getOutros() {
        return outros;
    }

    public void setOutros(String outros) {
        this.outros = outros;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cozinha getCozinha() {
        return cozinha;
    }

    public void setCozinha(Cozinha cozinha) {
        this.cozinha = cozinha;
    }

    public List<ImagemPasso> getImagensPasso2() {
        return imagensPasso2;
    }

    public void setImagensPasso2(List<ImagemPasso> imagensPasso2) {
        this.imagensPasso2 = imagensPasso2;
    }

    public Imagem getImagemPrincipal(){
        if(imagensPasso2 != null && imagensPasso2.size() > 0){
            for (ImagemPasso i: imagensPasso2) {
                if(i.isPrincipal()){
                    return i.getImagem();
                }
            }
        }
        return null;
    }

    public boolean isInformacoesCompletas() {
        boolean ok = true;
        ok &= StringUtils.isNotEmpty(titulo);
        ok &= StringUtils.isNotEmpty(tipo);
        ok &= StringUtils.isNotEmpty(descricao);
        return ok;
    }

    public void addImagemPasso(ImagemPasso imgPasso){
        if(imagensPasso2 == null){
            imagensPasso2 = new ArrayList<ImagemPasso>();
        }
        imagensPasso2.add(imgPasso);
    }
}
