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
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

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
    public int selectmywebinardtab = 0;
    public Dialog myDialog;
    public TextView tv_login, tv_cancel;


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

                if (!AppSettings.get_login_token(context).isEmpty()) {
                    isclickhome = false;
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
                    isclickhome = false;
                    setselectedtab = 1;
                    selectmywebinardtab = 1;
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

                if (!AppSettings.get_login_token(context).isEmpty()) {
                    setselectedtab = 2;
                    selectmywebinardtab = 0;
                    isclickhome = false;
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
                    isclickhome = false;
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


    }


    public void SetCreditScreen() {
        isclickhome = false;
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


    public void SetFavoriteTab() {
        isclickhome = false;
        setselectedtab = 1;
        selectmywebinardtab = 2;
        SetImageBackground(2);
        userDashBoardFragment = new UserDashBoardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, userDashBoardFragment, getResources().getString(R.string.userdashBoard_fragment))
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
