package com.android.utils.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

public class EmailUtils {
	
	private static final String TAG = "EmailUtils";

	/**
	 * Envia um email por intent.
	 * 
	 * Verificar classe "Mailto" tamb�m
	 * 
	 * @param context
	 * @param email
	 */
	public static void mailToWithSubject(Context context, String subject, String msg, boolean html) {
		 Intent sendIntent = new Intent(Intent.ACTION_SEND);
         sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
         sendIntent.putExtra(Intent.EXTRA_TEXT, html ? Html.fromHtml(msg) : msg);
         sendIntent.setType(html ? "text/html" : "message/rfc822");
         context.startActivity(Intent.createChooser(sendIntent, "Not�cia"));
	}
	
	/**
	 * Envia um email por intent.
	 * 
	 * Verificar classe "Mailto" tamb�m
	 * 
	 * @param context
	 * @param email
	 */
	public static void mailTo(Context context, String email) {
		if(StringUtils.isNotEmpty(email)) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("plain/text");//message/rfc822
			intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});  
			context.startActivity(intent);
		}
	}

	public static boolean validarEmail(String email) {
		if (email == null || email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(" ") != -1) {
			return false;
		}
		int emailLenght = email.length();
		int atPosition = email.indexOf("@");

		String beforeAt = email.substring(0, atPosition);
		String afterAt = email.substring(atPosition + 1, emailLenght);

		if (beforeAt.length() == 0 || afterAt.length() == 0) {
			Log.i(TAG,"only @ is");
			return false;
		}

		//CHECK for .(dot) before @(at) = aaa.@domain.com
		if (email.charAt(atPosition - 1) == '.') {
			Log.i(TAG,".(dot) before @(at)");
			return false;
		}

		//CHECK for .(dot) before @(at) = aaa@.domain.com
		if (email.charAt(atPosition + 1) == '.') {
			Log.i(TAG,"@.");
			return false;
		}

		//CHECK for .(dot) = email@domaincom
		if (afterAt.indexOf(".") == -1) {
			Log.i(TAG,"no dot(.)");
			return false;
		}

		//CHECK for ..(2 dots) = email@domain..com
		char dotCh = 0;
		for (int i = 0; i < afterAt.length(); i++) {
			char ch = afterAt.charAt(i);
			if((ch == 0x2e) && (ch == dotCh)){
				Log.i(TAG,"find .. (2 dots) in @>");
				return false;
			}
			dotCh = ch;
		}

		//CHECK for double '@' = example@@domain.com
		if (afterAt.indexOf("@") != -1) {
			Log.i(TAG,"find 2 @");
			return false;
		}
		//check domain name 2-5 chars
		int ind = 0;
		do {
			int newInd = afterAt.indexOf(".", ind + 1);

			if (newInd == ind || newInd == -1) {
				String prefix = afterAt.substring(ind + 1);
				if (prefix.length() > 1 && prefix.length() < 6) {
					break;
				} else {
					Log.i(TAG,"prefix not 2-5 chars");
					return false;
				}
			} else {
				ind = newInd;
			}
		} while (true);

		//CHECK for valid chars[a-z][A-Z][0-9][. - _]
		//CHECK for ..(2 dots)
		dotCh = 0;
		for (int i = 0; i < beforeAt.length(); i++) {
			char ch = beforeAt.charAt(i);
			if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a) ||
					(ch == 0x2e) || (ch == 0x2d) || (ch == 0x5f))) {
				Log.i(TAG,"not valid chars");
				return false;
			}
			if((ch == 0x2e) && (ch == dotCh)){
				Log.i(TAG,"find .. (2 dots)");
				return false;
			}
			dotCh = ch;
		}
		return true;
	}
}
