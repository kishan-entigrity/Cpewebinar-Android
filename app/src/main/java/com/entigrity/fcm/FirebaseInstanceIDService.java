package com.entigrity.fcm;

import com.entigrity.utility.Constant;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * This will use for generating the device token while app will open
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    public FirebaseInstanceIDService() {

    }


    @Override
    public void onTokenRefresh() {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        AppConfig.deviceToken = deviceToken;


        Constant.Log("device token", "device token" + FirebaseInstanceId.getInstance().getToken());
        // AppSettings.set_device_token(FirebaseInstanceIDService.this, FirebaseInstanceId.getInstance().getToken());

    }
}