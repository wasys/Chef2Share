package com.android.utils.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

/**
 * @author ricardo
 *
 */
public class NumericText extends EditText implements OnKeyListener {

	public NumericText(Context context) {
		super(context);
		setOnKeyListener(this);
	}

	public NumericText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnKeyListener(this);
	}

	public int getTextInt(){
		String s = super.getText().toString();
		try {
			if(s != null && s.trim().length() > 0){
				int i = Integer.parseInt(s);
				return i;	
			}
		} catch (NumberFormatException e) {
			Log.e("livro",e.getMessage(),e);
		}
		return 0;
	}

	/**
	 * Called when a key is dispatched to a view. This allows listeners to get a chance to respond before the target view.
	 * 
	 * Return: True if the listener has consumed the event, false otherwise. 
	 * 
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		char ch = (char) keyCode;
		Log.i("livro","Tecla["+keyCode+"]: " + ch);
		switch (keyCode) {
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9:
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_CLEAR:
				// OK
				return false;
		default:
			break;
		}
		return true;
	}
}
