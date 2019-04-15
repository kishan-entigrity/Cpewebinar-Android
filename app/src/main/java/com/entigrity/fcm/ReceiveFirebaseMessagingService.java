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


    }


}