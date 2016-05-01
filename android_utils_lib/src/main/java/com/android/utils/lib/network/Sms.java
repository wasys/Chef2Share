package com.android.utils.lib.network;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Classe que encapsula a l�gica para enviar e receber um SMS
 * 
 * @author ricardo
 * 
 */
public class Sms {
	private static final String CATEGORIA = "livro";

	//Envia um sms para o numero indicado
	public void enviarSms(Context context, String destino, String mensagem) {
		try {
			SmsManager smsManager = SmsManager.getDefault();

			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);

			smsManager.sendTextMessage(destino, null, mensagem, pIntent, null);

		} catch (Exception e) {
			Log.e(CATEGORIA, "Erro ao enviar o SMS: " + e.getMessage(),e);
		}
	}

	//L� uma mensagem da Intent. A Intent � recebida por um IntentFilter
	//configurado para a a��o "android.provider.Telephony.SMS_RECEIVED"
	public SmsMessage receberMensagem(Intent intent) {
		SmsMessage[] mensagens = getMessagesFromIntent(intent);
		if (mensagens != null) {
			return mensagens[0];
		}
		return null;
	}
	private SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object messages[] = (Object[]) (Object[]) intent.getSerializableExtra("pdus");

		if (messages == null) {
			return null;
		}

		byte pduObjs[][] = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++)
			pduObjs[i] = (byte[]) (byte[]) messages[i];

		byte pdus[][] = new byte[pduObjs.length][];

		int pduCount = pdus.length;

		if (pduCount == 0) {
			return null;
		}

		SmsMessage msgs[] = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);

			String celular = msgs[0].getDisplayOriginatingAddress();
			String mensagem = msgs[0].getDisplayMessageBody();

			Log.d(CATEGORIA, "Sms.Mensagem: " + celular + " -> " + mensagem);
		}

		return msgs;
	}

	public static void sendSms(final Context context, String destinationNumber, String message){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("address",destinationNumber);
        sendIntent.putExtra("sms_body", message);
        sendIntent.setType("vnd.android-dir/mms-sms");
        context.startActivity(sendIntent);
	}
}
