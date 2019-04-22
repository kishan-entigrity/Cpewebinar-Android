package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.databinding.ActivityWebinardetailsBinding;
import com.entigrity.model.webinar_details.Webinar_Detail_Model;
import com.entigrity.model.webinar_details_new.Webinar_details;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;
import com.squareup.picasso.Picasso;
import com.universalvideoview.UniversalVideoView;

import java.util.Calendar;
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
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";


    TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinardetails);
        context = WebinarDetailsActivity.this;
        mAPIService = ApiUtilsNew.getAPIService();

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


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.toast(context, "comming soon");
            }
        });


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
            ViewGroup.LayoutParams layoutParams = binding.videoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            binding.videoLayout.setLayoutParams(layoutParams);


        } else {
            ViewGroup.LayoutParams layoutParams = binding.videoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            binding.videoLayout.setLayoutParams(layoutParams);

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
        if (this.isFullscreen) {
            binding.videoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }


    private void GetWebinarDetails() {
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


                           /* if (!webinar_detail_model.getPayload().getWebinar().getSeries().equalsIgnoreCase("")) {
                                binding.tvSeries.setText("" + webinar_detail_model.getPayload().getWebinar().getSeries());
                            }*/


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






                           /* if (!webinar_detail_model.getPayload().getWebinar().getStatus().equalsIgnoreCase("")) {
                                if (webinar_detail_model.getPayload().getWebinar().getStatus().equalsIgnoreCase(getResources()
                                        .getString(R.string.str_webinar_status_register))) {
                                    binding.tvWebinarStatus.setBackgroundResource(R.drawable.download_button_webinar_detail);
                                } else {
                                    binding.tvWebinarStatus.setBackgroundResource(R.drawable.squrebutton_webinar_status);
                                }


                                binding.tvWebinarStatus.setText("" + webinar_detail_model.getPayload().getWebinar().getStatus());
                            }*/

                            if (!webinar_detail_model.getPayload()
                                    .getWebinar().getStatus().equalsIgnoreCase("")) {
                                binding.tvWebinarStatus.setText("" + webinar_detail_model.getPayload().getWebinar().getStatus());
                            }


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
}
