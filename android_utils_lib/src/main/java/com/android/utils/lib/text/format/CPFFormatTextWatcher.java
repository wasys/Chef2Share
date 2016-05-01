package com.android.utils.lib.text.format;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.android.utils.lib.utils.AndroidUtils;
/**
 * Classe utilizada para formatar dinamicamente o CPF informado pelo usuario
 * @author Maja
 * @create 07/11/2011
 */
public class CPFFormatTextWatcher extends Activity implements TextWatcher,OnKeyListener {
	
	private final EditText edit;
	private String valor;
	private boolean edicao = true;	
	private boolean init = true;
	/**Posi��o da ultima edi��o, que � onde o cursor deve ficar**/
	private int posicao;
	private String textoAlterado;
	private boolean apagando;
	private Context context;

	public CPFFormatTextWatcher(EditText text, Context context){
		this.edit = text;
		if(text == null) {
			throw new IllegalArgumentException();
		}
		this.context = context;
	}

	@Override
	public void afterTextChanged(Editable s) {	
		// S� executa se for a primeira mudan�a, pois quando usamos o setText() chama este metodo novamente.
		// Isso � necess�rio pois o valor seria formatado novamente
		if(init){
			init = false;
			if(edicao){
				valor = formataCampo(valor);
			}
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					edit.setText(valor);	
					edit.setSelection(posicao > valor.length() ? valor.length() : posicao);
					init = true;
					posicao = 0;
//					if(valor.length() == 14){
//						AndroidUtils.closeVirtualKeyboard(context, edit);
//					}
				}
			});
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		String valor = s.toString();
		boolean end = valor.endsWith(".") || valor.endsWith("-");
		//Quando after == 0, significa que est� apagando
		apagando = after == 0;
		//Quando estiver apagando e chegar nos caracteres inclusos por formata��o n�o pode formatar novamente, 
		//pois ir� incluir o caracter que abacou de ser exclu�do
		if (apagando && start + count == valor.length() && end){
				edicao = false;
		}else{
			edicao = true;
		}

		if(init){
			posicao = start + after;
			textoAlterado = valor.substring(start, start+count);
			//Se estiver incluindo e a posi��o for corresponde a algum caracter da mascara, aumenta a posicao, 
			//pois o caracter que foi inserido deve ficar depois da mascara
			if((posicao == 4 || posicao == 8 || posicao == 12) && (posicao != valor.length()) && !apagando){
				posicao++;
			}
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		valor = s.toString();
		if((textoAlterado.equals(".") || textoAlterado.equals("-")) && apagando){
			//Mudo o cursor para um caracter anterior pois o exclu�do foi um da mascara e eu preciso excluir mais um caracter.
			posicao--;
			String aux1 = valor.substring(0, posicao);
			String aux2 = valor.substring(posicao+1, valor.length());
			valor = aux1 + aux2;
		}
	}

	//012.456.890-23
	private String formataCampo(String valor) {
		String aux1;
		String aux2;
		boolean fim;
		int qtdePto = qtdePto(valor);
		boolean contemTraco = valor.contains("-");
		valor = valor.replace(".", "");
		valor = valor.replace("-", "");
		int length = valor.length();
		for (int i = 1; i <= length; i++) {
			if(i == 3){
				aux1 = valor.substring(0, i);
				aux2 = valor.substring(i, length);
				valor = aux1 + "." + aux2;
				length++;
				fim = valor.length() -1 == posicao;
				//Se n�o tem nenhum ponto no valor inicial e a posi��o n�o esta correta aumenta a posi��o
				if(!(qtdePto > 0) && fim && posicao != 9){
					posicao++;
				}
			}
			if(i == 7){
				aux1 = valor.substring(0, i);
				aux2 = valor.substring(i, length);
				valor = aux1 + "." + aux2;
				length++;
				fim = valor.length() -1 == posicao;
				//Se s� tem um ponto e a posi��o n�o esta correta aumenta a posi��o
				if(qtdePto != 2 && fim && posicao != 9){
					posicao++;
				}
			}
			if(i == 11){
				aux1 = valor.substring(0, i);
				aux2 = valor.substring(i, length);
				valor = aux1 + "-" + aux2;
				length++;
				fim = valor.length() -1 == posicao;
				//S� aumenta a posicao quando n�o tem traco e a altera��o foi feita no final da string e a posi��o n�o esta correta
				if(!contemTraco && fim && posicao != 13){
					posicao++;
				}
			}
		}
		return valor;
	}

	private int qtdePto(String valor) {
		int qtPt = 0;
		for (int i = 0; i < valor.length(); i++) {
			String c = valor.substring(i, i + 1);
			if(c.equals(".")){
				qtPt++;
			}
		}
		return qtPt;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			String key = AndroidUtils.getKeyEventString(keyCode);
			if (key != null) {

				String valor = edit.getText().toString();

				if (valor.length() == 14) {
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