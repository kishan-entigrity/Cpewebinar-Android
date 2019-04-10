package com.entigrity.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.entigrity.model.SaveTopicsSignUpModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;


public class Constant {
    public static boolean developer_mode = true;
    public static String device_type = "a";  //1 for android
    public static String failure_message = "";
    public static String access_token = "";
    public static ArrayList<SaveTopicsSignUpModel> arraylistselected = new ArrayList<SaveTopicsSignUpModel>();
    public static ArrayList<Integer> arraylistselectedvalue = new ArrayList<Integer>();


    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    public static boolean isValidPassword(String password) {
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$").matcher(password).matches();

    }


    public static String GetReturnResponse(Context context, Throwable e) {


        try {
            HttpException error = (HttpException) e;


            try {
                JSONObject jsonObject = new JSONObject(error.response().errorBody().string());

                Constant.Log("object", "+++++" + jsonObject);

                Constant.Log("error", "+++++" + error.response().errorBody().string());

                String success = jsonObject.getString("success");
                failure_message = jsonObject.getString("message");


            } catch (JSONException e2) {
                e2.printStackTrace();
            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return failure_message;


    }


    public static String Trim(String inputvalue) {

        String inputreturn = inputvalue.trim();
        return inputreturn;

    }


    public static String GetDeviceid(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }


    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void Log(String text, String text2) {
        if (developer_mode == true) {
            try {
                Log.e(text, text2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void ShowPopUp(String message, Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setMessage(message);
        // Setting OK Button

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
