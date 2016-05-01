package com.android.utils.lib.cript;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Gerar hashs MD5, SHA-1 e SHA-256
 * 
 * @author ricardo.lecheta.ext
 * 
 */
public class CriptHash {

	public static byte[] toMD5(String s) {
		return hash(s, "MD5");
	}
	
	/**
	 * Gera um "md5 checksum" em Hexa
	 * 
	 * @param s
	 * @return
	 */
	public static String toShai1tring(String s) {
		byte[] bytes = hash(s, "SHA-1");
		String hexa = toHexa(bytes);
		return hexa;
	}
	
	/**
	 * Gera um "md5 checksum" em Hexa
	 * 
	 * @param s
	 * @return
	 */
	public static String toMD5String(String s) {
		byte[] bytes = hash(s, "MD5");
		String hexa = toHexa(bytes);
		return hexa;
	}

	private static byte[] hash(String s, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(s.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	private static String toHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}
}
