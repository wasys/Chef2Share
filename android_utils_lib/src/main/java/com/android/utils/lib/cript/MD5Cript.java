package com.android.utils.lib.cript;

import android.util.Log;

import com.android.utils.lib.utils.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe utilit�ria para gerar hash no padr�o MD5 (apenas cria, n�o desfaz)
 * 
 * EX: android / c31b32364ce19ca8fcd150a417ecce58
 * 
 * @author Jonas Baggio
 * @create 08/12/2011
 */
public class MD5Cript {
	
	private final static String TAG = "md5cript";

	public static String cript(String value){
		if(StringUtils.isEmpty(value)){
			throw new IllegalArgumentException("Nenhum valor informado para gerar MD5");
		}		
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(value.getBytes());
	 
	        byte byteData[] = md.digest();

	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }

	       value =  sb.toString();
		}catch(NoSuchAlgorithmException e){
			Log.i(TAG, "MD5Cript.NoSuchAlgorithmException [ "+e.getMessage()+" ]");
		}
		return value;
	}
	
	/**
	 * � informado um valor String original, e a MD5 que queremos validar.
	 * Se essa String gerar o mesmo MD5 informado, o MD5 esta validado
	 * @param senhaString
	 * @param md5
	 * @return
	 */
	public static boolean isMD5valido(String senhaString, String md5){
		if(StringUtils.isEmpty(senhaString) || StringUtils.isEmpty(md5)){
			throw new IllegalArgumentException("Nenhum valor informado para comparar MD5");
		}		
		String newMd5 = cript(senhaString);
		
		if(md5.equals(newMd5)){
			return true;
		}		
		return false;
	}	
}
