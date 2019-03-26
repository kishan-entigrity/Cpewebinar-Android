package com.entigrity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.activity.LoginActivity;
import com.entigrity.fragments.AccountFragment;
import com.entigrity.fragments.CompanyFragment;
import com.entigrity.fragments.ContactUsFragment;
import com.entigrity.fragments.FavoritesFragment;
import com.entigrity.fragments.InstructorFragment;
import com.entigrity.fragments.MyCreditsFragment;
import com.entigrity.fragments.MyFavoriteScreenFragment;
import com.entigrity.fragments.UserDashBoardFragment;
import com.entigrity.fragments.ViewProfileFragment;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    UserDashBoardFragment userDashBoardFragment;
    ViewProfileFragment viewProfileFragment;
    AccountFragment accountFragment;
    MyCreditsFragment myCreditsFragment;
    MyFavoriteScreenFragment myFavoriteScreenFragment;


    FavoritesFragment favoritesFragment;
    ContactUsFragment contactUsFragment;
    InstructorFragment instructorFragment;
    CompanyFragment companyFragment;


    public Context context;
    private APIService mAPIService;
    public TextView tv_popup_ok, tv_popup_cancel;
    public TextView tv_popup_msg, tv_popup_submit;
    ProgressDialog progressDialog;
    private static MainActivity instance;
    public RelativeLayout rel_top_bottom;
    public ImageView iv_mycredit, iv_mywebinar, iv_home, iv_myfavorite, iv_account;
    private static final String TAG = MainActivity.class.getName();


    public boolean isclickhome = false;
    public int setselectedtab = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = MainActivity.this;


        rel_top_bottom = (RelativeLayout) findViewById(R.id.rel_top_bottom);
        iv_mycredit = (ImageView) findViewById(R.id.iv_mycredit);
        iv_mywebinar = (ImageView) findViewById(R.id.iv_mywebinar);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_myfavorite = (ImageView) findViewById(R.id.iv_myfavorite);
        iv_account = (ImageView) findViewById(R.id.iv_account);


        context = MainActivity.this;
        mAPIService = ApiUtils.getAPIService();


        iv_mycredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclickhome = false;
                setselectedtab = 0;

                SetImageBackground(0);
                //SetDefault();
                myCreditsFragment = new MyCreditsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myCreditsFragment, getResources()
                        .getString(R.string.mycreditfragment)).addToBackStack(getResources().getString(R.string.add_to_back_stack)).commit();


            }
        });

        iv_mywebinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclickhome = false;
                setselectedtab = 1;
                SetImageBackground(1);
                SetDefault();


            }
        });


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselectedtab = 0;

                if (!isclickhome) {
                    isclickhome = true;
                    SetImageBackground(2);
                    SetDefault();
                }


            }
        });

        iv_myfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselectedtab = 2;
                isclickhome = false;

                SetImageBackground(3);
                myFavoriteScreenFragment = new MyFavoriteScreenFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFavoriteScreenFragment, getResources()
                        .getString(R.string.myfavoritescreenfragment)).addToBackStack(getResources().getString(R.string.add_to_back_stack)).commit();


            }
        });

        iv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselectedtab = 0;
                isclickhome = false;

                SetImageBackground(4);
                //viewProfileFragment = new ViewProfileFragment();

                accountFragment = new AccountFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, accountFragment, getResources()
                        .getString(R.string.accountfragment)).addToBackStack(getResources().getString(R.string.add_to_back_stack)).commit();


            }
        });


        SetDefault();


    }


    public void SetCreditScreen() {
        isclickhome = false;
        setselectedtab = 0;

        SetImageBackground(0);
        //SetDefault();
        myCreditsFragment = new MyCreditsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myCreditsFragment, getResources()
                .getString(R.string.mycreditfragment)).addToBackStack(getResources().getString(R.string.add_to_back_stack)).commit();

    }


    public void SetImageBackground(int position) {

        if (position == 0) {
            iv_mycredit.setImageResource(R.mipmap.footer_mycredits_select);
            iv_mywebinar.setImageResource(R.mipmap.footer_mywebinars);
            iv_myfavorite.setImageResource(R.mipmap.footer_favorites);
            iv_account.setImageResource(R.mipmap.footer_account);
        } else if (position == 1) {
            iv_mycredit.setImageResource(R.mipmap.footer_mycredits);
            iv_mywebinar.setImageResource(R.mipmap.footer_mywebinars_select);
            iv_myfavorite.setImageResource(R.mipmap.footer_favorites);
            iv_account.setImageResource(R.mipmap.footer_account);
        } else if (position == 2) {
            iv_mycredit.setImageResource(R.mipmap.footer_mycredits);
            iv_mywebinar.setImageResource(R.mipmap.footer_mywebinars);
            iv_myfavorite.setImageResource(R.mipmap.footer_favorites);
            iv_account.setImageResource(R.mipmap.footer_account);
        } else if (position == 3) {
            iv_mycredit.setImageResource(R.mipmap.footer_mycredits);
            iv_mywebinar.setImageResource(R.mipmap.footer_mywebinars);
            iv_myfavorite.setImageResource(R.mipmap.footer_favorites_select);
            iv_account.setImageResource(R.mipmap.footer_account);
        } else if (position == 4) {
            iv_mycredit.setImageResource(R.mipmap.footer_mycredits);
            iv_mywebinar.setImageResource(R.mipmap.footer_mywebinars);
            iv_myfavorite.setImageResource(R.mipmap.footer_favorites);
            iv_account.setImageResource(R.mipmap.footer_account_select);
        }


    }


    public void ShowPopUp(String message, final Context context) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                context).create();
        alertDialog.setMessage(message);
        // Setting OK Button
        alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();

                AppSettings.removeFromSharedPreferences(context, getResources().getString(R.string.str_token));


                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static MainActivity getInstance() {
        return instance;

    }


    public void SetDefault() {
        userDashBoardFragment = new UserDashBoardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, userDashBoardFragment, getResources().getString(R.string.userdashBoard_fragment))
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void Logout(String Authorization, String device_id, String device_token, String device_type) {

        // RxJava
        mAPIService.logout(getResources().getString(R.string.bearer) + Authorization, device_id
                , device_token, device_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LogoutModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //handle failure response
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);

                    }


                    @Override
                    public void onNext(LogoutModel logoutModel) {
                        if (logoutModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            ShowPopUp(logoutModel.getMessage(), context);
                        } else {
                            if (logoutModel.getPayload().getAccessToken() != null && !logoutModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, logoutModel.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    Logout(AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }


                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(logoutModel.getMessage(), context);
                            }

                        }


                    }
                });

    }


    public void LogOutPoPUp() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.logout_text));


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                dialog.cancel();

                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));

                if (Constant.isNetworkAvailable(context)) {
                    Logout(AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
                } else {
                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                }

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }


}
