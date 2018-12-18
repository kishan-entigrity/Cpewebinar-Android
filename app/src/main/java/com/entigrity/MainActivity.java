package com.entigrity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.activity.LoginActivity;
import com.entigrity.adapter.ExpandableListAdapter;
import com.entigrity.fragments.ChangePasswordFragment;
import com.entigrity.fragments.ContactUsFragment;
import com.entigrity.fragments.ViewProfileFragment;
import com.entigrity.fragments.UserDashBoardFragment;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        //  navigationView.setNavigationItemSelectedListener(this);

        SetDefault();


        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToolbarTitle(2);
                viewProfileFragment = new ViewProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, viewProfileFragment, "viewprofilefragment").addToBackStack("null").commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


    }


    public void ShowPopUpSucess(String message, final Context context) {
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

    }


    public void SetDefault() {
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

        menuModel = new MenuModel("Instructor Profile", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Company Profile", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
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
                        } else if (groupPosition == 8) {
                            if (Constant.isNetworkAvailable(context)) {
                                ShowPopUp();
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

                            ShowPopUpSucess(logoutModel.getMessage(), context);
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


    public void ShowPopUp()

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
    }

}
