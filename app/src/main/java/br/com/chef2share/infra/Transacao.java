package br.com.chef2share.infra;

import android.app.ProgressDialog;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import br.com.chef2share.domain.Response;

public interface Transacao {

	public void onError(String msgErro);

	/**
	 * UI Thread para atualizar a View
	 */
	public void onSuccess(Response response);

	/**
	 * Thread para executar transacoes
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception ;
}
