package com.android.utils.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.utils.lib.view.TextLimiteClose;
import com.android.utils.lib.view.TextLimiteNext;
import com.android.utils.lib.view.TextLimiteRunnable;

import java.util.Locale;

/**
 * @author ricardo
 * 
 */
public class TextUtils {

	/**
	 * Configura o Locale em runtime
	 * 
	 * @param res
	 *            Resources
	 * @param language
	 *            "pt" or "en"
	 */
	public static void setLocale(Resources res, String language) {
		Locale locale = Locale.getDefault();
		if ("pt".equals(language)) {
			locale = new Locale("pt", "BR");
		} else if ("en".equals(language)) {
			locale = Locale.ENGLISH;
		}

		setLocale(res, locale);
	}

	/**
	 * Coloca um estilo no texto de um text.
	 * Sempre � colocado na primeira ocorrencia encontrada.
	 * 
	 * @param c
	 * @param tv
	 * @param word
	 * @param style
	 * 
	 * @author leonardo.otto
	 */
	public static void textViewStyle(TextView tv, String word, CharacterStyle style) {
		String text = tv.getText().toString();
		if(!text.contains(word)){
			return;
		}
		// Deve-se utilizar o getText() pois o retorno j� pode estar estilizado.
		SpannableStringBuilder ssb = new SpannableStringBuilder(tv.getText());
		ssb.setSpan(style, text.indexOf(word), text.indexOf(word) + word.length(), 0);
		tv.setText(ssb);
	}
	
	/**
	 * Concatena os valores dos campos e retorna.
	 * Preservando a formata��o dos campos de texto, se existirem
	 * 
	 * @author leonardo.otto
	 */
	public static CharSequence concatText(TextView... texts){
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		for (TextView textView : texts) {
			ssb.append(textView.getText());
		}
		return ssb;
	}
	/**
	 * Remove o texto de cada um dos campos
	 * 
	 * @author leonardo.otto
	 */
	public static void clean(TextView... texts){
		for (TextView textView : texts) {
			textView.setText("");
		}
	}
	

	/**
	 * Configura o Locale em runtime
	 * 
	 * Locale locale = new Locale("pt","BR")
	 * 
	 * Locale.GERMANY
	 */
	public static void setLocale(Resources res, Locale locale) {
		// Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = locale;
		res.updateConfiguration(conf, dm);
	}

	public static int getInt(TextView text) {
		String s = text.getText().toString();
		if (s != null && s.trim().length() > 0) {
			try {
				int n = Integer.parseInt(s);
				return n;
			} catch (NumberFormatException e) {
				Log.e("livro", "TextUtils.getInt(" + s + ")" + e.getMessage());
				return 0;
			}
		}
		return 0;
	}

	public static boolean isInteger(String s) {
		if (s != null && s.trim().length() > 0) {
			try {
				Integer.parseInt(s);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isInteger(TextView text) {
		String s = text.getText().toString();
		if (s != null && s.trim().length() > 0) {
			try {
				Integer.parseInt(s);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isDouble(TextView text) {
		String s = text.getText().toString();
		if (s != null && s.trim().length() > 0) {
			try {
				Double.parseDouble(s);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static String getText(View view) {
		TextView text = (TextView) view;

		String s = text.getText().toString();

		return s;
	}

	public static String getText(Activity activity, int id) {
		TextView text = (TextView) activity.findViewById(id);

		String s = text.getText().toString();

		return s;
	}

	public static boolean getBoolean(Activity activity, int id) {
		CheckBox c = (CheckBox) activity.findViewById(id);

		boolean b = c.isChecked();

		return b;
	}

	public static void setText(Activity activity, String text, int id) {
		TextView view = (TextView) activity.findViewById(id);

		view.setText(text);
	}

	public static void setTextStyle(Context context, TextView text, int resStyle) {
		text.setTextAppearance(context, resStyle);
	}

	/**
	 * Troca de um Text para outro ao digitar x caracteres
	 */
	public static void setTextLimitNext(Context context, TextView text, TextView next, int maxLength) {
		text.addTextChangedListener(new TextLimiteNext(text, next, maxLength));
	}

	public static void setTextLimitNext(Activity context, int text, int next, int maxLength) {
		TextView textT = (TextView) context.findViewById(text);
		View nextT = context.findViewById(next);
		textT.addTextChangedListener(new TextLimiteNext(textT, nextT, maxLength));
	}

	public static void setTextLimitRunnable(Activity context, int text, int maxLength, Runnable runnable) {
		TextView textT = (TextView) context.findViewById(text);
		textT.addTextChangedListener(new TextLimiteRunnable(textT, maxLength, runnable));
	}

	public static void setTextLimitClose(Activity context, int text, int maxLength) {
		TextView textT = (TextView) context.findViewById(text);
		textT.addTextChangedListener(new TextLimiteClose(context, textT, maxLength));
	}

	public static void setTextLimitClose(Activity context, View view, int text, int maxLength) {
		TextView textT = (TextView) view.findViewById(text);
		textT.addTextChangedListener(new TextLimiteClose(context, textT, maxLength));
	}

	public static void setTextLimitClose(Context context, TextView text, int maxLength) {
		text.addTextChangedListener(new TextLimiteClose(context, text, maxLength));
	}

	public static void underline(Context context, TextView text) {
		SpannableString underlined = new SpannableString(text.getText());
		underlined.setSpan(new UnderlineSpan(), 0, underlined.length(), 0);
		text.setText(underlined);
	}
	
	public static float stringWidth(TextView text) {
		String s = text.getText().toString();
		float measureText = text.getPaint().measureText(s);
		return measureText;
	}
	
	public static float stringHeight(TextView text) {
		float textSize = text.getPaint().getTextSize();
		return textSize;
	}
	
	public static void setOnBackspaceFocus(Activity act,int text1,int text2){
		TextView e1 = (TextView) act.findViewById(text1);
		final TextView e2 = (TextView) act.findViewById(text2);
		
		e1.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_DEL){
					TextView tv = (TextView) view;
					if(tv.getText().toString().isEmpty()){
						e2.requestFocus();
					}
				}
				return false;
			}
		});
	}
		
}
