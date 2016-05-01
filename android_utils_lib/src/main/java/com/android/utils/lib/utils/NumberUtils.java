package com.android.utils.lib.utils;


public class NumberUtils {

	public static Double getDouble(String s) {
		if(StringUtils.isEmpty(s)) {
			return 0.0;
		}
		try {
			Double d = Double.parseDouble(s);
			return d;
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	public static Long getLong(String s) {
		if(StringUtils.isEmpty(s)) {
			return 0L;
		}
		try {
			Double d = getDouble(s);
			Long l = new Long(d.longValue());
			return l;
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
	
	public static Float getFloat(String s) {
		if(StringUtils.isEmpty(s)) {
			return 0.0F;
		}
		try {
			Double d = getDouble(s);
			Float f = new Float(d.doubleValue());
			return f;
		} catch (NumberFormatException e) {
			return 0.0F;
		}
	}

	public static Integer getInteger(String s) {
		if(StringUtils.isEmpty(s)) {
			return 0;
		}
		try {
			Double d = getDouble(s);
			Integer i = d.intValue();
			return i;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
