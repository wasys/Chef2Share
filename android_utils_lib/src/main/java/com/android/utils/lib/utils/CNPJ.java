package com.android.utils.lib.utils;

/**
 * 
 * @author Jonas Baggio
 * @create 21/11/2013 09:27:37
 */
public class CNPJ {
	
	public static final int SIZE_COM_MASK = 18;
	public static final int SIZE_SEM_MASK = 14;

	/**
	 * 17.225.737/0001-05
	 * @param cnpj
	 * @return
	 */
	public static String formataCNPJ(String cnpj) {
		try {
			cnpj = StringUtils.leftPad(cnpj, 14, '0');

			return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "."+ cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);
		} catch (Exception e) {
			return cnpj;
		}
	}
}
