package br.com.chef2share.service;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.chef2share.R;

/**
 * Created by Jonas on 19/11/2015.
 */
public class GCMMessageHandler extends GcmListenerService {

    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override

    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        createNotification(from, message);
    }

    private void createNotification(String title, String body) {

        Context context = getBaseContext();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                .setContentText(body);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }
}
