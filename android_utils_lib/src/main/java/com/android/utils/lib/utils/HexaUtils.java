package com.android.utils.lib.utils;

public class HexaUtils {

	public static byte[] toBytesFromHexa(String hexa) {
		int i;
		byte bytes[] = new byte[i = hexa.length() / 2];
		for (int j = 0; j <= i - 1; j++) {
			long l = Long.parseLong(hexa.substring(2 * j, 2 * j + 2), 16);
			bytes[j] = (byte) (int) (l & 255L);
		}

		return bytes;
	}

	public static String toHexaString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}
