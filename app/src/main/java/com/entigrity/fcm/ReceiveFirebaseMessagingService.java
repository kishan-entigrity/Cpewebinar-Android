package com.entigrity.fcm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.entigrity.utility.Constant;
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

      //  Constant.Log("onmessage", "onmessage" + remoteMessage.getNotification().getBody());
    }


}