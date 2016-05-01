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
 * Classe utilizada para formatar dinamicamente o CNPJ informado pelo usuario
 * @author Maja
 * @create 13/07/2012
 */
public class CNPJFormatTextWatcher extends Activity implements TextWatcher,OnKeyListener {
	
	private final EditText edit;
	private String valor;
	private boolean init = true;
	private int posicao;
	private String textoAlterado;
	private boolean apagando;
	private Context context;
	private final String barra = "/";
	private final String traco = "-";
	private final String pto = ".";
	private boolean edicao;

	public CNPJFormatTextWatcher(EditText text, Context context){
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
				formataCampo();
			}
			edit.setText(valor);
			edit.setSelection(posicao > valor.length() ? valor.length() : posicao);
			if (valor.length() == 18 && !apagando) {
				AndroidUtils.closeVirtualKeyboard(context, edit);
			}
			init = true;
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
			if((posicao == 3 || posicao == 7 || posicao == 11 || posicao == 16) && (posicao != valor.length()) && !apagando){
				posicao++;
			}
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		valor = s.toString();
		if((textoAlterado.equals(pto) || textoAlterado.equals(traco) || textoAlterado.equals(barra)) && apagando){
			//Mudo o cursor para um caracter anterior pois o exclu�do foi um da mascara e eu preciso excluir mais um caracter.
			posicao--;
			String aux1 = valor.substring(0, posicao);
			String aux2 = valor.substring(posicao+1, valor.length());
			valor = aux1 + aux2;
		}
	}

	//012345678901234567
	//12.345.678/9012-34
	private void formataCampo() {
		String aux1;
		String aux2;
		boolean fim;
		int qtdePto = qtdePto(valor);
		boolean contemTraco = valor.contains("-");
		boolean contemBarra = valor.contains("/");
		valor = valor.replace(".", "");
		valor = valor.replace("-", "");
		valor = valor.replace("/", "");
		int length = valor.length();
		for (int i = 1; i <= length; i++) {
			if(i == 2){
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
			if(i == 6){
				aux1 = valor.substring(0, i);
				aux2 = valor.substring(i, length);
				valor = aux1 + "." + aux2;
				length++;
				fim = valor.length() -1 == posicao;
				//Se s� tem um ponto e a posi��o n�o esta correta aumenta a posi��o
				if(qtdePto != 2 && fim && posicao != 8){
					posicao++;
				}
			}
			if(i == 10){
				aux1 = valor.substring(0, i);
				aux2 = valor.substring(i, length);
				valor = aux1 + "/" + aux2;
				length++;
				fim = valor.length() -1 == posicao;
				//S� aumenta a posicao quando n�o tem traco e a altera��o foi feita no final da string e a posi��o n�o esta correta
				if(!contemBarra && fim && posicao != 12){
					posicao++;
				}
			}
			
			if(i == 15){
				aux1 = valor.substring(0, i);
				aux2 = valor.substring(i, length);
				valor = aux1 + "-" + aux2;
				length++;
				fim = valor.length() -1 == posicao;
				//S� aumenta a posicao quando n�o tem traco e a altera��o foi feita no final da string e a posi��o n�o esta correta
				if(!contemTraco && fim && posicao != 17){
					posicao++;
				}
			}
		}
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

				valor = edit.getText().toString();

				if (valor.length() == 14) {
					return true;
				}

				valor += key;
				formataCampo();
				edit.setText(valor);				

				// posiciona o cursor no fim
				edit.setSelection(valor.length());

				return true;
			}
		}
		return false;
	}
}