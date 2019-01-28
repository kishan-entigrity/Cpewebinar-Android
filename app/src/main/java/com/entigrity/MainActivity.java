package com.entigrity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.activity.LoginActivity;
import com.entigrity.adapter.ExpandableListAdapter;
import com.entigrity.fragments.ChangePasswordFragment;
import com.entigrity.fragments.CompanyFragment;
import com.entigrity.fragments.ContactUsFragment;
import com.entigrity.fragments.FavoritesFragment;
import com.entigrity.fragments.InstructorFragment;
import com.entigrity.fragments.UserDashBoardFragment;
import com.entigrity.fragments.ViewProfileFragment;
import com.entigrity.model.MenuModel;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    UserDashBoardFragment userDashBoardFragment;
    ChangePasswordFragment changePasswordFragment;
    ViewProfileFragment viewProfileFragment;
    ContactUsFragment contactUsFragment;
    InstructorFragment instructorFragment;
    CompanyFragment companyFragment;


    FavoritesFragment favoritesFragment;

    private String[] activityTitles;
    public Context context;
    private static final String TAG = MainActivity.class.getName();
    private APIService mAPIService;
    public Dialog myDialog;
    public TextView tv_popup_ok, tv_popup_cancel;
    DrawerLayout drawer;
    public Dialog myDialog_popup;
    public TextView tv_popup_msg, tv_popup_submit;
    ProgressDialog progressDialog;
    private static MainActivity instance;

    public RelativeLayout rel_top_bottom;

    public ImageView iv_assesement, iv_favorities, iv_home, iv_dash_board, iv_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = MainActivity.this;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        rel_top_bottom = (RelativeLayout) findViewById(R.id.rel_top_bottom);

        iv_assesement = (ImageView) findViewById(R.id.iv_assesement);
        iv_favorities = (ImageView) findViewById(R.id.iv_favorities);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_dash_board = (ImageView) findViewById(R.id.iv_dash_board);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);

        setSupportActionBar(toolbar);
        context = MainActivity.this;
        mAPIService = ApiUtils.getAPIService();

        ImageView floating = (ImageView) findViewById(R.id.fab);

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.ShowPopUp("clicked", context);


            }
        });

        iv_assesement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetImageBackground(0);

                SetDefault();


            }
        });

        iv_favorities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetImageBackground(1);
                setToolbarTitle(6);
                favoritesFragment = new FavoritesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, favoritesFragment, "favoritesFragment").addToBackStack("null").commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }
        });


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetImageBackground(2);
                SetDefault();

            }
        });

        iv_dash_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetImageBackground(3);

                SetDefault();

            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetImageBackground(4);
                setToolbarTitle(2);
                viewProfileFragment = new ViewProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, viewProfileFragment, "viewprofilefragment").addToBackStack("null").commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });





        /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);*/

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);


        View headerview = navigationView.getHeaderView(0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);
                Constant.hideKeyboard(MainActivity.this);

            }
        });


        //  navigationView.setNavigationItemSelectedListener(this);

        SetDefault();


        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetImageBackground(4);
                setToolbarTitle(2);
                viewProfileFragment = new ViewProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, viewProfileFragment, "viewprofilefragment").addToBackStack("null").commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


    }


    public void SetImageBackground(int position) {

        if (position == 0) {
            iv_assesement.setImageResource(R.mipmap.assesment_hover);
            iv_favorities.setImageResource(R.mipmap.favorites);
            iv_dash_board.setImageResource(R.mipmap.dash_board);
            iv_profile.setImageResource(R.mipmap.footer_profile);
        } else if (position == 1) {
            iv_assesement.setImageResource(R.mipmap.assesment);
            iv_favorities.setImageResource(R.mipmap.favorites_hover);
            iv_dash_board.setImageResource(R.mipmap.dash_board);
            iv_profile.setImageResource(R.mipmap.footer_profile);
        } else if (position == 2) {
            iv_assesement.setImageResource(R.mipmap.assesment);
            iv_favorities.setImageResource(R.mipmap.favorites);
            iv_dash_board.setImageResource(R.mipmap.dash_board);
            iv_profile.setImageResource(R.mipmap.footer_profile);
        } else if (position == 3) {
            iv_assesement.setImageResource(R.mipmap.assesment);
            iv_favorities.setImageResource(R.mipmap.favorites);
            iv_dash_board.setImageResource(R.mipmap.dash_board_hover);
            iv_profile.setImageResource(R.mipmap.footer_profile);
        } else if (position == 4) {
            iv_assesement.setImageResource(R.mipmap.assesment);
            iv_favorities.setImageResource(R.mipmap.favorites);
            iv_dash_board.setImageResource(R.mipmap.dash_board);
            iv_profile.setImageResource(R.mipmap.footer_profile_hover);
        }


    }


   /* public void ShowPopUpSucess(String message, final Context context) {
        myDialog_popup = new Dialog(context);
        myDialog_popup.setContentView(R.layout.activity_popup);
        myDialog_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_popup_msg = (TextView) myDialog_popup.findViewById(R.id.tv_popup_msg);
        tv_popup_submit = (TextView) myDialog_popup.findViewById(R.id.tv_popup_submit);

        tv_popup_msg.setText(message);


        tv_popup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog_popup.isShowing()) {
                    myDialog_popup.dismiss();
                }


                AppSettings.removeFromSharedPreferences(context, getResources().getString(R.string.str_token));


                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();


            }
        });
        myDialog_popup.show();

    }*/

    public void ShowPopUp(String message, final Context context) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                context).create();
        alertDialog.setMessage(message);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
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
        SetImageBackground(0);
        setToolbarTitle(0);
        userDashBoardFragment = new UserDashBoardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, userDashBoardFragment, "UserDashBoardFragment").addToBackStack("null").commit();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Constant.hideKeyboard(MainActivity.this);
            finish();
        } else {
            finish();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel("My Profile", true, true, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel("Dashboard", false, false, "https://www.journaldev.com/7153/core-java-tutorial");
        childModelsList.add(childModel);

        childModel = new MenuModel("Change Password", false, false, "https://www.journaldev.com/19187/java-fileinputstream");
        childModelsList.add(childModel);

        childModel = new MenuModel("My Favorites", false, false, "https://www.journaldev.com/19115/java-filereader");
        childModelsList.add(childModel);

        childModel = new MenuModel("My Assessment", false, false, "https://www.journaldev.com/19115/java-filereader");
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            Log.d("API123", "here");
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel("Home", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Series Webinars", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Contact Us", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Instructor", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Company", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("FAQ", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Privacy Policy", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Logout", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);



       /* if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }*/


    }

    private void setToolbarTitle(int value) {
        getSupportActionBar().setTitle(activityTitles[value]);
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        Constant.Log(TAG, "position" + groupPosition);
                        if (groupPosition == 3) {
                            setToolbarTitle(3);
                            contactUsFragment = new ContactUsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contactUsFragment, "contactusfragment").addToBackStack("null").commit();
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 4) {
                            setToolbarTitle(4);
                            instructorFragment = new InstructorFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, instructorFragment, "instructorfragment").addToBackStack("null").commit();
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 5) {
                            setToolbarTitle(5);
                            companyFragment = new CompanyFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, companyFragment, "companyfragment").addToBackStack("null").commit();
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 8) {
                            if (Constant.isNetworkAvailable(context)) {
                                // ShowPopUp();
                                LogOutPoPUp();

                            } else {
                                Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                            }
                        }
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (childPosition == 0) {
                        setToolbarTitle(0);
                        userDashBoardFragment = new UserDashBoardFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, userDashBoardFragment, "UserDashBoardFragment").addToBackStack("null").commit();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        setToolbarTitle(1);
                        changePasswordFragment = new ChangePasswordFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, changePasswordFragment, "changePasswordFragment").addToBackStack("null").commit();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 2) {
                        setToolbarTitle(6);
                        favoritesFragment = new FavoritesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, favoritesFragment, "favoritesFragment").addToBackStack("null").commit();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }

                    /* if (model.url.length() > 0) {
                     *//* WebView webView = findViewById(R.id.webView);
                        webView.loadUrl(model.url);*//*
                        onBackPressed();
                    }*/
                }

                return false;
            }
        });
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

        // Setting Dialog Title
        // alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.logout_text));


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
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
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }


    /*public void ShowPopUp()
    {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.activity_popup_logout);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_popup_ok = (TextView) myDialog.findViewById(R.id.tv_popup_ok);
        tv_popup_cancel = (TextView) myDialog.findViewById(R.id.tv_popup_cancel);


        tv_popup_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }

                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));

                if (Constant.isNetworkAvailable(context)) {
                    Logout(AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
                } else {
                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                }


            }
        });


        tv_popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }
        });
        myDialog.show();
    }*/

}
