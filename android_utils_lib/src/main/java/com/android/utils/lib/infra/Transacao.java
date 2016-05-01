package com.android.utils.lib.infra;

/**
 * Interface para as transacoes
 * 
 * @author ricardo
 *
 */
public interface Transacao {

	/**
	 * UI Thread para atualizar a View
	 */
	void updateView();
	
	/**
	 * Thread para executar transacoes
	 * 
	 * @throws Exception
	 */
	void execute() throws Exception ;

}
