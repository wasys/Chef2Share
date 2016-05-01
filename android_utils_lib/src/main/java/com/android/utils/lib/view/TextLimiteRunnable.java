package com.android.utils.lib.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * TextWatcher que executa um Runnable, ao atingir o limite do texto: "android:maxLength=4"
 * 
 * @author ricardo
 *
 */
public class TextLimiteRunnable implements TextWatcher {
	private final int maxLength;
	private final TextView text;
	private final Runnable runnable;

	public TextLimiteRunnable(TextView text, int maxLength, Runnable runnable) {
		this.text = text;
		this.maxLength = maxLength;
		this.runnable = runnable;

	}

	public void afterTextChanged(Editable s) {
		if (text.getText().length() == maxLength) {
			runnable.run();
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}
}
