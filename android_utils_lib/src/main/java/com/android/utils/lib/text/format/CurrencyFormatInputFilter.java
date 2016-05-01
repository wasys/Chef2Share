package com.android.utils.lib.text.format;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mascara de dinheiro em EditText
 * 
 * @author Jonas Baggio
 * @create 23/11/2013 11:34:04
 */
public class CurrencyFormatInputFilter implements InputFilter {

	private Pattern mPattern = null;
	
	public CurrencyFormatInputFilter(int digitsBeforeZero,int digitsAfterZero) {
	    mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
	}


	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher=mPattern.matcher(dest);       
        if(!matcher.matches())
            return "";
        return null;
    }

}