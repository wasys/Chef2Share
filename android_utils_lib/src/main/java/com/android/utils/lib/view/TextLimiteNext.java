package com.android.utils.lib.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

/**
 * TextWatcher que pula para o proximo EditText se atingir o limite do texto: "android:maxLength=4"
 * 
 * @author ricardo
 *
 */
public class TextLimiteNext implements TextWatcher{
	protected final int maxLength;
	protected final TextView text;
	protected final View nextView;
	public TextLimiteNext(TextView text,View nextView, int maxLength) {
		this.text = text;
		this.nextView = nextView;
		this.maxLength = maxLength;
		if(text == null){
			throw new IllegalArgumentException("TextLimitePula text is null");
		}
//		if(textNext == null){
//			throw new IllegalArgumentException("TextLimitePula textNext is null");
//		}
	}
	
	/**
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	public void afterTextChanged(Editable s) {
		if(text != null && text.getText().length() == maxLength) {
//			text.requestFocus(View.FOCUS_DOWN);
			if(nextView != null) {
				nextView.requestFocus();
			}
		}
	}
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
}
