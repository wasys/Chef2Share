package com.android.utils.lib.text.format;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.android.utils.lib.utils.AndroidUtils;
/**
 * Classe que captura os eventos do teclado virtual e do teclado fisico, formatando
 * dinamicamente os valores inseridos para data no formato dd/MM/yyyy
 *  
 * @author Jonas Baggio
 * @create Jan 3, 2011
 */
public class DateFormatTextWatcher extends Activity implements TextWatcher,OnKeyListener {
	
	private final EditText edit;
	private String valor;
	private int size;
	
	private boolean edicao = false;	
	private boolean executa = true;
	
	public DateFormatTextWatcher(EditText text){
		this.edit = text;
		if(text == null) {
			throw new IllegalArgumentException();
		}
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
					edit.setText(valor);	
					executa = false;
					edit.setSelection(valor.length());
				}	
			}
		});
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
	
	
	private String formataCampo(String valor) {
		size = valor.length();		
		if(size == 10 ){
			edicao = true;
		}
		if(size == 0){
			edicao = false;
		}
		if(size == 4){
			edicao = false;
		}
		if(size == 6){
			edicao = true;
		}
		
		if (!edicao) {
			valor = valor.replace(" ", "");			

			if (size == 2) {
				valor += "/";
				edicao = true;
			}
			if (size == 5) {
				valor += "/";
				edicao = true;				
			}			
		}
		
		String aux = null;
		
		int contBarras = 0;
		for(char c : valor.toCharArray()){
			if(c == '/'){
				contBarras++;
			}
		}
		
		if(size == 3){
			if(!valor.contains("/")){
				aux = valor.substring(0,2);
				aux+="/";
				aux+=valor.charAt(2);
				valor = aux;
			}			
		}else if(size == 6){
			if(contBarras != 2){
				aux = valor.substring(0,5);
				aux+="/";
				aux+=valor.charAt(5);
				valor = aux;
			}	
		}
		
		return valor;
		
	}

	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			String key = AndroidUtils.getKeyEventString(keyCode);
			if (key != null) {
				
				String valor = edit.getText().toString();
				
				if (valor.length() == 10) {
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