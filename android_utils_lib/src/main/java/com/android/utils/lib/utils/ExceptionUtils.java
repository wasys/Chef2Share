package com.android.utils.lib.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Retornar a stackTrace em formato String
 * 
 * @author rlecheta
 *
 */
public class ExceptionUtils {

	/**
	 * Retorna a stackTrace em formato String
	 * 
	 * @param exception
	 * @return
	 */
	public static String getStackTrace(Throwable exception) {
		try {
			StringWriter  sw = new StringWriter();
			PrintWriter writer = new PrintWriter(sw);
			exception.printStackTrace(writer);
			sw.close();
			writer.close();
			return sw.toString();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return "";
	}

	public static String getMessage(String msg, Throwable e) {
		return msg + ": " + e.getMessage() + " -  " + e.getClass();
	}
}
