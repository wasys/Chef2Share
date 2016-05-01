package br.com.chef2share.domain;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.MoneyUtils;
import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Passo3 implements Serializable {

    private String id;
    private Forma forma = Forma.PAGO;
    private String tipo; //TODO Esta vindo somente o valueOf do Enum, aqui esta label/value
    private String dataInicio;
    private String dataTermino;
    private String horaInicio;
    private String horaTermino;
    private Double valor;
    private Double valorDuplo;
    private Double valorAntecipado;
    private String dataAntecipado;
    private int minimo;
    private int maximo;
    private List<Avaliacao> avaliacoes;
    private String politicaCancelamento;
    private String politicaNaoComparecimento;

    public String getDataTerminoPorExtenso() {
        Date dateTermino = getDataTerminoDate();
        return DateUtils.toString(dateTermino, "EEEE") + ", " + DateUtils.toString(dateTermino, "d") + " de " + DateUtils.toString(dateTermino, "MMM") + " " + DateUtils.toString(dateTermino, "yyyy") + ", " + getHoraTermino();
    }

    public String getDataInicioFormatadaPasso3() {
        Date dataInicio = getDataInicioDate();
        StringBuffer sb = new StringBuffer(DateUtils.toString(dataInicio));
        if(StringUtils.isNotEmpty(horaInicio)) {
            sb.append(" às ").append(getHoraInicio());
        }

        return sb.toString();
    }
    public String getDataTerminoFormatadaPasso3() {
        Date dataTermino = getDataTerminoDate();
        StringBuffer sb = new StringBuffer(DateUtils.toString(dataTermino));
        if(StringUtils.isNotEmpty(horaTermino)) {
            sb.append(" às ").append(getHoraTermino());
        }

        return sb.toString();
    }

    public String getDataAntecipadaFormatadaPasso3() {
        Date dataAntecipada = getDataAntecipadoDate();
        StringBuffer sb = new StringBuffer(DateUtils.toString(dataAntecipada));
        return sb.toString();
    }

    public enum Campo{
        TIPO(true),
        INICIO(true),
        TERMINO(true),
        VALOR(true),
        FORMA(true),
        NUMERO_MAX_CONVIDADOS(true),
        PROMOCAO_DUAS_PESSOAS(false),
        PROMOCAO_COMPRA_ANTECIPADA(false),
        POLITICA_CANCELAMENTO(false),
        POLITICA_NAO_COMPARECIIMENTO(false);

        Campo(boolean obrigatorio){
            this.obrigatorio = obrigatorio;
        }

        private boolean obrigatorio;

        public boolean isObrigatorio() {
            return obrigatorio;
        }
    }

    public List<Campo> campos() {
        List<Campo> list = new ArrayList<Campo>();
        list.add(Campo.TIPO);
        list.add(Campo.INICIO);
        list.add(Campo.TERMINO);
        list.add(Campo.FORMA);

        if(Forma.PAGO == this.forma) {
            list.add(Campo.VALOR);
        }

        list.add(Campo.NUMERO_MAX_CONVIDADOS);

        if(Forma.PAGO == this.forma) {
            list.add(Campo.PROMOCAO_DUAS_PESSOAS);
            list.add(Campo.PROMOCAO_COMPRA_ANTECIPADA);
        }
        list.add(Campo.POLITICA_CANCELAMENTO);
        list.add(Campo.POLITICA_NAO_COMPARECIIMENTO);
        return list;
    }

    public enum Forma{
        PAGO("Pago"),
        GRATUITO("Gratuito");

        private String desc;

        Forma(String desc){
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return getDesc();
        }
    }

    public Forma getForma() {
        return forma;
    }

    public void setForma(Forma forma) {
        this.forma = forma;
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

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(String dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setValorAntecipado(Double valorAntecipado) {
        this.valorAntecipado = valorAntecipado;
    }

    public void setValorDuplo(Double valorDuplo) {
        this.valorDuplo = valorDuplo;
    }

    public Double getValor() {
        return valor;
    }

    public Double getValorAntecipado() {
        return valorAntecipado;
    }

    public Double getValorDuplo() {
        return valorDuplo;
    }

    public int getMinimo() {
        return minimo;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public String getDataAntecipado() {
        return dataAntecipado;
    }

    public void setDataAntecipado(String dataAntecipado) {
        this.dataAntecipado = dataAntecipado;
    }

    public String getPoliticaCancelamento() {
        return politicaCancelamento;
    }

    public void setPoliticaCancelamento(String politicaCancelamento) {
        this.politicaCancelamento = politicaCancelamento;
    }

    public String getPoliticaNaoComparecimento() {
        return politicaNaoComparecimento;
    }

    public void setPoliticaNaoComparecimento(String politicaNaoComparecimento) {
        this.politicaNaoComparecimento = politicaNaoComparecimento;
    }

    public String getValorFormatado(boolean showMoeda) {
        return MoneyUtils.formatReais(valor, 2, showMoeda);
    }

    public String getValorAntecipadoFormatado() {
        if(valorAntecipado == null) {
            return "";
        }
        return MoneyUtils.formatReais(valorAntecipado, 2, true);
    }
    public String getValorDuploFormatado() {
        if(valorDuplo == null) {
            return "";
        }
        return MoneyUtils.formatReais(valorDuplo, 2, true);
    }

    public String getDataEventoDDMM() {
        StringBuffer sb = new StringBuffer();
        Date date = getDataInicioDate();
        return DateUtils.toString(date, "dd/MM");
    }

    public String getDataEventoE() {
        Date date = getDataInicioDate();
        return DateUtils.toString(date, "E");
    }

    public Date getDataInicioDate() {
        Date date = DateUtils.toDate(dataInicio, DateUtils.DATE_BD);
        return date;
    }
    public Date getDataTerminoDate() {
        Date date = DateUtils.toDate(dataTermino, DateUtils.DATE_BD);
        return date;
    }

    public Date getDataAntecipadoDate() {
        Date date = DateUtils.toDate(dataAntecipado,DateUtils.DATE_BD);
        return date;
    }

    public String getDataPorExtenso() {
        Date dataInicio = getDataInicioDate();
        return DateUtils.toString(dataInicio, "EEEE") + ", " + DateUtils.toString(dataInicio, "d") + " de " + DateUtils.toString(dataInicio, "MMM") + " " + DateUtils.toString(dataInicio, "yyyy") + ", " + getHoraInicio();
    }

    /**
     * Tem que possui valor promocional e não estar vencido
     * @return
     */
    public boolean isPossuiValorAntecipado() {
        return (valorAntecipado != null && valorAntecipado > 0.0) && DateUtils.isMenor(getDataAntecipadoDate(), new Date());
    }

    public boolean isEventoRealizado() {
        return DateUtils.isMaior(new Date(), getDataInicioDate());
    }

    public Date getDataHoraInicioDate(){
        String dataHora = dataInicio + " " + horaInicio;
        return DateUtils.toDate(dataHora, "yyyy-MM-dd HH:mm");
    }
    public Date getDataHoraFimDate(){
        String dataHora = dataTermino+ " " + horaTermino;
        return DateUtils.toDate(dataHora, "yyyy-MM-dd HH:mm");
    }

    public boolean isInformacoesCompletas() {
        boolean ok = true;

        ok &= StringUtils.isNotEmpty(tipo);
        ok &= StringUtils.isNotEmpty(dataInicio);
        ok &= StringUtils.isNotEmpty(dataTermino);

        if(forma == Forma.PAGO) {
            ok &= valor != null && valor > 0;
        }

        ok &= maximo > 0;
        return ok;
    }
}
