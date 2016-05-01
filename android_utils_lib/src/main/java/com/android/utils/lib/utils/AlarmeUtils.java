package com.android.utils.lib.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

/**
 * Facilita utilizar a API de alarmes
 * 
 * @author ricardo
 *
 */
public class AlarmeUtils {

	/**
	 * Agenda a Intent para daqui a 'x' segundos
	 * 
	 * @param context
	 * @param intent
	 * @param segundos
	 */
	public static void agendarBroadcast(Context context, Intent intent, int segundos, boolean acordarCelular) {
		// Intent para disparar o broadcast
		PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		agendar(context, segundos, p, acordarCelular);
	}
	
	/**
	 * Agenda a Intent para a Data especificada
	 * 
	 * @param context
	 * @param intent
	 * @param segundos
	 */
	public static void agendarBroadcast(Context context, Intent intent, Date data, boolean acordarCelular) {
		// Intent para disparar o broadcast
		PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		agendar(context, data, p, acordarCelular);
	}

	/**
	 * Agenda a Intent para daqui a 'x' segundos
	 * 
	 * @param context
	 * @param intent
	 * @param segundos
	 */
	public static void agendarService(Context context, Intent intent, int segundos, boolean acordarCelular) {
		// Intent para disparar o broadcast
		PendingIntent p = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		agendar(context, segundos, p, acordarCelular);
	}
	
	/**
	 * Agenda a Intent para a Data especificada
	 * 
	 * @param context
	 * @param intent
	 * @param segundos
	 */
	public static void agendarService(Context context, Intent intent, Date data, boolean acordarCelular) {
		// Intent para disparar o broadcast
		PendingIntent p = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		agendar(context, data, p, acordarCelular);
	}

	private static void agendar(Context context, int segundos, PendingIntent p, boolean acordarCelular) {
		// Para executar o alarme depois de x segundos a partir de agora
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.MILLISECOND, segundos);

		// Agenda o alarme
		AlarmManager alarme = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		long time = c.getTimeInMillis();
		// RTC_WAKEUP faz o celular acordar se estiver dormindo
		int flag = acordarCelular ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC;
		alarme.set(flag, time, p);
	}

	private static void agendar(Context context, Date data, PendingIntent p, boolean acordarCelular) {
		// Agenda o alarme
		AlarmManager alarme = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		long time = data.getTime();
		// RTC_WAKEUP faz o celular acordar se estiver dormindo
		int flag = acordarCelular ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC;
		alarme.set(flag, time, p);
	}
}