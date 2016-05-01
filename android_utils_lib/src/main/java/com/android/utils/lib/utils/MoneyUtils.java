package com.android.utils.lib.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyUtils {
	private static DecimalFormatSymbols dfsPt;
	private static DecimalFormatSymbols dfsEn;

	static {
		dfsPt = new DecimalFormatSymbols(new Locale("pt"));
		dfsPt.setGroupingSeparator('.');
		dfsPt.setDecimalSeparator(',');
		
		dfsEn = new DecimalFormatSymbols(new Locale("en"));
		dfsEn.setGroupingSeparator(',');
		dfsEn.setDecimalSeparator('.');
	}

	public static DecimalFormat getFormat() {
		DecimalFormatSymbols d = new DecimalFormatSymbols();
		return new java.text.DecimalFormat("##,##0.00", d);
	}
	
	public static DecimalFormat getFormatPt0() {
		return new java.text.DecimalFormat("##,##0", dfsPt);
	}
	
	public static DecimalFormat getFormatPt() {
		return new java.text.DecimalFormat("##,##0.00", dfsPt);
	}
	
	public static DecimalFormat getFormatPt3() {
		return new java.text.DecimalFormat("##,##0.000", dfsPt);
	}
	
	public static DecimalFormat getFormatPt4() {
		return new java.text.DecimalFormat("##,##0.0000", dfsPt);
	}
	
	public static DecimalFormat getFormatEn() {
		return new java.text.DecimalFormat("##,##0.00", dfsEn);
	}
	
	public static DecimalFormat getFormatEn0() {
		return new java.text.DecimalFormat("##,##0", dfsEn);
	}
	
	public static DecimalFormat getFormatEn3() {
		return new java.text.DecimalFormat("##,##0.000", dfsEn);
	}
	
	public static DecimalFormat getFormatEn4() {
		return new java.text.DecimalFormat("##,##0.0000", dfsEn);
	}

	public static String format(Double value) {
		String s = getFormat().format(value);
		return s;
	}
	
	public static String format(String value) {
		Double d = getDouble(value);
		String s = format(d);
		return s;
	}

	public static String formatReais(String value) {
		return formatReais(value, true);
	}
	
	public static String formatReais(String value, boolean showMoeda) {
		return formatReais(value, 2, showMoeda);
	}
	
	public static String formatReais(String value, int casas, boolean showMoeda) {
		Double d = getDouble(value);
		String s = formatReais(d,casas,showMoeda);
		return s;
	}
	
	public static String formatReais4(String value, boolean showMoeda) {
		Double d = getDouble(value);
		String s = formatReais(d,4, showMoeda);
		return s;
	}
	
	public static String formatReais3(String value, boolean showMoeda) {
		Double d = getDouble(value);
		String s = formatReais(d,4,showMoeda);
		return s;
	}

	public static String formatDolar(String value) {
		Double d = getDouble(value);
		String s = formatDolar(d);
		return s;
	}
	
	public static String formatDolar3(String value) {
		Double d = getDouble(value);
		String s = formatDolar3(d);
		return s;
	}
	
	public static String formatDolar4(String value) {
		Double d = getDouble(value);
		String s = formatDolar4(d);
		return s;
	}
	
	public static String formatReais(Double value) {
		return formatReais(value, 2, true);
	}
	
	public static String formatReais(Double value, int casas, boolean showMoeda) {
		if(value == null) {
			value = 0.0;
		}
		try {
			String s = "";
			if(casas == 0) {
				s = getFormatPt0().format(value);
			}else if(casas <= 2) {
				s = getFormatPt().format(value);
			} else if(casas == 3) {
				s = getFormatPt3().format(value);
			}  else if(casas == 4) {
				s = getFormatPt4().format(value);
			}
			return showMoeda ? "R$ " + s : s;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar ["+value+"] " + e.getMessage(), e);
		}
	}
	
	public static String formatDolar(Double value, int casas, boolean showMoeda) {
		if(value == null) {
			value = 0.0;
		}
		try {
			String s = "";
			if(casas == 0) {
				s = getFormatEn0().format(value);
			}else if(casas <= 2) {
				s = getFormatEn().format(value);
			} else if(casas == 3) {
				s = getFormatEn3().format(value);
			}  else if(casas == 4) {
				s = getFormatEn4().format(value);
			}
			return showMoeda ? "R$ " + s : s;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar ["+value+"] " + e.getMessage(), e);
		}
	}

	public static String formatDolar(Double value) {
		if(value == null) {
			value = 0.0;
		}
		try {
			String s = getFormatEn().format(value);
			return "US$ " + s;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar ["+value+"] " + e.getMessage(), e);
		}
	}
	
	public static String formatDolar3(Double value) {
		if(value == null) {
			value = 0.0;
		}
		try {
			String s = getFormatEn3().format(value);
			return "US$ " + s;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar ["+value+"] " + e.getMessage(), e);
		}
	}
	
	public static String formatDolar4(Double value) {
		if(value == null) {
			value = 0.0;
		}
		try {
			String s = getFormatEn4().format(value);
			return "US$ " + s;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar ["+value+"] " + e.getMessage(), e);
		}
	}

	public static double getDoubleZeroCasas(String formatedMoney) {
		if(StringUtils.isEmpty(formatedMoney)) {
			return 0;
		}
		String s = formatedMoney;
		// Nao tem casa decimal entao zera tudo
		s = s.replace(".", "");
		s = s.replace(",", "");
		// bota as casas
		s += ".0";

		double d = getDouble(s);
		return d;
	}
	
	public static double getDouble(String formatedMoney) {
		try {
			if(StringUtils.isEmpty(formatedMoney)) {
				return 0;
			}
			if ("0".equals(formatedMoney) || "0.0".equals(formatedMoney)) {
				return 0;
			}
			String s = formatedMoney;

			s = tiraMilhar(s);

			// Troca . e , mas deixa a ultima (para casa decimal)
			int idxPonto = s.indexOf(".");
			int idxVirgula = s.indexOf(",");
			
			if(idxPonto != -1 && idxVirgula != -1) {
				if(idxPonto < idxVirgula) {
					s = s.replace(".", "");
					s = s.replace(",", ".");
				} else {
					s = s.replace(",", "");
				}
			}

			if(idxVirgula != -1) {
				s = s.replace(",", ".");
			}

			s = s.replace("R$", "");
			s = s.replace("US$", "");
			s = s.replace("U$", "");
			s = s.replace("%", "");
			s = s.replace(" ", "");

			double d = Double.parseDouble(s);
			return d;
		} catch (NumberFormatException e) {
		}
		return 0;
	}
	
	public static String tiraMilhar(String s) {
		if(StringUtils.isEmpty(s)) {
			return s;
		}

		int length = s.length();

		// Conta virgulas e ponto para descobrir quem � o milhar
		// Se tem mais de 1 � o milhar, se somente 1 � o decimal
		int countVirgula = 0;
		int countPonto = 0;

		for (int i = length-1; i > 0; i--) {
			char c = s.charAt(i);
			if(c == ',') {
				countVirgula++;
			} else if(c == '.') {
				countPonto++;
			}
		}

		// � milhar
		if(countPonto >= 2) {
			s = StringUtils.replace(s,".", "");
		} else if(countVirgula >= 2) {
			s = StringUtils.replace(s,",", "");
		} else {
			// Nao tem milhar
			return s;
		}

		// Garante que est� ponto no fina para converter pra double
		s = StringUtils.replace(s,",", ".");
		return s;
	}

	public static double getDouble(double number, int casasDecimais)
	{
		String s = String.valueOf(number);

		int idx = s.indexOf(".");
		if(idx != -1) {
			String s2 = s.substring(idx+1);
			if(s2.length() > casasDecimais) {
				s2 = s2.substring(0,casasDecimais);
			}
			
			String s1 = s.substring(0,idx);

			s = s1 + "." + s2;
		}

		Double d = Double.parseDouble(s);
		return d;
	}

	public static String removeFormato(String valor) {
		if(StringUtils.isNotEmpty(valor)){	
			valor = StringUtils.replace(valor, "R$", "");
			valor = StringUtils.replace(valor, "$", "");
			valor = StringUtils.replace(valor, " ", "");
			valor = StringUtils.replace(valor, ",", "");
			valor = StringUtils.replace(valor, ".", "");
		}
		return valor;
	}
	
	public static String toStringDouble(String valorTotal) {
		if(StringUtils.isEmpty(valorTotal)){
			return "0.0";
		}
		Double double1 = NumberUtils.getDouble(valorTotal);
		if(double1 != 0){
			return String.valueOf(double1);
		}
		String valor = StringUtils.replace(valorTotal, " ", "");
		valor = valor.replaceAll(" ", "").replaceAll("\\.", "").replaceAll(",", ".").replaceAll("R|\\$", "");
		return valor;
	}
}
