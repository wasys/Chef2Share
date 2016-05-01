package com.android.utils.lib.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * AutoComplete que permite digitar palavras com " " (caracter 'espa�o') e
 * conntinuar com o filtro do autocomplete
 * 
 * private static final String DEFAULT_SEPARATOR = " "; //aqui � definido qual
 * sera o separador entre uma pavra e outra
 * 
 * @author Jonas Baggio
 * @create 10/05/2012
 */
public class AutoCompleteTextViewUtils extends AutoCompleteTextView {

	private static final String DEFAULT_SEPARATOR = " ";
	private String mSeparator = DEFAULT_SEPARATOR;

	public AutoCompleteTextViewUtils(Context context) {
		super(context);
	}

	public AutoCompleteTextViewUtils(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoCompleteTextViewUtils(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 
	 * @return
	 */
	public String getSeparator() {
		return mSeparator;
	}

	/**
	 * 
	 * @param separator
	 */
	public void setSeparator(String separator) {
		mSeparator = separator;
	}

	@Override
	protected void performFiltering(CharSequence text, int keyCode) {
		String newText = text.toString();
		if (newText.indexOf(mSeparator) != -1) {
			int lastIndex = newText.lastIndexOf(mSeparator);
			if (lastIndex != newText.length() - 1) {
				newText = newText.substring(lastIndex + 1).trim();
				if (newText.length() >= getThreshold()) {
					text = newText;
				}
			}
		}
		super.performFiltering(text, keyCode);
	}

	@Override
	protected void replaceText(CharSequence text) {
		String newText = getText().toString();
		if (newText.indexOf(mSeparator) != -1) {
			int lastIndex = newText.lastIndexOf(mSeparator);
			newText = newText.substring(0, lastIndex + 1) + text.toString();
		} else {
			newText = text.toString();
		}
		super.replaceText(newText);
	}
}
