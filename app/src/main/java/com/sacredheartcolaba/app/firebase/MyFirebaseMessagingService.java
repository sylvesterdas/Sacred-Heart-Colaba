package com.sacredheartcolaba.app.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sacredheartcolaba.app.MainActivity;
import com.sacredheartcolaba.app.R;
import com.sacredheartcolaba.app.extras.Constants;

/**
 * @author Sylvester
 * @since 12/4/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService implements Constants {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "Entire.toString" + remoteMessage.toString());

        Log.d(TAG, "FROM: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Msg Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification(), remoteMessage.getData().get("body"));
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, String redirect) {

        Intent intent = new Intent(this, MainActivity.class);
        int id = 0;
        switch (redirect) {
            case "news":
                intent.putExtra(EXTRA_KEY_NOTIFICATION, EXTRA_VALUE_NEWS);
                id = NOTIFICATION_NEWS_ID;
                break;

            case "events":
                intent.putExtra(EXTRA_KEY_NOTIFICATION, EXTRA_VALUE_EVENTS);
                id = NOTIFICATION_EVENTS_ID;
                break;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilder.build());
    }
}
