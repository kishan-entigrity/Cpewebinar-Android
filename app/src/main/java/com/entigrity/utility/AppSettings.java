package com.entigrity.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.entigrity.R;


public class AppSettings {

    public static SharedPreferences mPrefs;
    public static Editor prefsEditor;


    /**
     * Set user id to preferences
     *
     * @param context
     * @return
     */
    public static void setuserid(Context context, int value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        // prefsEditor.putInt(context.getResources().getString(R.string.login_userid), value);
        prefsEditor.commit();
    }


    /**
     * Get user id from preferences
     *
     * @param context
     * @return
     */
    public static int getuserid(Context context) {
        if (null != context) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            //return mPrefs.getInt(context.getResources().getString(R.string.login_userid), 0);
        }
        /*default val*/
        return 0;
    }


    //login token
    public static void set_login_token(Context context, String login_token) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(context.getResources().getString(R.string.str_token), login_token);
        prefsEditor.commit();
    }

    public static String get_login_token(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(context.getResources().getString(R.string.str_token), "");

    }


    //fcm token


    public static void set_device_token(Context context, String login_token) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(context.getResources().getString(R.string.device_token), login_token);
        prefsEditor.commit();
    }

    public static String get_device_token(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(context.getResources().getString(R.string.device_token), "");

    }


    //device id

    public static void set_device_id(Context context, String device_id) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(context.getResources().getString(R.string.device_id), device_id);
        prefsEditor.commit();
    }

    public static String get_device_id(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(context.getResources().getString(R.string.device_id), "");

    }


    public static void removeFromSharedPreferences(Context mContext, String key) {
        if (mContext != null) {
            mPrefs.edit().remove(key).commit();
        }
    }


    //user profile picture


    public static void set_profile_picture(Context context, String profile_picture) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(context.getResources().getString(R.string.str_profile_picture), profile_picture);
        prefsEditor.commit();
    }

    public static String get_profile_picture(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(context.getResources().getString(R.string.str_profile_picture), "");

    }

    //username

    public static void set_profile_username(Context context, String username) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(context.getResources().getString(R.string.str_profile_username), username);
        prefsEditor.commit();
    }

    public static String get_profile_username(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(context.getResources().getString(R.string.str_profile_username), "");

    }


}