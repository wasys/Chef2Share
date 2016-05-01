package com.android.utils.lib.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Classe utilitaria para datas
 * 
 * @author rlecheta
 * 
 */
@SuppressWarnings("deprecation")
public class DateUtils {

	public static final String[] MESES_ABREVIADOS = new String[] { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };

	public static final String[] MESES_COMPLETOS = new String[] { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
			"Outubro", "Novembro", "Dezembro" };

	public static final int[] ULTIMA_DIA_MES = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public static final String DATE = "dd/MM/yyyy";

    public static final String DATE_TIME_BD = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String DATE_TIME_ISO = "yyyy-MM-ddTHH:mm:ssUTC";

    public static final String DATE_BD = "yyyy-MM-dd";

	// nao tem diferen�a entre dia e noite
	public static final String DATE_TIME_AM_PM = "dd/MM/yyyy hh:mm:ss";

	// formato at� 25h
	public static final String DATE_TIME_24h = "dd/MM/yyyy HH:mm:ss";

    public static final String TIME_24h = "HH:mm";

    public static final String DATE_TIME_24h_UTC = "yyyy-MM-dd HH:mm:ss.s";

	private static final String TAG = "DateUtils";

	public static String getDate() {
		return toString(new Date());
	}

	public static String getDate(String pattern) {
		return getDate(new Date(), pattern);
	}

	public static String getDate(Date date, String pattern) {
		return toString(date, pattern);
	}

	public static String toString(Date date, String pattern) {
		if (date == null) {
			return "";
		}

		if (pattern == null) {
			throw new IllegalArgumentException("Null pattern parameter");
		}

		SimpleDateFormat format = new SimpleDateFormat(pattern);

		try {
			return format.format(date);
		} catch (Exception e) {
			Log.e(TAG, "Error to parse the Date: " + date + " using pattern: " + pattern, e);
			return null;
		}
	}

	public static String toString(Date date) {
		return toString(date, DATE);
	}

	public static Date toDate(String date, String pattern) {
		if (date == null) {
			return null;
		}

		if (pattern == null) {
			throw new IllegalArgumentException("Null pattern parameter");
		}

		SimpleDateFormat format = new SimpleDateFormat(pattern);

		try {
			return format.parse(date);
		} catch (ParseException e) {
			Log.e(TAG, "Error to parse the Date: " + date + " using pattern: " + pattern, e);
			return null;
		}
	}

	public static Date toDate(String date) {
		return toDate(date, DATE);
	}

	public static int compareTo(Date dateA, Date dateB) {
		return compareTo(dateA, dateB, "yyyyMMdd");
	}

	/**
	 * Compara duas datas
	 * 
	 * // - : menor (this < other) // 0 : igual (this = other) // + : maior
	 * (this > other)
	 * 
	 * @param otherCalendar
	 * @return
	 */
	public static int compareTo(Date dateA, Date dateB, String pattern) {
		String sA = toString(dateA, pattern);
		String sB = toString(dateB, pattern);

		// - : menor (this < other)
		// 0 : igual (this = other)
		// + : maior (this > other)

		return sA.compareTo(sB);
	}

	public static boolean equals(Date dateA, Date dateB, String pattern) {
		return compareTo(dateA, dateB, pattern) == 0;
	}

	public static boolean equals(Date dateA, Date dateB) {
		return compareTo(dateA, dateB) == 0;
	}

	/**
	 * Retorna o valor do hor�rio minimo para a data de referencia passada. <BR>
	 * <BR>
	 * Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data
	 * retornada por este metodo ser� "30/01/2009 as 00h:00m:00s e 000ms".
	 * 
	 * @param date
	 *            de referencia.
	 * @return {@link Date} que representa o hor�rio minimo para dia
	 *         informado.
	 */
	public static Date lowDateTime(Date date) {
		Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		toOnlyDate(aux); // zera os parametros de hour,min,sec,milisec
		return aux.getTime();
	}

	/**
	 * Retorna o valor do hor�rio maximo para a data de referencia passada. <BR>
	 * <BR>
	 * Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data
	 * retornada por este metodo ser� "30/01/2009 as 23h:59m:59s e 999ms".
	 * 
	 * @param date
	 *            de referencia.
	 * @return {@link Date} que representa o hor�rio maximo para dia
	 *         informado.
	 */
	public static Date highDateTime(Date date) {
		Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		toOnlyDate(aux); // zera os parametros de hour,min,sec,milisec
		aux.add(Calendar.DATE, 1); // vai para o dia seguinte
		aux.add(Calendar.MILLISECOND, -1); // reduz 1 milisegundo
		return aux.getTime();
	}

	/**
	 * Zera todas as referencias de hora, minuto, segundo e milesegundo do
	 * {@link Calendar}.
	 * 
	 * @param date
	 *            a ser modificado.
	 */
	public static void toOnlyDate(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
	}

	public static Date zeraTempo(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		Date d = c.getTime();
		return d;
	}

	public static int getDia(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int dia = c.get(Calendar.DAY_OF_MONTH);
		return dia;
	}

	public static int getMes(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int dia = c.get(Calendar.MONTH) + 1;
		return dia;
	}

	public static int getAno(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int dia = c.get(Calendar.YEAR);
		return dia;
	}
	
	public static String getDiferencaEntreDatas(Date a, Date b){
		StringBuffer sb = new StringBuffer();
		sb.append(getDiferencaHoras(a, b)).append(":");
		sb.append(getDiferencaMinutos(a, b)).append(":");
		sb.append(getDiferencaSegundos(a, b));
		return sb.toString();
	}
	
	public static long getDiferencaMinutos(Date dataInicial, Date dataFinal) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataInicial);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataFinal);

		long diffMillis = c2.getTimeInMillis() - c1.getTimeInMillis();
		long diffMins = diffMillis/(60*1000);
		return diffMins;
	}

	/**
	 * http://www.exampledepot.com/egs/java.util/CompDates.html
	 * 
	 * Retorna a quantidade de horas de diferença entre a data informada e a
	 * data atual
	 * 
	 * @param dataAtual
	 * @param data
	 * @return
	 */
	public static long getDiferencaHoras(Date dataInicial, Date dataFinal) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataInicial);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataFinal);
		long diffMillis = c2.getTimeInMillis() - c1.getTimeInMillis();
		long diffHours = diffMillis / (60 * 60 * 1000);
		return diffHours;
	}
	
	public static long getDiferencaSegundos(Date dataInicial, Date dataFinal) {

		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataInicial);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataFinal);

		long diffMillis = c2.getTimeInMillis() - c1.getTimeInMillis();

		// Get difference in seconds
		 long diffSecs = diffMillis/(1000);

		return diffSecs;
	}
	
	public static long getDiferencaMillis(Date dataInicial, Date dataFinal) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataInicial);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataFinal);

		long diffMillis = c2.getTimeInMillis() - c1.getTimeInMillis();
		return diffMillis;
	}

	/**
	 * http://www.exampledepot.com/egs/java.util/CompDates.html
	 * 
	 * Retorna a quantidade de horas de diferença entre a data informada e a
	 * data atual
	 * 
	 * @param dataAtual
	 * @param data
	 * @return
	 */
	public static long getDiferencaDias(Date dataInicial, Date dataFinal) {

		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataInicial);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataFinal);

		// Determine which is earlier
		// boolean b = c1.after(c2); // false
		// b = c1.before(c2); // true

		// Get difference in milliseconds
		long diffMillis = c2.getTimeInMillis() - c1.getTimeInMillis();

		// Get difference in seconds
		// long diffSecs = diffMillis/(1000);

		// Get difference in minutes
		// long diffMins = diffMillis/(60*1000);

		// Get difference in hours
		// long diffHours = diffMillis/(60*60*1000);

		// Get difference in days
		long diffDays = diffMillis / (24 * 60 * 60 * 1000);

		return diffDays;
	}

	/**
	 * Faz a soma de 1 dia na data especificada, corrigindo o problema com
	 * horário de verão.<br>
	 * 
	 * Se por acaso o calendar do Java (ao somar 1 dia), continuar no mesmo dia
	 * se for horário de verão (ex: horario de verão perde 1 hora, e volta
	 * para as 23h) O algoritmo força a troca de dia
	 * 
	 * @author Ricardo R. Lecheta
	 * @param data
	 * @return
	 * @since v2.0, 14/1/2009
	 */
	public static Date addDia(Date date, int dias) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int dia1 = c.get(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DATE, dias);
		int dia2 = c.get(Calendar.DAY_OF_MONTH);
		if (dia1 == dia2) {
			// Adiciona novamente o dia porque o Horário de Verão não troca
			// de dia
			// Ex: 10/10/2009, fica para 10/10/2009 - 23:00:00
			c.add(Calendar.DATE, dias);
		}

		// zera as horas
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);

		Date data = c.getTime();
		return data;
	}

	public static String getMesDesc(int mes) {
		return MESES_COMPLETOS[mes - 1];
	}

	public static String getMesDescAbrev(int mes) {
		return MESES_ABREVIADOS[mes - 1];
	}

	public static boolean isMaior(Date dateA, Date dateB, String pattern) {
		return compareTo(dateA, dateB, pattern) > 0;
	}

	public static boolean isIgual(Date dateA, Date dateB, String pattern) {
		return compareTo(dateA, dateB, pattern) == 0;
	}

	public static boolean isMenor(Date dateA, Date dateB, String pattern) {
		return compareTo(dateA, dateB, pattern) < 0;
	}

	public static boolean isMaior(Date dateA, Date dateB) {
		return compareTo(dateA, dateB) > 0;
	}

	public static boolean isIgual(Date dateA, Date dateB) {
		return compareTo(dateA, dateB) == 0;
	}

	public static boolean isMenor(Date dateA, Date dateB) {
		return compareTo(dateA, dateB) < 0;
	}

	/**
	 * Retorna o numero de dias uteis no mes que ocorreram antes desta Data
	 * 
	 * @param mes
	 *            Jan = 1
	 * @return
	 */
	public static int getDiasUteisAteDia(Date dateFim) {
		int diaFim = getDia(dateFim);
		int mes = getMes(dateFim);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, mes - 1);
		int dias = 0;
		for (int i = 1; i < diaFim; i++) {
			c.set(Calendar.DAY_OF_MONTH, i);
			if (isDiaSemana(c.getTime())) {
				dias++;
			}

		}
		return dias;
	}

	/**
	 * Retorna o numero de dias uteis no mes que ainda tem depois desta Data
	 * 
	 * @param dataInicio
	 * @return
	 */
	public static int getDiasUteisDepoisDia(Date dataInicio) {
		int diaInicio = getDia(dataInicio);
		int mes = getMes(dataInicio);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, mes - 1);
		int dias = 0;
		int diasMes = ULTIMA_DIA_MES[mes - 1];
		for (int i = diaInicio + 1; i <= diasMes; i++) {
			c.set(Calendar.DAY_OF_MONTH, i);
			if (isDiaSemana(c.getTime())) {
				dias++;
			}

		}
		return dias;
	}

	/**
	 * Retorna o numero de dias uteis no mes
	 * 
	 * @param mes
	 *            Jan = 1
	 * @return
	 */
	public static int getDiasUteis(int mes) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, mes - 1);
		int dias = 0;
		int diasMes = ULTIMA_DIA_MES[mes - 1];
		for (int i = 1; i <= diasMes; i++) {
			c.set(Calendar.DAY_OF_MONTH, i);
			if (isDiaSemana(c.getTime())) {
				dias++;
			}

		}
		return dias;
	}

	/**
	 * É domingo?
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDomingo(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dia = c.get(Calendar.DAY_OF_WEEK);

		boolean b = dia == Calendar.SUNDAY;

		return b;
	}

	/**
	 * É sábado?
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isSabado(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dia = c.get(Calendar.DAY_OF_WEEK);

		boolean b = dia == Calendar.SATURDAY;

		return b;
	}

	/**
	 * Verifica se a data é de 2ª-feira a 6ª-feira
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDiaSemana(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dia = c.get(Calendar.DAY_OF_WEEK);

		switch (dia) {
		case Calendar.MONDAY:
		case Calendar.TUESDAY:
		case Calendar.WEDNESDAY:
		case Calendar.THURSDAY:
		case Calendar.FRIDAY:
			return true;
		}

		return false;
	}

	public static String toStringFromDateString(String dataString, String fromPattern, String toPattern) {
		try {
			Date data1 = toDate(dataString, fromPattern);

			String s = toString(data1, toPattern);

			return s;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return dataString;
		}
	}

	// Data Feriado Motiva��o
	// 1� de janeiro Confraterniza��o Universal social
	// 21 de abril Tiradentes c�vica
	// 1 de maio Dia do Trabalho social
	// 7 de setembro Independ�ncia do Brasil c�vica
	// 12 de outubro Nossa Senhora Aparecida religiosa (cat�lica)
	// 2 de novembro Finados religiosa (cat�lica)
	// 15 de novembro Proclama��o da Rep�blica c�vica
	// 25 de dezembro Natal religiosa (crist�)
	// from http://pt.wikipedia.org/wiki/Feriados_no_Brasil
	private static final Date[] FERIADOS = new Date[]{
		new Date(0, 0, 1),
		new Date(0, 3, 21),
		new Date(0, 4, 1),
		new Date(0, 8, 7),
		new Date(0, 9, 12),
		new Date(0, 10, 2),
		new Date(0, 10, 15),
		new Date(0, 11, 25)
	};

	/**
	 * Retorna se � um feriado fixo
	 * 
	 * @return
	 */
	public static boolean isFeriadoFixo(Date date) {
		Calendar calendar = Calendar.getInstance();
		for (Date feriado : FERIADOS) {
			calendar.setTime(feriado);
			int diaFeriado = calendar.get(Calendar.DAY_OF_MONTH);
			int mesFeriado = calendar.get(Calendar.MONTH);
			calendar.setTime(date);
			int dia = calendar.get(Calendar.DAY_OF_MONTH);
			int mes = calendar.get(Calendar.MONTH);
			if (dia == diaFeriado && mes == mesFeriado) {
				return true;
			}
		}
		return false;
	}
	
	public static String diferencaDehorasEntreDatas(Date dataInicio, Date dataFim) {
		long diferencaHoras = getDiferencaMillis(dataInicio, dataFim);
		Date data = new Date(diferencaHoras);
		StringBuffer sb = new StringBuffer();
		long horas = DateUtils.getDiferencaHoras(dataInicio, dataFim);
		sb.append((horas < 9) ? ("0" + horas) : horas);
		sb.append(":").append(DateUtils.getDate(data, "mm:ss"));
		return sb.toString();
	}

    public static String recuperarDiaSemana(Date data) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(data);

        switch ((cal.get(Calendar.DAY_OF_WEEK))) {
            case 1:
                return "Domingo";
            case 2:
                return "Segunda-Feira";
            case 3:
                return "Terça-Feira";
            case 4:
                return "Quarta-Feira";
            case 5:
                return "Quinta-Feira";
            case 6:
                return "Sexta-Feira";
            case 7:
                return "Sabádo";
        }

        return "";
    }
    public static String recuperarMesExtenso(Date data) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(data);

        switch ((cal.get(Calendar.MONTH) + 1)) {
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
        }

        return "";
    }
    public static String converteDiaSemanaDataeHoraporExtenso(Date date) {
        String data;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        data = recuperarDiaSemana(date);
        data += " ,";
        data += cal.get(Calendar.DAY_OF_MONTH);
        data += " de ";
        data += recuperarMesExtenso(date);
        data += " de ";
        data += cal.get(Calendar.YEAR);
        data += " ás ";
        DateFormat f24h = new SimpleDateFormat("HH:mm");
        data += f24h.format(date);
        return data;
    }
    public static String converteDataporExtenso(Date date) {
        String data = "";
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        data += cal.get(Calendar.DAY_OF_MONTH);
        data += " de ";
        data += recuperarMesExtenso(date);
        data += " de ";
        data += cal.get(Calendar.YEAR);
        return data;
    }
    public static String dateBD(Date date){
        return toString(date,DATE_BD);
    }

    public static int milisegundos(Date date){
        String data = "";
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.MILLISECOND);
    }
    public static Date addHoras(Date date, int hora,int minutos) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, minutos);

        Date data = c.getTime();
        return data;
    }
    public static Date addHoras(Date date, int hora,int minutos,int segundos) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, minutos);
        c.set(Calendar.SECOND, segundos);

        Date data = c.getTime();
        return data;
    }

    public static Date dateMenos1Dia(Date date) {
        Calendar aux = Calendar.getInstance();
        aux.setTime(date);
        toOnlyDate(aux); // zera os parametros de hour,min,sec,milisec
        aux.add(Calendar.DATE, -1); // vai para o dia anterior
        return aux.getTime();
    }
}
