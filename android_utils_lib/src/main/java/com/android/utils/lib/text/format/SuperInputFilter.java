package com.android.utils.lib.text.format;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Jonas Baggio
 * @create 22/01/2014 15:45:11
 */
public class SuperInputFilter implements InputFilter {

	public static final String MASK_TELEFONE = "\\(\\d{3}\\)\\d{3}\\-\\d{2}\\-\\d{2}";  
	
	private Pattern mPattern;

	public SuperInputFilter(final EditText editText, final String pattern) {
		mPattern = Pattern.compile(pattern);
		
		editText.addTextChangedListener(new TextWatcher(){  
            @Override  
            public void afterTextChanged(Editable s) {  
                String value  = s.toString();  
                if(value.matches(pattern))  
                	editText.setTextColor(Color.BLACK);  
                else  
                	editText.setTextColor(Color.RED);  
            }  
	  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  
  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {}  
		});  
	}
	
	@Override
	public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd) {
		String textToCheck = destination.subSequence(0, destinationStart).toString() + source.subSequence(sourceStart, sourceEnd) + destination.subSequence(destinationEnd, destination.length()).toString();

		Matcher matcher = mPattern.matcher(textToCheck);

		// Entered text does not match the pattern
		if (!matcher.matches()) {

			// It does not match partially too
			if (!matcher.hitEnd()) {
				return "";
			}

		}

		return null;
	}

//	@Override
//	public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd) {
//		String textToCheck = destination.subSequence(0, destinationStart).toString() + source.subSequence(sourceStart, sourceEnd) + destination.subSequence(destinationEnd, destination.length()).toString();
//
//		Matcher matcher = mPattern.matcher(textToCheck);
//
//		// Entered text does not match the pattern
//		if (!matcher.matches()) {
//
//			// It does not match partially too
//			if (!matcher.hitEnd()) {
//				return "";
//			}
//
//		}
//
//		return null;
//	}

}
