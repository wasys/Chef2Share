package com.android.utils.lib.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.android.utils.lib.utils.AndroidUtils;

/**
 * Fecha o teclado virtual ao atingir o limite
 * 
 * @author ricardo
 *
 */
public class TextLimiteClose implements TextWatcher{
	protected final int maxLength;
	protected final TextView text;
	private final Context context;
	public TextLimiteClose(Context context, TextView text,int maxLength) {
		this.context = context;
		this.text = text;
		this.maxLength = maxLength;
		if(text == null){
			throw new IllegalArgumentException("TextLimitePula text is null");
		}
	}
	
	/**
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	public void afterTextChanged(Editable s) {
		if(text != null && text.getText().length() == maxLength) {
			if(text instanceof EditText) {
				AndroidUtils.closeVirtualKeyboard(context, text);
			}
		}
	}
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
}
