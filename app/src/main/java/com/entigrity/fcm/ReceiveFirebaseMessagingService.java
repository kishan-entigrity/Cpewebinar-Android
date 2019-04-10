package com.entigrity.fcm;

import android.content.Context;

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
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = this;


    }


}