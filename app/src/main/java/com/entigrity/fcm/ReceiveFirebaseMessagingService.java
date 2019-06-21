package com.entigrity.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.activity.SplashActivity;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


/**
 * This will use for the receiving data from the notification
 */
public class ReceiveFirebaseMessagingService extends FirebaseMessagingService {

    Context context;
    private String CHANNEL_ID = "myCPE";
    private static final String TAG = ReceiveFirebaseMessagingService.class.getSimpleName();
    private String title, message, webinar_type = "";
    private int flag = 0, webinar_id = 0;

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

            Log.e("remotemessage", "++++++" + remoteMessage.getData());

          // webinar_id = 0;

            //   String click_action = remoteMessage.getNotification().getClickAction();

            if (remoteMessage.getData().size() > 0) {

                System.out.println("+++++ remoteMessage:" + remoteMessage.getData().get("flag"));
                System.out.println("+++++ remoteMessage:" + remoteMessage.getData().get("text"));
                System.out.println("+++++ remoteMessage:" + remoteMessage.getData().get("title"));
                System.out.println("+++++ remoteMessage:" + remoteMessage.getData().get("webinar_type"));
                System.out.println("+++++ remoteMessage:" + remoteMessage.getData().get("webinar_id"));

                flag = Integer.parseInt(remoteMessage.getData().get("flag"));
                message = remoteMessage.getData().get("text");
                title = remoteMessage.getData().get("title");
                webinar_type = remoteMessage.getData().get("webinar_type");
                webinar_id = Integer.parseInt(remoteMessage.getData().get("webinar_id"));

               /* JSONObject data = new JSONObject(remoteMessage.getData());
                try {
                    flag = data.getInt("flag");
                    message = data.getString("text");
                    title = data.getString("title");
                    webinar_type = data.getString("webinar_type");
                    webinar_id = data.getInt("webinar_id");




                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
            Constant.Log("flag", "+++" + webinar_type + "  " + webinar_id);

            if (!AppSettings.get_login_token(ReceiveFirebaseMessagingService.this).isEmpty()) {
                Intent intent = new Intent(this, SplashActivity.class);
                intent.putExtra(getResources().getString(R.string.pass_webinar_type), webinar_type);
                intent.putExtra(getResources().getString(R.string.pass_webinar_id), webinar_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                final int not_nu = generateRandom();

                PendingIntent pendingIntent = PendingIntent.getActivity(this, not_nu, intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.play_icon)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.play_icon))
                        .setContentTitle(title)
                        .setColor(getResources().getColor(R.color.webinar_status))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)// Set the intent that will fire when the user taps the notification
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(not_nu, mBuilder.build());

                createNotificationChannel();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                final int not_nu = generateRandom();

                PendingIntent pendingIntent = PendingIntent.getActivity(this, not_nu, intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.play_icon)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.play_icon))
                        .setContentTitle(title)
                        .setColor(getResources().getColor(R.color.webinar_status))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)// Set the intent that will fire when the user taps the notification
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(not_nu, mBuilder.build());

                createNotificationChannel();
            }


            /*title = remoteMessage.getNotification().getTitle(); //get title
            message = remoteMessage.getNotification().getBody(); //get message
            click_action = remoteMessage.getNotification().getClickAction(); //get click_action

            Log.d("", "Notification Title: " + title);
            Log.d("", "Notification Body: " + message);
            Log.d("", "Notification click_action: " + click_action);

            sendNotification(title, message, click_action);*/



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

   /* private void sendNotification(String title, String messageBody, int flag, int webinar_id, String webinar_type) {

    }*/

    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}