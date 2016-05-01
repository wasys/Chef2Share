package com.android.utils.lib.text.format;


import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DataVencimentoTextWatcher extends Activity implements TextWatcher {
	
	private EditText edit;
	private EditText next;
	private String text;
	private Object textoAlterado;
	private boolean apagando;
	private int posicao;
	private boolean init = true;

	public DataVencimentoTextWatcher(EditText text, Context context){
		this.edit = text;
		if(text == null) {
			throw new IllegalArgumentException();
		}
	}
	
	public DataVencimentoTextWatcher(EditText text, EditText next, Context context){
		this.edit = text;
		if(text == null) {
			throw new IllegalArgumentException();
		}
		this.next = next;
	}

	@Override
	public void afterTextChanged(Editable s) {
		if(!init){
			return;
		}
		init = false;
		formatar();
		edit.setText(text);
		edit.setSelection(posicao > text.length() ? text.length() : posicao);
		if (text.length() == 5 && !apagando) {
			if (next != null) {
				next.requestFocus();
			}
//			else{
//				AndroidUtils.closeVirtualKeyboard(context, edit);
//			}
		}
		init = true;

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		String valor = s.toString();
		apagando = after == 0;
		if (init) {
			posicao = start + after;
			textoAlterado = valor.substring(start, start + count);
		}

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		text = s.toString();
		if(textoAlterado.equals("/") && apagando){
			//Mudo o cursor para um caracter anterior pois o exclu�do foi um da mascara e eu preciso excluir mais um caracter.
			posicao--;
			String aux1 = text.substring(0, posicao);
			String aux2 = text.substring(posicao+1, text.length());
			this.text = aux1 + aux2;
		}

	}
	
//	private void formatar(){
//		if(text.contains("/")){
//			posicao--;
//			text = text.replace("/", "");
//		}
//		if(text.length() > 2){
//			String aux1 = text.substring(0, 2);
//			String aux2 = text.substring(2, text.length());
//			text = aux1 + "/" + aux2;
//			posicao++;
//		}
//	}

	private void formatar() {
		boolean contemBarra = text.contains("/");
		text = text.replace("/", "");
		if (text.length() > 2) {
			String aux1 = text.substring(0, 2);
			String aux2 = text.substring(2, text.length());
			text = aux1 + "/" + aux2;
			boolean fim = text.length() - 1 == posicao;
			if (!contemBarra && fim && posicao != 4) {
				posicao++;
			}
		}
	}


}
