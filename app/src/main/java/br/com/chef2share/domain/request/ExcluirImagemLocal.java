package br.com.chef2share.domain.request;

import java.util.List;

import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Imagem;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo1;

public class ExcluirImagemLocal {

    private Imagem imagem;
    private Passo1 passo1;

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public Passo1 getPasso1() {
        return passo1;
    }

    public void setPasso1(Passo1 passo1) {
        this.passo1 = passo1;
    }
}
