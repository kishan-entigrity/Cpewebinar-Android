package com.entigrity.fcm;

import android.content.Context;
import android.util.Log;

import com.entigrity.utility.AppSettings;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * This will use for the receiving data from the notification
 */
public class ReceiveFirebaseMessagingService extends FirebaseMessagingService {

    Context context;

    public ReceiveFirebaseMessagingService() {
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        String deviceToken = token;
        AppSettings.set_device_token(ReceiveFirebaseMessagingService.this, deviceToken);
        Log.e("Refreshed token:", deviceToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = this;

        if (remoteMessage.getData() != null) {

/*
            Log.e("remotemessage", "++++++" + remoteMessage.getData());


            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSmallIcon(R.mipmap.play_icon)
                    .build();
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(0, notification);*/

         /*   String title = remoteMessage.getData().get(“title”);
            String body = remoteMessage.getData().get(“body”);
            String objectId = remoteMessage.getData().get(object_id);
            String objectType = remoteMessage.getData().get(objectType”);
            */

            /*Intent intent = new Intent(this, SplashActivity.class);

            intent.putExtra("flag", remoteMessage.getData().get("flag"));
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.play_icon)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
//                .setGroup("black_race")
//                .setGroupSummary(true)
                    .getNotification();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification.priority = Notification.PRIORITY_MAX;
            }

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, notification);
*/
        }


    }


}