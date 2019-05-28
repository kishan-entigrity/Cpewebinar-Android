package com.entigrity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entigrity.R;
import com.entigrity.databinding.ActivityWebinardetailsBinding;
import com.entigrity.model.registerwebinar.ModelRegisterWebinar;
import com.entigrity.model.timezones;
import com.entigrity.model.webinar_details_new.Webinar_details;
import com.entigrity.model.webinar_like_dislike.Webinar_Like_Dislike_Model;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;
import com.squareup.picasso.Picasso;
import com.universalvideoview.UniversalVideoView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WebinarDetailsActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback {
    public Context context;
    ActivityWebinardetailsBinding binding;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    private static final String TAG = WebinarDetailsActivity.class.getName();
    public int webinarid = 0;
    public String webinar_type = "";
    private String webinar_share_link = "";
    private String is_favorite = "";
    ProgressDialog mProgressDialog;
    LayoutInflater inflater_new;
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private ArrayList<String> arrayListhandout = new ArrayList<>();
    private static String VIDEO_URL = "";


    TextView tv_who_attend, tv_lerning_objectives;
    LinearLayout lv_row_testimonial;
    public boolean ispause = false;
    DownloadTask downloadTask;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PERMISSIONS_MULTIPLE_REQUEST_CAlENDER = 1234;
    private ArrayList<String> arrayListtimezone = new ArrayList<String>();
    private ArrayList<timezones> arrayliattimezones = new ArrayList<timezones>();


    String[] mhandoutArray;
    public ArrayList<String> whoshouldattend = new ArrayList<>();

    public boolean boolean_timezone = true;
    private String webinar_status = "";

    private String year, month, day, hour, min, min_calendar, month_calendar = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinardetails);
        context = WebinarDetailsActivity.this;
        mAPIService = ApiUtilsNew.getAPIService();
        inflater_new = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mProgressDialog = new ProgressDialog(context);


        Intent intent = getIntent();
        if (intent != null) {
            webinarid = intent.getIntExtra(getResources().getString(R.string.pass_webinar_id), 0);
            webinar_type = intent.getStringExtra(getResources().getString(R.string.pass_webinar_type));

            Constant.Log(TAG, "webinar_id" + webinarid);

            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetWebinarDetailsNew();
            } else {
                Snackbar.make(binding.relView, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
            }


        }


        binding.tvWebinarStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.tvWebinarStatus.getText().toString().equalsIgnoreCase(getResources()
                        .getString(R.string.str_webinar_status_register))) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    RegisterWebinar(webinarid, binding.tvWebinarStatus);

                }

            }
        });


        binding.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webinar_type.equalsIgnoreCase(getResources().getString(R.string.str_filter_live))) {
                    //Snackbar.make(binding.ivPlay, context.getResources().getString(R.string.str_goto_meeting_link), Snackbar.LENGTH_SHORT).show();
                    if (webinar_status.equalsIgnoreCase(getResources().getString(R.string.str_webinar_status_register))) {
                        Snackbar.make(binding.ivPlay, context.getResources().getString(R.string.str_video_validation), Snackbar.LENGTH_SHORT).show();
                    } else {
                        String url = "https://global.gotowebinar.com/join/5445100837992316429/962947485";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                } else if (webinar_type.equalsIgnoreCase(getResources().getString(R.string.str_filter_selfstudy))) {
                    if (webinar_status.equalsIgnoreCase(getResources().getString(R.string.str_webinar_status_register))) {
                        Snackbar.make(binding.ivPlay, context.getResources().getString(R.string.str_video_validation), Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (!VIDEO_URL.equalsIgnoreCase("")) {
                            PlayVideo();
                        } else {
                            Snackbar.make(binding.ivPlay, context.getResources().getString(R.string.str_video_link_not_avilable), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                } else {
                    if (!VIDEO_URL.equalsIgnoreCase("")) {
                        PlayVideo();
                    } else {
                        Snackbar.make(binding.ivPlay, context.getResources().getString(R.string.str_video_link_not_avilable), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.lvWhoAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ActivityWhoYouAre.class);
                i.putStringArrayListExtra(getResources().getString(R.string.pass_who_you_are_list), whoshouldattend);
                startActivity(i);

            }
        });

        binding.tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(binding.tvViewMore, context.getResources().getString(R.string.str_comming_soon), Snackbar.LENGTH_SHORT).show();
            }
        });


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndroidVersion();
            }
        });


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (boolean_timezone) {
                    boolean_timezone = false;
                } else {

                    if (arrayliattimezones.size() > 0) {
                        if (!arrayliattimezones.get(position).getStart_date().equalsIgnoreCase("")) {
                            StringTokenizer tokens = new StringTokenizer(arrayliattimezones.get(position).getStart_date(), "-");
                            year = tokens.nextToken();// this will contain year
                            month = tokens.nextToken();//this will contain month

                            month_calendar = month;

                            day = tokens.nextToken();//this will contain day

                            // year = year.substring(2);


                            if (month.equalsIgnoreCase("01")) {
                                month = getResources().getString(R.string.jan);

                            } else if (month.equalsIgnoreCase("02")) {
                                month = getResources().getString(R.string.feb);

                            } else if (month.equalsIgnoreCase("03")) {
                                month = getResources().getString(R.string.march);

                            } else if (month.equalsIgnoreCase("04")) {
                                month = getResources().getString(R.string.april);

                            } else if (month.equalsIgnoreCase("05")) {
                                month = getResources().getString(R.string.may);

                            } else if (month.equalsIgnoreCase("06")) {
                                month = getResources().getString(R.string.june);

                            } else if (month.equalsIgnoreCase("07")) {
                                month = getResources().getString(R.string.july);

                            } else if (month.equalsIgnoreCase("08")) {
                                month = getResources().getString(R.string.aug);

                            } else if (month.equalsIgnoreCase("09")) {
                                month = getResources().getString(R.string.sept);

                            } else if (month.equalsIgnoreCase("10")) {
                                month = getResources().getString(R.string.oct);

                            } else if (month.equalsIgnoreCase("11")) {
                                month = getResources().getString(R.string.nov);

                            } else if (month.equalsIgnoreCase("12")) {
                                month = getResources().getString(R.string.dec);

                            }

                            StringTokenizer tokens_time = new StringTokenizer(arrayliattimezones.get(position).getStart_time(), ":");
                            hour = tokens_time.nextToken();// this will contain year
                            min = tokens_time.nextToken();//this will contain month

                            StringTokenizer tokens_time_min = new StringTokenizer(min, " ");
                            min_calendar = tokens_time_min.nextToken();
                            if (min_calendar.equalsIgnoreCase("00")) {
                                min_calendar = "0";
                            }

                            Constant.Log(TAG, "event" + hour + "  " + min_calendar);


                            binding.tvWebinardate.setText(day + " " + month + " " + year +
                                    " | " + arrayliattimezones.get(position).getStart_time()

                            );


                        }

                    } else {
                        Constant.toast(context, getResources().getString(R.string.str_time_zone_not_found));
                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String htmlString = "<u>Add to Calender</u>";
        binding.tvAddtocalendar.setText(Html.fromHtml(htmlString));


        binding.tvAddtocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndroidVersion_Calender();
            }
        });


        binding.ivnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WebinarDetailsActivity.this, NotificationActivity.class);
                startActivity(i);

            }
        });


        binding.ivfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, context.getResources().getString(R.string.progrees_msg));
                    WebinarFavoriteLikeDislike(webinarid, binding.ivfavorite);
                } else {
                    Snackbar.make(binding.ivfavorite, context.getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        binding.ivshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!webinar_share_link.equalsIgnoreCase("")) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, webinar_share_link);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } else {
                    Constant.toast(context, getResources().getString(R.string.str_sharing_not_avilable));
                }


            }
        });


        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });


    }

    public void PlayVideo() {
        binding.ivPlay.setVisibility(View.GONE);
        binding.ivthumbhel.setVisibility(View.GONE);
        binding.videoLayout.setVisibility(View.VISIBLE);
        binding.videoView.setMediaController(binding.mediaController);
        setVideoAreaSize();
        binding.videoView.setVideoViewCallback(WebinarDetailsActivity.this);

        if (mSeekPosition > 0) {
            binding.videoView.seekTo(mSeekPosition);
        }
        binding.videoView.start();

    }

    public void RegisterWebinar(int webinar_id, final Button button) {

        mAPIService.RegisterWebinar(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context), webinar_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelRegisterWebinar>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(button, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(ModelRegisterWebinar modelRegisterWebinar) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (modelRegisterWebinar.isSuccess() == true) {
                            Snackbar.make(button, modelRegisterWebinar.getMessage(), Snackbar.LENGTH_SHORT).show();
                            recreate();
                        } else {
                            Snackbar.make(button, modelRegisterWebinar.getMessage(), Snackbar.LENGTH_SHORT).show();

                        }


                    }

                });

    }

    private void WebinarFavoriteLikeDislike(final int webinar_id, final ImageView ImageView) {

        mAPIService.PostWebinarLikeDislike(getResources().getString(R.string.accept),
                getResources().getString(R.string.bearer) + AppSettings.get_login_token(context),
                webinar_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Like_Dislike_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(ImageView, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(Webinar_Like_Dislike_Model webinar_like_dislike_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (webinar_like_dislike_model.isSuccess()) {
                            if (webinar_like_dislike_model.getPayload().getIsLike().equalsIgnoreCase(context
                                    .getResources().getString(R.string.fav_yes))) {
                                ImageView.setImageResource(R.mipmap.round_like_icon);
                            } else {
                                ImageView.setImageResource(R.mipmap.round_like_icon_one);
                            }
                            Snackbar.make(ImageView, webinar_like_dislike_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(ImageView, webinar_like_dislike_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            // write your logic here
            if (arrayListhandout.size() > 0) {
                mhandoutArray = new String[arrayListhandout.size()];
                mhandoutArray = arrayListhandout.toArray(mhandoutArray);
                if (mhandoutArray.length > 0) {
                    downloadTask = new DownloadTask(context);
                    downloadTask.execute(mhandoutArray);
                }
            } else {
                Constant.toast(context, getResources().getString(R.string.str_download_link_not_found));
            }


        }

    }

    private void checkAndroidVersion_Calender() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CheckPermission_Calender();
        } else {
            AddToCalender();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writePermission && readExternalFile) {
                        // write your logic here
                        if (arrayListhandout.size() > 0) {
                            mhandoutArray = new String[arrayListhandout.size()];
                            mhandoutArray = arrayListhandout.toArray(mhandoutArray);
                            if (mhandoutArray.length > 0) {
                                downloadTask = new DownloadTask(context);
                                downloadTask.execute(mhandoutArray);
                            }
                        } else {
                            Constant.toast(context, getResources().getString(R.string.str_download_link_not_found));
                        }
                    } else {
                        requestPermissions(
                                new String[]{Manifest.permission
                                        .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSIONS_MULTIPLE_REQUEST);
                    }
                }
                break;
            case PERMISSIONS_MULTIPLE_REQUEST_CAlENDER:
                if (grantResults.length > 0) {

                    boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writePermission && readExternalFile) {
                        AddToCalender();
                    } else {
                        requestPermissions(
                                new String[]{Manifest.permission
                                        .READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                                PERMISSIONS_MULTIPLE_REQUEST_CAlENDER);
                    }


                    break;
                }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    ((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            ((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_MULTIPLE_REQUEST);
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted

            if (arrayListhandout.size() > 0) {
                mhandoutArray = new String[arrayListhandout.size()];
                mhandoutArray = arrayListhandout.toArray(mhandoutArray);
                if (mhandoutArray.length > 0) {
                    downloadTask = new DownloadTask(context);
                    downloadTask.execute(mhandoutArray);
                }
            } else {
                Constant.toast(context, getResources().getString(R.string.str_download_link_not_found));
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CheckPermission_Calender() {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CALENDAR) + ContextCompat
                .checkSelfPermission(context,
                        Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    ((Activity) context, Manifest.permission.READ_CALENDAR) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            ((Activity) context, Manifest.permission.WRITE_CALENDAR)) {

                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                        PERMISSIONS_MULTIPLE_REQUEST_CAlENDER);
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                        PERMISSIONS_MULTIPLE_REQUEST_CAlENDER);
            }
        } else {
            // write your logic code if permission already granted

            AddToCalender();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (binding.videoView != null && binding.videoView.isPlaying()) {
            Constant.Log(TAG, "on_Pause");
            ispause = true;
            mSeekPosition = binding.videoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            binding.videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ispause) {
            Constant.Log(TAG, "On_Resume");
            binding.videoView.seekTo(mSeekPosition);
            binding.videoView.start();
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();


        if (ispause) {
            Constant.Log(TAG, "onRestart");
            binding.videoView.seekTo(mSeekPosition);
            binding.videoView.start();
        }
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        binding.videoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = binding.videoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = binding.videoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                binding.videoLayout.setLayoutParams(videoLayoutParams);
                Uri video = Uri.parse(VIDEO_URL);
                binding.videoView.setVideoURI(video);
                // binding.videoView.requestFocus();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState Position=" + binding.videoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.e(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        Constant.Log(TAG, "++++" + isFullscreen);

        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = binding.videoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            binding.videoLayout.setLayoutParams(layoutParams);
            binding.rvtitle.setVisibility(View.GONE);
            binding.rvbottom.setVisibility(View.GONE);
            //GONE the unconcerned views to leave room for video and controller
            //  mBottomLayout.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams layoutParams = binding.videoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            binding.videoLayout.setLayoutParams(layoutParams);
            binding.rvtitle.setVisibility(View.VISIBLE);
            binding.rvbottom.setVisibility(View.VISIBLE);
            // mBottomLayout.setVisibility(View.VISIBLE);
        }

        //   switchTitleBar(!isFullscreen);
    }


    private void switchTitleBar(boolean show) {
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
        mSeekPosition = binding.videoView.getCurrentPosition();
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.isFullscreen) {
            binding.videoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }


    private void GetWebinarDetailsNew() {
        mAPIService.GetWebinardetails(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context), webinarid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_details>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.relView, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(Webinar_details webinar_details) {

                        if (webinar_details.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getWebinarTitle().equalsIgnoreCase("")) {
                                binding.tvWebinartitle.setText("" + webinar_details.getPayload().getWebinarDetail().getWebinarTitle());
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getCourseid().equalsIgnoreCase("")) {
                                binding.tvCouseId.setText(webinar_details.getPayload().getWebinarDetail().getCourseid());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getCredit().equalsIgnoreCase("")) {
                                binding.tvCredit.setText("" + webinar_details.getPayload().getWebinarDetail().getCredit());
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getWebinarVideoUrl().equalsIgnoreCase("")) {
                                VIDEO_URL = webinar_details.getPayload().getWebinarDetail().getWebinarVideoUrl();
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getCost().equalsIgnoreCase("")) {
                                binding.tvCost.setText("$" + webinar_details.getPayload().getWebinarDetail().getCost());
                            } else {
                                binding.tvCost.setText("FREE");
                            }

                            if (webinar_details.getPayload().getWebinarDetail().getTimezones().size() > 0) {
                                for (int i = 0; i < webinar_details.getPayload().getWebinarDetail().getTimezones().size(); i++) {
                                    arrayListtimezone.add(webinar_details.getPayload().getWebinarDetail().getTimezones().get(i)
                                            .getTimezoneshort());
                                    timezones timezones = new timezones();
                                    timezones.setStart_date(webinar_details.getPayload().getWebinarDetail()
                                            .getTimezones().get(i).getStartDate());
                                    timezones.setStart_time(webinar_details.getPayload().getWebinarDetail()
                                            .getTimezones().get(i).getStartTime());
                                    timezones.setTimezone(webinar_details.getPayload().getWebinarDetail()
                                            .getTimezones().get(i).getTimezone());
                                    timezones.setTimezone_short(webinar_details.getPayload().getWebinarDetail()
                                            .getTimezones().get(i).getTimezoneshort());
                                    arrayliattimezones.add(timezones);
                                }
                                ShowAdapter();
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getStartDate().equalsIgnoreCase("")) {


                                StringTokenizer tokens = new StringTokenizer(webinar_details.getPayload().getWebinarDetail().getStartDate(), "-");
                                year = tokens.nextToken();// this will contain year
                                month = tokens.nextToken();//this will contain month
                                month_calendar = month;
                                day = tokens.nextToken();//this will contain day

                                // year = year.substring(2);


                                if (month.equalsIgnoreCase("01")) {
                                    month = getResources().getString(R.string.jan);

                                } else if (month.equalsIgnoreCase("02")) {
                                    month = getResources().getString(R.string.feb);

                                } else if (month.equalsIgnoreCase("03")) {
                                    month = getResources().getString(R.string.march);

                                } else if (month.equalsIgnoreCase("04")) {
                                    month = getResources().getString(R.string.april);

                                } else if (month.equalsIgnoreCase("05")) {
                                    month = getResources().getString(R.string.may);

                                } else if (month.equalsIgnoreCase("06")) {
                                    month = getResources().getString(R.string.june);

                                } else if (month.equalsIgnoreCase("07")) {
                                    month = getResources().getString(R.string.july);

                                } else if (month.equalsIgnoreCase("08")) {
                                    month = getResources().getString(R.string.aug);

                                } else if (month.equalsIgnoreCase("09")) {
                                    month = getResources().getString(R.string.sept);

                                } else if (month.equalsIgnoreCase("10")) {
                                    month = getResources().getString(R.string.oct);

                                } else if (month.equalsIgnoreCase("11")) {
                                    month = getResources().getString(R.string.nov);

                                } else if (month.equalsIgnoreCase("12")) {
                                    month = getResources().getString(R.string.dec);

                                }


                                StringTokenizer tokens_time = new StringTokenizer(webinar_details.getPayload().getWebinarDetail().getStartTime(), ":");
                                hour = tokens_time.nextToken();// this will contain year
                                min = tokens_time.nextToken();//this will contain month

                                StringTokenizer tokens_time_min = new StringTokenizer(min, " ");
                                min_calendar = tokens_time_min.nextToken();

                                if (min_calendar.equalsIgnoreCase("00")) {
                                    min_calendar = "0";
                                }


                                Constant.Log(TAG, "event" + hour + "  " + min_calendar);


                                binding.tvWebinardate.setText(day + " " + month + " " + year +
                                        " | " + webinar_details.getPayload().getWebinarDetail().getStartTime()

                                );


                            }


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                binding.tvRefundCancelationPolicy.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getRefundAndCancelationPolicy(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                binding.tvRefundCancelationPolicy.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getRefundAndCancelationPolicy()));
                            }


                            if (webinar_details.getPayload().getWebinarDetail().getDuration() != 0) {

                                String result = formatHoursAndMinutes(webinar_details.getPayload().getWebinarDetail().getDuration());

                                Constant.Log(TAG, "duration" + webinar_details.getPayload().getWebinarDetail().getDuration());

                                Constant.Log(TAG, "minutes" + result);

                                StringTokenizer tokens = new StringTokenizer(result, ":");
                                String hour = tokens.nextToken();// this will contain year
                                String min = tokens.nextToken();//this will contain month

                                if (min.equalsIgnoreCase("00")) {
                                    binding.tvDuration.setText(hour + " " + getResources().getString(R.string.str_hour));

                                } else {
                                    binding.tvDuration.setText(hour + " " + getResources().getString(R.string.str_hour) + " " + min +
                                            " " + getResources().getString(R.string.str_min));
                                }


                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getSubjectArea().equalsIgnoreCase("")) {
                                binding.tvSubjectarea.setText("" + webinar_details.getPayload().getWebinarDetail().getSubjectArea());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getCourseLevel().equalsIgnoreCase("")) {
                                binding.tvCourseLevel.setText("" + webinar_details.getPayload().getWebinarDetail().getCourseLevel());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getInstructionalMethod().equalsIgnoreCase("")) {
                                binding.tvInstructionalmethod.setText("" + webinar_details.getPayload().getWebinarDetail().getInstructionalMethod());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getPrerequisite().equalsIgnoreCase("")) {
                                binding.tvPrerequisite.setText("" + webinar_details.getPayload().getWebinarDetail().getPrerequisite());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getAdvancePreparation().equalsIgnoreCase("")) {
                                binding.tvAdvancePreparation.setText("" + webinar_details.getPayload().getWebinarDetail().getAdvancePreparation());
                            }

                            if (webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().size() > 0) {
                               /* binding.tvWhoShouldAttend.setText("" + webinar_detail_model
                                        .getPayload().getWebinar().getWhoShouldAttend());*/

                                whoshouldattend.addAll(webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend());
                                final LinearLayout[] myview = new LinearLayout[webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().size()];

                                final TextView[] myTextViews = new TextView[webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().size()]; // create an empty array;

                                if (webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().size() == 1) {
                                    final View myView_inflat = inflater_new.inflate(R.layout.row_who_should_attend, null);
                                    tv_who_attend = (TextView) myView_inflat.findViewById(R.id.tv_who_attend);
                                    tv_who_attend.setText(webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().get(0));
                                    binding.lvWhoAttend.addView(tv_who_attend);
                                } else {
                                    for (int i = 0; i < 2; i++) {
                                        // create a new textview
                                        final View myView_inflat = inflater_new.inflate(R.layout.row_who_should_attend, null);
                                        tv_who_attend = (TextView) myView_inflat.findViewById(R.id.tv_who_attend);

                                        if (i == 0) {
                                            tv_who_attend.setText(webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().get(i));
                                        } else {
                                            tv_who_attend.setText(webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().size() - 1
                                                    + "+" + "  " + "more");
                                        }

                                        binding.lvWhoAttend.addView(tv_who_attend);
                                        LinearLayout.LayoutParams tvlp = (LinearLayout.LayoutParams) tv_who_attend.getLayoutParams();
                                        tvlp.rightMargin = 5;
                                        tvlp.leftMargin = 5;
                                        tv_who_attend.setLayoutParams(tvlp);
                                        myTextViews[i] = tv_who_attend;


                                    }
                                }


                                for (int i = 0; i < webinar_details.getPayload().getWebinarDetail().getWhoShouldAttend().size(); i++) {
                                    final View myView_inflat = inflater_new.inflate(R.layout.row_tetimonial, null);
                                    lv_row_testimonial = (LinearLayout) myView_inflat.findViewById(R.id.lv_row_testimonial);
                                    binding.lvTestimonial.addView(lv_row_testimonial);
                                    LinearLayout.LayoutParams tvlp = (LinearLayout.LayoutParams) lv_row_testimonial.getLayoutParams();
                                    lv_row_testimonial.setLayoutParams(tvlp);
                                    myview[i] = lv_row_testimonial;
                                }

                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getProgramDescription().equalsIgnoreCase("")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvDescription.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getProgramDescription(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvDescription.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getProgramDescription()));
                                }
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getLearningObjective().equalsIgnoreCase("")) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvLearningObjective.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getLearningObjective(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvLearningObjective.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getLearningObjective()));
                                }


                            }


                            //temp purpose

/*
                            final LinearLayout[] myview = new LinearLayout[1]; // create an empty array;
                            final View myView_inflat = inflater_new.inflate(R.layout.row_tetimonial, null);
                            lv_row_testimonial = (LinearLayout) myView_inflat.findViewById(R.id.lv_row_testimonial);
                            binding.lvTestimonial.addView(lv_row_testimonial);
                            LinearLayout.LayoutParams tvlp = (LinearLayout.LayoutParams) lv_row_testimonial.getLayoutParams();
                            lv_row_testimonial.setLayoutParams(tvlp);
                            myview[1] = lv_row_testimonial;*/


                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getName().equalsIgnoreCase("")) {
                                binding.tvPresenterName.setText(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getName());
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getEmailId().equalsIgnoreCase("")) {
                                binding.tvPresenterEmailid.setText(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getEmailId());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getCompanyName().equalsIgnoreCase("")) {
                                binding.tvCompanyName.setText(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getCompanyName());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer()
                                    .getCompanyWebsite().equalsIgnoreCase("")) {
                                binding.tvCompanyWebsite.setText(webinar_details.getPayload().getWebinarDetail().getAboutPresententer()
                                        .getCompanyWebsite());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getCompanyDesc().equalsIgnoreCase("")) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvCompanyDescription.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getCompanyDesc(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvCompanyDescription.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getCompanyDesc()));
                                }

                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getDesgnination().equalsIgnoreCase("")) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvPresenterDesignation.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getDesgnination(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvPresenterDesignation.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getDesgnination()));
                                }

                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getSpeakerDesc().equalsIgnoreCase("")) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvAboutPresenter.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getSpeakerDesc(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvAboutPresenter.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getSpeakerDesc()));
                                }

                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getFaq().equalsIgnoreCase("")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvFaq.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getFaq(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvFaq.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getFaq()));
                                }
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getStatus().equalsIgnoreCase("")) {
                                if (webinar_details.getPayload().getWebinarDetail().getStatus().equalsIgnoreCase(getResources()
                                        .getString(R.string.str_webinar_status_register))) {
                                    binding.tvWebinarStatus.setBackgroundResource(R.drawable.download_button_webinar_detail);
                                } else {
                                    binding.tvWebinarStatus.setBackgroundResource(R.drawable.squrebutton_webinar_status);
                                }


                                webinar_status = webinar_details.getPayload().getWebinarDetail().getStatus();


                                binding.tvWebinarStatus.setText("" + webinar_details.getPayload().getWebinarDetail().getStatus());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getWebinarThumbnail().equalsIgnoreCase("")) {
                                Picasso.with(context).load(webinar_details.getPayload().getWebinarDetail().getWebinarThumbnail())
                                        .placeholder(R.mipmap.webinar_placeholder)
                                        .into((binding.ivthumbhel));
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getNasbaApproved().getNasbaProfileIcon().equalsIgnoreCase("")) {
                                Picasso.with(context).load(webinar_details.getPayload().getWebinarDetail().getNasbaApproved().getNasbaProfileIcon())
                                        .placeholder(R.mipmap.webinar_placeholder)
                                        .into((binding.ivNasbaProfile));
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getNasbaApproved().getAddress().equalsIgnoreCase("")) {

                                binding.nasbaAddress.setText(webinar_details.getPayload().getWebinarDetail().getNasbaApproved().getAddress());
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getNasbaApproved().getNasbaDesc().equalsIgnoreCase("")) {

                                binding.nasbaDescription.setText(webinar_details.getPayload().getWebinarDetail().getNasbaApproved().getNasbaDesc());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getShareableLink().equalsIgnoreCase("")) {
                                webinar_share_link = webinar_details.getPayload().getWebinarDetail().getShareableLink();
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getLikeDislikeStatus().equalsIgnoreCase("")) {

                                is_favorite = webinar_details.getPayload().getWebinarDetail().getLikeDislikeStatus();

                                if (is_favorite.equalsIgnoreCase(getResources()
                                        .getString(R.string.fav_yes))) {
                                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon);
                                } else {
                                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon_one);
                                }

                            }


                            if (webinar_details.getPayload().getWebinarDetail().getPresentationHandout().size() > 0) {
                                for (int i = 0; i < webinar_details.getPayload().getWebinarDetail().getPresentationHandout().size(); i++) {
                                    arrayListhandout.add(webinar_details.getPayload().getWebinarDetail().getPresentationHandout().get(i));
                                }

                            }

                            if (webinar_type.equalsIgnoreCase(getResources().getString(R.string.str_filter_live))) {
                                binding.relTimezone.setVisibility(View.VISIBLE);

                                if (webinar_status.equalsIgnoreCase(getResources().getString(R.string.str_webinar_status_register))) {
                                    binding.tvAddtocalendar.setVisibility(View.GONE);
                                } else {
                                    binding.tvAddtocalendar.setVisibility(View.VISIBLE);
                                }
                            } else {
                                binding.relTimezone.setVisibility(View.INVISIBLE);
                                binding.tvAddtocalendar.setVisibility(View.INVISIBLE);

                            }


                        } else {
                            Snackbar.make(binding.relView, webinar_details.getMessage(), Snackbar.LENGTH_SHORT).show();


                        }


                    }

                });

    }

    public void ShowAdapter() {
        if (arrayliattimezones.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, arrayListtimezone);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinner.setAdapter(aa);
        }
    }


    public String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }


    public void AddToCalender() {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(year), Integer.parseInt(month_calendar) - 1, Integer.parseInt(day),
                Integer.parseInt(hour), Integer.parseInt(min_calendar));
       /* Calendar endTime = Calendar.getInstance();
        endTime.set(2019, 5, 8, 15, 40);*/
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                /*.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())*/
                .putExtra(CalendarContract.Events.TITLE, binding.tvWebinartitle.getText().toString())
                .putExtra(Intent.EXTRA_EMAIL, AppSettings.get_email_id(context));
        startActivity(intent);


    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        //private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            /*PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();*/
            mProgressDialog.show();
            mProgressDialog.setMessage("downloading");
            progressDialog.setMax(100);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
        }


        @Override
        protected String doInBackground(String... sUrl) {
            int count;
            try {

                for (int i = 0; i < sUrl.length; i++) {
                    URL url = new URL(sUrl[i]);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();

                    Constant.Log("URL", "++++" + url);

                    String extension = sUrl[i].substring(sUrl[i].lastIndexOf('.') + 1).trim();
                    Constant.Log("result", "++++" + extension);


                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(
                            url.openStream(), 8192);
                    // System.out.println("Data::" + sUrl[i]);
                    // Output stream to write file
                    OutputStream output = new FileOutputStream(
                            "/sdcard/handout" + new Date().getTime() + "." + extension);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress((int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
           /* mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);*/
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }
    }


}


