package com.entigrity.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.databinding.ActivitySplashBinding;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ActivitySplashBinding binding;

    public ArrayList<String> arrayemployee = new ArrayList();
    public ArrayList<String> arrayListclient = new ArrayList();


    public Context context;
    private static final String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = SplashActivity.this;

      /*  arrayemployee.add("chirag");
        arrayemployee.add("deepak");
        arrayemployee.add("tanuj");

        arrayListclient.add("chirag");
        arrayListclient.add("deepak");


        for (int i = 0; i < arrayemployee.size(); i++) {
            for (int j = 0; j < arrayListclient.size(); j++) {
                if (arrayemployee.get(i).contains(arrayListclient.get(j))) {
                    Constant.Log("matched", "++++" + arrayemployee.get(i));
                }
            }

        }*/


        Navigation();

    }

    public void Navigation() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                Constant.Log("token", "token" + AppSettings.get_login_token(context));


                if (!AppSettings.get_login_token(context).isEmpty()) {

                    Intent i = new Intent(context, MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent i = new Intent(context, PreLoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);

    }
}
