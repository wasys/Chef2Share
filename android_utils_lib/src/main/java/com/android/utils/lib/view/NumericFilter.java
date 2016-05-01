package com.android.utils.lib.view;

import android.text.InputFilter;
import android.text.Spanned;

import com.android.utils.lib.utils.StringUtils;

/**
 * InputFilter que aceita apenas numeros
 * 
 * � bom para usar em conjunto com um @style:
 * 
 * <style name="numero">
		<item name="android:singleLine">true</item>
		<item name="android:numeric">integer</item>
		<item name="android:phoneNumber">true</item>
	</style>
 * 
 * @author ricardo
 *
 */
public class NumericFilter implements InputFilter {
	@Override
 	public CharSequence filter(CharSequence  source, int start, int end, Spanned  dest, int dstart, int dend) {
		String s = source.toString();
		if(StringUtils.isInteger(s)) {
			return null;
		}
		return "";
	}
}
