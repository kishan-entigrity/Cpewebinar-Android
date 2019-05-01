package com.entigrity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entigrity.R;
import com.entigrity.databinding.ActivityWebinardetailsBinding;
import com.entigrity.model.webinar_details_new.Webinar_details;
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
    private String webinar_share_link = "";
    private String is_favorite = "";
    public boolean checkfavoritestate = false;
    ProgressDialog mProgressDialog;
    LayoutInflater inflater_new;
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private ArrayList<String> arrayListhandout = new ArrayList<>();
    private static final String VIDEO_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";


    TextView tv_who_attend, tv_lerning_objectives;
    DownloadTask downloadTask;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    String[] mhandoutArray;


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

            Constant.Log(TAG, "webinar_id" + webinarid);

            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetWebinarDetailsNew();
            } else {
                Snackbar.make(binding.relView, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
            }


        }


        /*int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            binding.rvtitle.setVisibility(View.VISIBLE);
            binding.rvbottom.setVisibility(View.VISIBLE);
        } else {
            // code for landscape mode

            binding.rvtitle.setVisibility(View.GONE);
            binding.rvbottom.setVisibility(View.GONE);
        }*/


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

       /* mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true); //cancel the task
            }
        });*/


        String htmlString = "<u>Add to Calender</u>";
        binding.tvAddtocalendar.setText(Html.fromHtml(htmlString));


        binding.tvAddtocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCalender();
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


                if (checkfavoritestate == false) {
                    checkfavoritestate = true;
                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon_one);
                } else {
                    checkfavoritestate = false;
                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon);
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

        binding.videoView.setMediaController(binding.mediaController);
        setVideoAreaSize();
        binding.videoView.setVideoViewCallback(this);

        if (mSeekPosition > 0) {
            binding.videoView.seekTo(mSeekPosition);
        }


        binding.videoView.start();


        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            setContentView(R.layout.activity_webinardetails);
        } else {
            // code for landscape mode
            setContentView(R.layout.activity_landscape_webinardetails);

        }
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


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (binding.videoView != null && binding.videoView.isPlaying()) {
            mSeekPosition = binding.videoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            binding.videoView.pause();
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
        Log.d(TAG, "onSaveInstanceState Position=" + binding.videoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
           /* ViewGroup.LayoutParams layoutParams = binding.videoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(params);*/
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) binding.videoView.getLayoutParams();
            params.width = (int) (300 * metrics.density);
            params.height = (int) (200 * metrics.density);
            params.leftMargin = 30;
            binding.videoView.setLayoutParams(params);

            Constant.Log(TAG, "" + isFullscreen);


        } else {
            /*ViewGroup.LayoutParams layoutParams = binding.videoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            binding.videoLayout.setLayoutParams(layoutParams);*/

            Constant.Log(TAG, "" + isFullscreen);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) binding.videoView.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = (int) (200 * metrics.density);
            params.leftMargin = 0;
            binding.videoView.setLayoutParams(params);


        }

        switchTitleBar(!isFullscreen);
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


  /*  private void GetWebinarDetails() {
        mAPIService.GetWebinarDetail(getResources().getString(R.string.accept), String.valueOf(webinarid), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Detail_Model>() {
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
                    public void onNext(Webinar_Detail_Model webinar_detail_model) {

                        if (webinar_detail_model.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getTitle().equalsIgnoreCase("")) {
                                binding.tvWebinartitle.setText("" + webinar_detail_model.getPayload()
                                        .getWebinar().getTitle());
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getCpaCredit().equalsIgnoreCase("")) {

                                binding.tvCredit.setText("" + webinar_detail_model.getPayload()
                                        .getWebinar().getCpaCredit());

                            }


                            if (!webinar_detail_model.getPayload().getWebinar()
                                    .getFee().equalsIgnoreCase("")) {
                                binding.tvCost.setText("$" + webinar_detail_model.getPayload()
                                        .getWebinar().getFee());
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getDuration().equalsIgnoreCase("")) {

                                int duration = Integer.parseInt(webinar_detail_model.getPayload().getWebinar().getDuration());

                                String result = formatHoursAndMinutes(duration);

                                Constant.Log(TAG, "duration" + duration);

                                Constant.Log(TAG, "minutes" + result);

                                StringTokenizer tokens = new StringTokenizer(result, ":");
                                String hour = tokens.nextToken();// this will contain year
                                String min = tokens.nextToken();//this will contain month


                                binding.tvDuration.setText(hour + " " + getResources().getString(R.string.str_hour) + " " + min +
                                        " " + getResources().getString(R.string.str_min));


                            }


                            if (!webinar_detail_model.getPayload().getWebinar()
                                    .getSubjectArea().equalsIgnoreCase("")) {
                                binding.tvSubjectarea.setText("" + webinar_detail_model
                                        .getPayload().getWebinar().getSubjectArea());
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getCourseLevel().equalsIgnoreCase("")) {
                                binding.tvCourseLevel.setText("" + webinar_detail_model.getPayload().getWebinar()
                                        .getCourseLevel());
                            }


                            if (!webinar_detail_model.getPayload().getWebinar()
                                    .getInstructionalMethod().equalsIgnoreCase("")) {
                                binding.tvInstructionalmethod.setText("" + webinar_detail_model
                                        .getPayload().getWebinar().getInstructionalMethod());
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getPreRequirement().equalsIgnoreCase("")) {
                                binding.tvPrerequisite.setText("" + webinar_detail_model.getPayload()
                                        .getWebinar().getPreRequirement());
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getAdvancePreparation().equalsIgnoreCase("")) {
                                binding.tvAdvancePreparation.setText("" + webinar_detail_model.getPayload()
                                        .getWebinar().getAdvancePreparation());
                            }

                            if (!webinar_detail_model.getPayload().getWebinar().getWhoShouldAttend().equalsIgnoreCase("")) {
                                binding.tvWhoShouldAttend.setText("" + webinar_detail_model
                                        .getPayload().getWebinar().getWhoShouldAttend());
                            }


                           *//* if (!webinar_detail_model.getPayload().getWebinar().getSeries().equalsIgnoreCase("")) {
                                binding.tvSeries.setText("" + webinar_detail_model.getPayload().getWebinar().getSeries());
                            }*//*


                            if (!webinar_detail_model.getPayload().getWebinar().getDescription().equalsIgnoreCase("")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvDescription.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getDescription(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvDescription.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getDescription()));
                                }
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getLearningObjectives().equalsIgnoreCase("")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvLearningObjective.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getLearningObjectives(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvLearningObjective.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getLearningObjectives()));
                                }
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getAboutSpeaker().equalsIgnoreCase("")) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvAboutPresenter.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getAboutSpeaker(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvAboutPresenter.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getAboutSpeaker()));
                                }

                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getFaq().equalsIgnoreCase("")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvFaq.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getFaq(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvFaq.setText(Html.fromHtml(webinar_detail_model.getPayload().getWebinar().getFaq()));
                                }
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getStatus().equalsIgnoreCase("")) {
                                if (webinar_detail_model.getPayload().getWebinar().getStatus().equalsIgnoreCase(getResources()
                                        .getString(R.string.str_webinar_status_register))) {
                                    binding.tvWebinarStatus.setBackgroundResource(R.drawable.download_button_webinar_detail);
                                } else {
                                    binding.tvWebinarStatus.setBackgroundResource(R.drawable.squrebutton_webinar_status);
                                }


                                binding.tvWebinarStatus.setText("" + webinar_detail_model.getPayload().getWebinar().getStatus());
                            }

                          *//*  if (!webinar_detail_model.getPayload()
                                    .getWebinar().getStatus().equalsIgnoreCase("")) {
                                binding.tvWebinarStatus.setText("" + webinar_detail_model.getPayload().getWebinar().getStatus());
                            }
*//*

                            if (!webinar_detail_model.getPayload().getWebinar().getWebinarThumbnailImage().equalsIgnoreCase("")) {
                                Picasso.with(context).load(webinar_detail_model.getPayload().getWebinar().getWebinarThumbnailImage())
                                        .placeholder(R.mipmap.webinar_placeholder)
                                        .into((binding.ivthumbhel));
                            }


                            if (!webinar_detail_model.getPayload().getWebinar().getWebinarShareLink().equalsIgnoreCase("")) {
                                webinar_share_link = webinar_detail_model.getPayload().getWebinar().getWebinarShareLink();
                            }

                            if (!webinar_detail_model.getPayload().getWebinar().getWebinarLike().equalsIgnoreCase("")) {

                                is_favorite = webinar_detail_model.getPayload().getWebinar().getWebinarLike();

                                if (is_favorite.equalsIgnoreCase(getResources()
                                        .getString(R.string.Yes))) {
                                    checkfavoritestate = false;
                                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon);
                                } else {
                                    checkfavoritestate = true;
                                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon_one);
                                }

                            }


                        } else {
                            if (webinar_detail_model.getPayload().getAccessToken() != null && !webinar_detail_model.getPayload().getAccessToken()
                                    .equalsIgnoreCase("")) {

                                AppSettings.set_login_token(context, webinar_detail_model.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetWebinarDetails();
                                } else {
                                    Snackbar.make(binding.relView, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                                }


                            } else {
                                Snackbar.make(binding.relView, webinar_detail_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }


                        }


                    }

                });


    }*/

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


                            if (!webinar_details.getPayload().getWebinarDetail().getCredit().equalsIgnoreCase("")) {

                                binding.tvCredit.setText("" + webinar_details.getPayload().getWebinarDetail().getCredit());

                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getCost().equalsIgnoreCase("")) {
                                binding.tvCost.setText("$" + webinar_details.getPayload().getWebinarDetail().getCost());
                            } else {
                                binding.tvCost.setText("FREE");
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getRefundAndCancelationPolicy().equalsIgnoreCase("")) {
                                binding.tvRefundCancelationPolicy.setText(webinar_details.getPayload().getWebinarDetail().getRefundAndCancelationPolicy());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getDuration().equalsIgnoreCase("")) {
                                int duration = Integer.parseInt(webinar_details.getPayload().getWebinarDetail().getDuration());

                                String result = formatHoursAndMinutes(duration);

                                Constant.Log(TAG, "duration" + duration);

                                Constant.Log(TAG, "minutes" + result);

                                StringTokenizer tokens = new StringTokenizer(result, ":");
                                String hour = tokens.nextToken();// this will contain year
                                String min = tokens.nextToken();//this will contain month


                                binding.tvDuration.setText(hour + " " + getResources().getString(R.string.str_hour) + " " + min +
                                        " " + getResources().getString(R.string.str_min));


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

                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getProgramDescription().equalsIgnoreCase("")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvDescription.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getProgramDescription(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvDescription.setText(Html.fromHtml(webinar_details.getPayload().getWebinarDetail().getProgramDescription()));
                                }
                            }


                            if (webinar_details.getPayload().getWebinarDetail().getLearningObjective().size() > 0) {


                                final TextView[] myTextViews = new TextView[webinar_details.getPayload().getWebinarDetail().getLearningObjective().size()]; // create an empty array;

                                for (int i = 0; i < webinar_details.getPayload().getWebinarDetail().getLearningObjective().size(); i++) {
                                    final View myView_inflat = inflater_new.inflate(R.layout.row_learning_objective, null);
                                    tv_lerning_objectives = (TextView) myView_inflat.findViewById(R.id.tv_learning_objectives);
                                    tv_lerning_objectives.setText(webinar_details.getPayload().getWebinarDetail().getLearningObjective().get(i));
                                    binding.lvLearningObjective.addView(tv_lerning_objectives);
                                    LinearLayout.LayoutParams tvlp = (LinearLayout.LayoutParams) tv_lerning_objectives.getLayoutParams();
                                    tvlp.rightMargin = 5;
                                    tvlp.leftMargin = 5;
                                    tv_lerning_objectives.setLayoutParams(tvlp);
                                    myTextViews[i] = tv_lerning_objectives;
                                }


                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getName().equalsIgnoreCase("")) {
                                binding.tvPresenterName.setText(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getName());
                            }

                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getEmailId().equalsIgnoreCase("")) {
                                binding.tvPresenterEmailid.setText(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getEmailId());
                            }


                            if (!webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getCompanyName().equalsIgnoreCase("")) {
                                binding.tvCompanyName.setText(webinar_details.getPayload().getWebinarDetail().getAboutPresententer().getCompanyName());
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
                                    checkfavoritestate = false;
                                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon);
                                } else {
                                    checkfavoritestate = true;
                                    binding.ivfavorite.setImageResource(R.mipmap.round_like_icon_one);
                                }

                            }


                            if (webinar_details.getPayload().getWebinarDetail().getPresentationHandout().size() > 0) {
                                for (int i = 0; i < webinar_details.getPayload().getWebinarDetail().getPresentationHandout().size(); i++) {
                                    arrayListhandout.add(webinar_details.getPayload().getWebinarDetail().getPresentationHandout().get(i));
                                }

                            }


                        } else {
                            Snackbar.make(binding.relView, webinar_details.getMessage(), Snackbar.LENGTH_SHORT).show();


                        }


                    }

                });

    }


    public String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }


    public void AddToCalender() {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 0, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 0, 19, 8, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
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

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(
                            url.openStream(), 8192);
                    System.out.println("Data::" + sUrl[i]);
                    // Output stream to write file
                    OutputStream output = new FileOutputStream(
                            "/sdcard/handout" + new Date().getTime() + ".extention");

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


