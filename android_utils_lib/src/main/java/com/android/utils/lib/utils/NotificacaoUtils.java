package com.android.utils.lib.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.utils.lib.infra.AppUtil;

/**
 * 
 * @author Jonas Baggio
 * @create 04/01/2014 15:31:40
 */
public class NotificacaoUtils {
	
	public static void sendNotification(Context context, int notificationId, int iconNotification, String title, String msg, Class <? extends Activity> clazz, boolean vibrar, String pathSound) {
		sendNotification(context, notificationId, iconNotification, title, msg, clazz, null, vibrar, pathSound);
	}
	
	/**
	 * 
	 * @param context
	 * @param notificationId  (ID da notificação, para remover posteriormente)
	 * @param iconNotification (Icone que vai aparecer na barra de status)
	 * @param title (Titulo da notificação)
	 * @param msg (Mensagem da notificacao)
	 * @param clazz (Activity que será iniciado quando a notificação for tocada)
	 * @param vibrar (vibrar no momento da notificacao)
	 */
	public static void sendNotification(Context context, int notificationId, int iconNotification, String title, String msg, Class <? extends Activity> clazz, Bundle param, boolean vibrar, String pathSound) {
    	AppUtil.log("Mensagem: " + msg);

    	AppUtil.log("Android 4 ? " + AndroidUtils.isAndroid_4_x());
    	Intent intent = new Intent(context, clazz);
    	if(param != null){
    		intent.putExtras(param);
    	}

		if(AndroidUtils.getAPILevel() < 16){
        	Notificacao n = new Notificacao(context, iconNotification, intent);
        	n.criarNotificacao(title, title, msg, notificationId);
        	
        }else{
        	// Prepare intent which is triggered if the
            // notification is selected
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

            // Build notification
            // Actions are just fake
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(title);
            builder.setContentText(msg).setSmallIcon(iconNotification);
            if(StringUtils.isNotEmpty(pathSound)){
            	builder.setSound(Uri.parse(pathSound));
            }
            builder.setContentIntent(pendingIntent).build();
            Notification noti = builder.build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(notificationId, noti);
            if(vibrar){
            	AndroidUtils.vibrar(context, 2000);
            }
        }
    }
	
	public static void removeNotification(Context context, int notificationId){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
	}
}
