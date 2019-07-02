package com.entigrity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.activity.LoginActivity;
import com.entigrity.activity.PreLoginActivity;
import com.entigrity.fragments.AccountFragment;
import com.entigrity.fragments.CompanyFragment;
import com.entigrity.fragments.ContactUsFragment;
import com.entigrity.fragments.FavoritesFragment;
import com.entigrity.fragments.InstructorFragment;
import com.entigrity.fragments.MyCreditsFragment;
import com.entigrity.fragments.MyFavoriteScreenFragment;
import com.entigrity.fragments.UserDashBoardFragment;
import com.entigrity.fragments.ViewProfileFragment;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import static com.entigrity.utility.Constant.checkmywebinardotstatusset;

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
    private APIService mAPIService_new;
    public TextView tv_popup_ok, tv_popup_cancel;
    public TextView tv_popup_msg, tv_popup_submit;
    ProgressDialog progressDialog;
    private static MainActivity instance;
    public RelativeLayout rel_top_bottom;
    public ImageView iv_mycredit, iv_mywebinar, iv_home, iv_myfavorite, iv_account;
    private static final String TAG = MainActivity.class.getName();
    public int setselectedtab = 0;
    public int selectmywebinardtab = 0;
    public Dialog myDialog;
    public TextView tv_login, tv_cancel;
    Intent intent;
    public int webinarid = 0;
    public String webinar_type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = MainActivity.this;
        intent = getIntent();

        rel_top_bottom = (RelativeLayout) findViewById(R.id.rel_top_bottom);
        iv_mycredit = (ImageView) findViewById(R.id.iv_mycredit);
        iv_mywebinar = (ImageView) findViewById(R.id.iv_mywebinar);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_myfavorite = (ImageView) findViewById(R.id.iv_myfavorite);
        iv_account = (ImageView) findViewById(R.id.iv_account);


        context = MainActivity.this;
        mAPIService_new = ApiUtilsNew.getAPIService();


        iv_mycredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppSettings.get_login_token(context).isEmpty()) {
                    setselectedtab = 0;
                    selectmywebinardtab = 0;
                    SetImageBackground(0);
                    myCreditsFragment = new MyCreditsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myCreditsFragment, getResources()
                            .getString(R.string.mycreditfragment)).addToBackStack(getResources().getString(R.string.add_to_back_stack)).commit();
                } else {
                    ShowPopUp();
                }


            }
        });

        iv_mywebinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppSettings.get_login_token(context).isEmpty()) {
                    setselectedtab = 1;
                    selectmywebinardtab = 1;
                    checkmywebinardotstatusset = false;
                    SetImageBackground(1);
                    SetDefault();


                } else {
                    ShowPopUp();
                }


            }
        });


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselectedtab = 0;
                selectmywebinardtab = 0;
                checkmywebinardotstatusset = false;
                SetImageBackground(2);
                SetDefault();

            }
        });

        iv_myfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppSettings.get_login_token(context).isEmpty()) {
                    setselectedtab = 2;
                    selectmywebinardtab = 0;
                    SetImageBackground(3);
                    myFavoriteScreenFragment = new MyFavoriteScreenFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFavoriteScreenFragment, getResources()
                            .getString(R.string.myfavoritescreenfragment)).addToBackStack(getResources().getString(R.string.add_to_back_stack)).commit();

                } else {
                    ShowPopUp();
                }


            }
        });

        iv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppSettings.get_login_token(context).isEmpty()) {
                    setselectedtab = 0;
                    selectmywebinardtab = 0;
                    SetImageBackground(4);
                    accountFragment = new AccountFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, accountFragment, getResources()
                            .getString(R.string.accountfragment)).addToBackStack(getResources().getString(R.string.add_to_back_stack)).commit();

                } else {
                    ShowPopUp();
                }


            }
        });

        SetDefault();
       /* if (intent != null) {
            if (getIntent().hasExtra(getResources().getString(R.string.pass_webinar_id))) {
                webinarid = intent.getIntExtra(getResources().getString(R.string.pass_webinar_id), 0);
                webinar_type = intent.getStringExtra(getResources().getString(R.string.pass_webinar_type));

                Constant.Log("flag", "+++" + webinar_type + "  " + webinarid);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        if (Constant.isNetworkAvailable(context)) {
                            Intent i = new Intent(MainActivity.this, WebinarDetailsActivity.class);
                            i.putExtra(getResources().getString(R.string.pass_webinar_type), webinar_type);
                            i.putExtra(getResources().getString(R.string.pass_webinar_id), webinarid);
                            startActivity(i);
                        } else {
                            Snackbar.make(iv_mycredit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }


        }
*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkmywebinardotstatusset = false;
    }


    public void AutoLogout() {
        if (Constant.isNetworkAvailable(context)) {
            AppSettings.removeFromSharedPreferences(context, getResources().getString(R.string.str_token));
            AppSettings.set_login_token(context, "");
            AppSettings.set_device_id(context, "");
            AppSettings.set_profile_username(context, "");
            AppSettings.set_email_id(context, "");

            Intent i = new Intent(context, PreLoginActivity.class);
            startActivity(i);
            finish();
            //  Logout(AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }
    }


  /*  public void Logout(String Authorization, String device_id, String device_token, String device_type) {

        // RxJava
        mAPIService_new.logout(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + Authorization, device_id
                , device_token, device_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LogoutModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //handle failure response
                        *//*if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }*//*

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(iv_home, message, Snackbar.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onNext(LogoutModel logoutModel) {
                        if (logoutModel.isSuccess()) {
                          *//*  if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }*//*


                            Snackbar.make(iv_home, logoutModel.getMessage(), Snackbar.LENGTH_SHORT).show();

                        } else {
                           *//* if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }*//*

                            Snackbar.make(iv_home, logoutModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });

    }*/


    public void SetCreditScreen() {
        setselectedtab = 0;
        selectmywebinardtab = 0;
        SetImageBackground(0);
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
        finish();
    }


    public void ShowPopUp() {

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.guest_user_popup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_login = (TextView) myDialog.findViewById(R.id.tv_login_guest);
        tv_cancel = (TextView) myDialog.findViewById(R.id.tv_cancel_guest);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }

            }
        });


        myDialog.show();


    }


}
