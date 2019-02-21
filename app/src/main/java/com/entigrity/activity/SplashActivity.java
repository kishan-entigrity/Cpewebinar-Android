package com.entigrity.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.databinding.ActivitySplashBinding;
import com.entigrity.utility.AppSettings;


public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ActivitySplashBinding binding;


    public Context context;

    private static final String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = SplashActivity.this;


        DisplayVersionName();
        Navigation();
    }


    public void DisplayVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            binding.tvbuildnumber.setText(version);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
