package com.android.utils.lib.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Classe auxiliar para criar uma notifica��o
 * 
 * @author ricardo
 * 
 */
public class Notificacao {

	// id utilizado para criar/cancelar a notifica��o
	private final Context context;
	private int icon;
	private Intent intent;

	/**
	 * @param context
	 *            - Context � a actiivty
	 * @param icon
	 *            - �cone para exibir a notifica��o
	 * @param intent
	 *            - Intent para disparar quando o usu�rio selecionar a
	 *            notifica��o
	 */
	public Notificacao(Context context, int icon, Intent intent) {
		this.context = context;
		this.icon = icon;
		this.intent = intent;
	}

	/**
	 * Exibe a notifica��o
	 * 
	 * @param mensagemBarraStatus
	 *            Mensagem que aparece como alerta na barra de status
	 * @param titulo
	 *            T�tulo da notifica��o
	 * @param mensagem
	 *            Mensagem para o corpo da notifica��o
	 * @param id - identificador da notifica��o
	 */
	public void criarNotificacao(CharSequence mensagemBarraStatus, CharSequence titulo, CharSequence mensagem, int id) {

		// Recupera o servi�o do NotificationManager
		NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

		Notification n = new Notification(icon, mensagemBarraStatus, System.currentTimeMillis());

		// PendingIntent para executar a Activity se o usu�rio selecionar a
		// notifica��o
		PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

		// Informa��es
		n.setLatestEventInfo(context, titulo, mensagem, p);

		// id (numero �nico) que identifica esta notifica��o
		nm.notify(id, n);
	}

	public static void cancelar(Context context, int id) {
		NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		nm.cancel(id);
	}
}
