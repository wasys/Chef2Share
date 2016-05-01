/**
 * 
 */
package com.android.utils.lib.text.format;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.StringUtils;


/**
 * Classe que captura os eventos do teclado virtual e do teclado fisico, formatando
 * o valor digitado para o formato moeda (R$ 12.345,67).
 * 
 * @author Jonas Baggio
 * @create Jan 2, 2010
 */
public class CurrencyFormatTextWatcher  extends Activity implements TextWatcher, OnKeyListener{
	protected EditTextListener listener;
	protected EditText edit;
	protected String valor;
	protected int maxLenght;
	
	protected boolean executa = true;

	public CurrencyFormatTextWatcher(EditText e, EditTextListener listener, int lenght ) {
		this.edit = e;
		this.maxLenght = lenght;
		this.listener = listener;
	}
	
	public CurrencyFormatTextWatcher(EditText e, int lenght ) {
		this.edit = e;
		this.maxLenght = lenght;
		this.listener = new EditTextListener() {
			
			@Override
			public void setText(String valor) {
				edit.setText(valor);
				edit.setSelection(valor.length());	
			}
		};
	}

	public CurrencyFormatTextWatcher(EditText e, EditTextListener listener ) {
		this.edit = e;
		this.maxLenght = 12;
		this.listener = listener;
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
//		 Toast.makeText(HSBCApplication.getInstance(), "beforeTextChanged() CharSequence: "+s +" START: "+start +" COUNT: "+count +" AFTER: "+after, Toast.LENGTH_SHORT).show();
				
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		Toast.makeText(HSBCApplication.getInstance(), "onTextChanged() "+" START: "+start +" BEFORE: "+before +" COUNT: "+count, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void afterTextChanged(Editable s) {	
		executa = true;
		valor = s.toString();
		valor = formataCampo(valor);
		 
		runOnUiThread(new Runnable() {	
			@Override
			public void run() {
				while(executa){
					listener.setText(valor);	
					executa = false;
				}			
			}			
		});		
	}
	
	

//	/**
//	 * coloca o valor formatado no campo e posiciona o cursor no final da string
//	 */
//	protected void somarTotal() {
//		edit.setText(valor);
//		edit.setSelection(valor.length());				
//		
//	}
	
	protected String formataCampo(String valor) {
	
		if (StringUtils.isNotEmpty(valor)) {

			if (valor.length() > maxLenght) {
				return valor;
			}

			StringBuffer sb = new StringBuffer(valor);
			sb = applyCustomFormat(sb);
			String s = sb.toString();
			return s;
		} else {
			return valor;
		}
	}
	
	/**
	 * aplica a formatacao do texto de acordo com o tipo
	 * 
	 */
	public StringBuffer applyCustomFormat(StringBuffer text) {

		text = removeCustomFormat(text);

		/*if(forceCentavos && text.length() > 0) {
			//Para adicionar o ,00 em campos teoricamente int
			//exemplo na recarga do celular: "25,00"
			text.append("00");
		}*/
		
		int decimal = 2;

		
		// WAFV CurrencyText - Est· fixo apenas casas decimais 2 e 3
		// Precisa fazer um algoritmo aqui depois
		if (decimal == 2) {
			if(text.length() == 1){
				text.insert(0, "00");
				//pos=4;
			}
			if(text.length() == 2){
				text.insert(0, "0");
				//pos=4;
			}
		} else if(decimal == 3) {
			if(text.length() == 1){
				text.insert(0, "000");
				//pos=5;
			}

			if(text.length() == 2){
				text.insert(0, "00");
				//pos=5;
			}

			if(text.length() == 3){
				text.insert(0, "0");
				//pos=5;
			}
		} else if(decimal == 4) {
			if(text.length() == 1){
				text.insert(0, "000");
				//pos=5;
			}

			if(text.length() == 2){
				text.insert(0, "00");
				//pos=5;
			}

			if(text.length() == 3){
				text.insert(0, "0");
				//pos=5;
			}
			
		} else {
			throw new RuntimeException("Campo suporte somente decimais 2 ou 3");
		}

		if (text.length() > decimal){
			text.insert(text.length()-decimal, ',');
		}

		if(text.length() > decimal+4){
			text.insert(text.length()-(decimal+4), '.');
		}

		if(text.length() > decimal+8){
			text.insert(text.length()-(decimal+8), '.');
		}

		if(text.length() > decimal+12){
			text.insert(text.length()-(decimal+12), '.');
		}

		if(text.length() > decimal+16){
			text.insert(text.length()-(decimal+16), '.');

		}

		if(text.length() > decimal+20){
			text.insert(text.length()-(decimal+20), '.');
		}

		if(text.length() > decimal+24){
			text.insert(text.length()-(decimal+24), '.');
		}

		if(text.length() > decimal+28){
			text.insert(text.length()-(decimal+28), '.');
		}

//		text.insert(0, "R$ ");

		//moveCursor(1);

		return text;
	}
	
	/**
	 * remove a formataÁ„o do texto ficando somente com o texto limpo
	 * ˙til para recolocar a formataÁ„o novamente depois
	 * 
	 */
	private StringBuffer removeCustomFormat(StringBuffer textOriginal) {
		
		String s = textOriginal.toString();
		s = s.replace("R$", "");
		s = s.replace(" ", "");
		//s = s.replace(",00", "");

		StringBuffer bufferCopia = new StringBuffer(s);

		String valor = bufferCopia.toString();

		if(valor.indexOf("0,0") == 0){
			bufferCopia.delete(0, 2);
			//pos=4;
		}

		if(valor.indexOf("0,") == 0){
			bufferCopia.delete(0, 1);
			//pos=4;
		}

		/*if(forceCentavos && valor.indexOf("00") > 0){
			bufferCopia.delete(valor.indexOf("00"), valor.indexOf("00")+2);
			pos=bufferCopia.length()-1;
		}*/

		char[] temp = bufferCopia.toString().toCharArray();

		StringBuffer temp2 = new StringBuffer();

		int size = temp.length;
		for(int i =0;i<size;i++){

			char c = temp[i];
			if(c!=',' && c!='.'){
				temp2.append(c);
			}
		}

		return temp2;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
			
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			String key = AndroidUtils.getKeyEventString(keyCode);
			if (key != null) {
				
				String valor = edit.getText().toString();
				
				if (valor.length() == maxLenght) {
					return true;
				}

				valor += key;
				valor = formataCampo(valor);
				edit.setText(valor);				

				// posiciona o cursor no fim
				edit.setSelection(valor.length());
				
				return true;
			}
		}

		return false;
	}

}