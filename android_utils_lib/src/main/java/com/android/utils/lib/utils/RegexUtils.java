package com.android.utils.lib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://s3.amazonaws.com/slideshare/ssplayer.swf?id=12985182&doc=portfoli
 * 
 * Find: 12985182
 * 
 * String search = RegexUtils.search(embed,"id=(.*?)&","$1");
 * 
 * @author Ricardo Lecheta
 *
 */
public class RegexUtils {

	public static String search(String text,String regex, String response) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			int groupCount = matcher.groupCount();
			for (int i = groupCount; i >= 0; i--) {
				response = response.replace("$" + i, matcher.group(i));
			}
			return response;
		}
		return null;
	}

	public static String search(String text,String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
}
