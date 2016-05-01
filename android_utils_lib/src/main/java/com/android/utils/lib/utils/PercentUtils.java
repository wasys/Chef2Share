package com.android.utils.lib.utils;

import java.text.DecimalFormat;

public class PercentUtils {
	
	public static String formatPt4(String value) {
		Double d = MoneyUtils.getDouble(value);
		String s = formatReais4(d);
		return s;
	}

	public static String formatPt(String value) {
		Double d = MoneyUtils.getDouble(value);
		String s = formatPt(d);
		return s;
	}

	public static String formatEn(String value) {
		Double d = MoneyUtils.getDouble(value);
		String s = formatEn(d);
		return s;
	}

	public static String format(String value) {
		Double d = MoneyUtils.getDouble(value);
		String s = format(d);
		return s;
	}

	public static String format(Double value) {
		if(value == null || value == 0) {
			return "0,00 %";
		}
		try {
			String s = MoneyUtils.getFormat().format(value);
			return s + " %";
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar ["+value+"] " + e.getMessage(), e);
		}
	}

	public static String formatPt(Double value) {
		if(value == null || value == 0) {
			return "0,00 %";
		}
		DecimalFormat format = MoneyUtils.getFormatPt();
		return toString(format, value);
	}
	
	public static String formatReais4(Double value) {
		if(value == null || value == 0) {
			return "0,0000 %";
		}
		DecimalFormat format = MoneyUtils.getFormatPt4();
		return toString(format, value);
	}

	public static String formatEn(Double value) {
		if(value == null || value == 0) {
			return "0,00 %";
		}
		DecimalFormat format = MoneyUtils.getFormatEn();
		return toString(format, value);
	}
	
	private static String toString(DecimalFormat format,Double value) {
		try {
			String s = format.format(value);
			return s + " %";
		} catch (Exception e) {
			throw new RuntimeException("Erro ao formatar ["+value+"] " + e.getMessage(), e);
		}
	}
}
