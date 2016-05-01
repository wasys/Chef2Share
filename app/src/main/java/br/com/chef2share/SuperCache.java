package br.com.chef2share;

import com.android.utils.lib.utils.DateUtils;

import java.util.Date;

import br.com.chef2share.domain.Cadastro;
import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;
import br.com.chef2share.fragment.FragmentPasso;

/**
 * Created by Jonas on 19/12/2015.
 * Responsável por guardar as informações que o usuario selecionou durante o app.
 */
public class SuperCache {
    
    private PesquisaEvento pesquisaEvento;
    private PesquisaEventoQueVou pesquisaEventoQueVou;
    private PesquisaEventoQueOfereco pesquisaEventoQueOfereco;
    private TipoAcesso tipoAcesso = TipoAcesso.USUARIO;
    private int qtdeNovasMsgs;

    /**
     * Informaçoes Locais e Cardapios do usuario
     */
    private Cadastro cadastro;
    private Evento evento;

    public enum TipoAcesso{
        CHEF,
        USUARIO,
    }

    public PesquisaEvento getPesquisaEvento() {
        if(pesquisaEvento == null){
            pesquisaEvento = new PesquisaEvento();
        }
        return pesquisaEvento;
    }

    public PesquisaEventoQueVou getPesquisaEventoQueVou() {
        if(pesquisaEventoQueVou == null){
            pesquisaEventoQueVou = new PesquisaEventoQueVou();
        }
        return pesquisaEventoQueVou;
    }
    public PesquisaEventoQueOfereco getPesquisaEventoQueOfereco() {
        if(pesquisaEventoQueOfereco == null){
            pesquisaEventoQueOfereco = new PesquisaEventoQueOfereco();
        }
        return pesquisaEventoQueOfereco;
    }

    public TipoAcesso getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(TipoAcesso tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    public class PesquisaEvento{
        public String min;
        public String max;
        public TipoEvento tipoEvento;
        public Cozinha cozinha;
        public Valor valorMaximo;
        public String cidade;
        public String estado;
        public String bairro;
        public String enderecoSearch;

        //indica se o o layout de pesquisa esta aberto ou não
        public int btnStringFiltro;
    }

    public class PesquisaEventoQueVou{

//        Date dataHoje = new Date();
//        Date dataFuturo = DateUtils.addDia(dataHoje, 60);

        public String periodoDe; // = DateUtils.toString(dataHoje, "yyyy-MM-dd");
        public String periodoAte; // = DateUtils.toString(dataFuturo, "yyyy-MM-dd");
    }

    public class PesquisaEventoQueOfereco extends PesquisaEventoQueVou{

    }

    public Evento getEvento() {
        if(evento == null){
            evento = new Evento();
        }
        return evento;
    }

    public int getQtdeNovasMsgs() {
        return qtdeNovasMsgs;
    }

    public void setQtdeNovasMsgs(int qtdeNovasMsgs) {
        this.qtdeNovasMsgs = qtdeNovasMsgs;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Cadastro getCadastro() {
        return cadastro;
    }

    public void setCadastro(Cadastro cadastro) {
        this.cadastro = cadastro;
    }
}
