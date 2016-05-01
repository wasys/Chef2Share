package br.com.chef2share.domain.request;

public class AlterarSenha {

    private String nova;
    private String repeticao;

    public String getNova() {
        return nova;
    }

    public void setNova(String nova) {
        this.nova = nova;
    }

    public String getRepeticao() {
        return repeticao;
    }

    public void setRepeticao(String repeticao) {
        this.repeticao = repeticao;
    }
}
