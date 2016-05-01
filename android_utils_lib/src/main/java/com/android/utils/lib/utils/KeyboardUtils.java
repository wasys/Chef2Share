package com.android.utils.lib.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class KeyboardUtils {

	
	/**
	 * Toggle para mostrar o teclado.
	 * Expoe a utiliza��o do metodo toggle, isso n�o � muito recomendado.
	 * 
	 * @author leonardo.otto
	 * @return 
	 */
	public static void showVirtualKeyboardUsingToggle(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}

	}
	/**
	 * Abre o teclado virtual
	 * @return 
	 */
	public static boolean showVirtualKeyboard(Context context, EditText text) {
		// Fecha o teclado virtual
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null) {
			boolean b = imm.showSoftInput(text, 0);
			return b;
		}
		return false;
	}
	
	/**
	 * Fecha o teclado virtual se aberto (view com foque)
	 */
	public static boolean closeVirtualKeyboard(Context context, View view) {
		// Fecha o teclado virtual
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null) {
			boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			return b;
		}
		return false;
	}
	
	/**
	 * Ao tocar na View fecha o Teclado.
	 * 
	 * Util para fechar o layout no touch de um layout
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	public static void closeVirtualKeyBoardOnTouch(final Context context, final View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					IBinder windowToken = view.getWindowToken();
					imm.hideSoftInputFromWindow(windowToken, 0);
				}
			}
		});
	}

	/**
	 * Fecha o teclado ao teclar <enter>
	 * 
	 * @param context
	 * @param t
	 */
	public static void setOnKeyEnterCloseListener(final Context context,final TextView t) {
		if (t != null) {
			t.setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						if (keyCode == KeyEvent.KEYCODE_ENTER) {
							closeVirtualKeyboard(context, t);
							return true;
						}
					}
					return false;
				}
			});
		}		
	}
	
	/**
	 * Fecha o teclado ao teclar <enter>
	 * 
	 * @param context
	 * @param t
	 */
	public static void setOnKeyEnterListener(final Context context,final TextView t, final Runnable runnable) {
		if (t != null) {
			t.setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						if (keyCode == KeyEvent.KEYCODE_ENTER) {
							closeVirtualKeyboard(context, t);
							runnable.run();
							return true;
						}
					}
					return false;
				}
			});
		}		
	}
}
