package com.entigrity.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.activity.LoginActivity;
import com.entigrity.activity.WebinarDetailsActivity;
import com.entigrity.model.registerwebinar.ModelRegisterWebinar;
import com.entigrity.model.webinar_like_dislike.Webinar_Like_Dislike_Model;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyFavoriteAdapter extends RecyclerView.Adapter implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isLoadingAdded = false;
    private Context mContext;
    LayoutInflater mInflater;
    public List<com.entigrity.model.myfavorites.WebinarItem> mList;
    private String start_time;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    public Dialog myDialog;
    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    private static final int CARD_CVC_TOTAL_SYMBOLS = 3;
    private TextView tv_submit, tv_cancel, tv_login;
    private EditText edt_card_number, edt_card_holder_name, edt_expiry_month, edt_expiry_year, edt_cvv;
    Integer[] imageArray = {R.drawable.visa, R.drawable.mastercard, R.drawable.discover, R.drawable.amx};
    private String cardtype = "";

    DownloadTask downloadTask;
    ProgressDialog mProgressDialog;
    public String certificate_link = "";
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    String join_url = "";

    private DownloadManager downloadManager;
    private long refid;
    ArrayList<Long> list = new ArrayList<>();


    public MyFavoriteAdapter(Context mContext, List<com.entigrity.model.myfavorites.WebinarItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mAPIService = ApiUtilsNew.getAPIService();
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        mContext.registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    public void DownloadCertificate(String Certificate) {

        list.clear();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Certificate));
        String extension = Certificate.substring(Certificate.lastIndexOf('.') + 1).trim();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading Document");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/MyCpe/" + "/" + "Webinar_Document" + "." + extension);

        refid = downloadManager.enqueue(request);


        Log.e("OUT", "" + refid);

        list.add(refid);
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {


            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


            Log.e("IN", "" + referenceId);

            list.remove(referenceId);


            if (list.isEmpty()) {


                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(mContext)
                                .setSmallIcon(R.mipmap.app_icon)
                                .setContentTitle("Document")
                                .setContentText("MYCpe");


                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());


            }

        }
    };


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {
        RecyclerView.ViewHolder vh;
        if (viewtype == VIEW_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);


        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_webinar, parent, false);

            vh = new HomeViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof HomeViewHolder) {


            if (!mList.get(position).getWebinarTitle().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_title.setText(mList.get(position).getWebinarTitle());
            }

            if (!mList.get(position).getCertificatelink().equalsIgnoreCase("")) {
                certificate_link = mList.get(position).getCertificatelink();
            }


            if (!mList.get(position).getStatus().equalsIgnoreCase("")) {

                if (mList.get(position).getStatus().equalsIgnoreCase(mContext
                        .getResources().getString(R.string.str_webinar_status_register))) {
                    ((HomeViewHolder) viewHolder).webinar_status.setBackgroundResource(R.drawable.rounded_webinar_status);
                } else {
                    ((HomeViewHolder) viewHolder).webinar_status.setBackgroundResource(R.drawable.rounded_webinar_status);
                }


                ((HomeViewHolder) viewHolder).webinar_status.setText(mList.get(position).getStatus());
            }


            if (!mList.get(position).getFee().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_price_status.setText("$" + mList.get(position).getFee());

            } else {
                ((HomeViewHolder) viewHolder).tv_webinar_price_status.setText("Free");
            }

            if (!mList.get(position).getWebinarThumbnailImage().equalsIgnoreCase("")) {
                Picasso.with(mContext).load(mList.get(position).getWebinarThumbnailImage())
                        .placeholder(R.mipmap.webinar_placeholder)
                        .fit()
                        .centerCrop()
                        .into(((HomeViewHolder) viewHolder).ivwebinar_thumbhel);
            } else {
                ((HomeViewHolder) viewHolder).ivwebinar_thumbhel.setImageResource(R.mipmap.webinar_placeholder);
            }

            if (!mList.get(position).getCpaCredit().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).credit_status.setText(mList.get(position).getCpaCredit());
            }


            if (!mList.get(position).getWebinarType().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_type.setText(mList.get(position).getWebinarType());
            }

            if (!mList.get(position).getSpeakerName().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_favorite_speaker_name.setText(mList.get(position).getSpeakerName());
            }

            if (!mList.get(position).getCompanyName().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_company_name.setText(mList.get(position).getCompanyName());
            }


            if (mList.get(position).getWebinarType().equalsIgnoreCase(mContext
                    .getResources().getString(R.string.str_filter_live))) {
                ((HomeViewHolder) viewHolder).tv_webinar_date.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).tv_webinar_time.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).tv_duration_name.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).dv_time_duration.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).dv_divider.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).tv_timezone.setVisibility(View.VISIBLE);

            } else {
                ((HomeViewHolder) viewHolder).tv_webinar_date.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).tv_webinar_time.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).tv_duration_name.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).dv_time_duration.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).dv_divider.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).tv_timezone.setVisibility(View.INVISIBLE);
            }


            if (mList.get(position).getDuration() != 0) {


                String result = formatHoursAndMinutes(mList.get(position).getDuration());


                StringTokenizer tokens = new StringTokenizer(result, ":");
                String hour = tokens.nextToken();// this will contain year
                String min = tokens.nextToken();//th

                if (min.equalsIgnoreCase("00")) {
                    ((HomeViewHolder) viewHolder).tv_duration_name.setText(hour + " " + mContext.getResources().getString(R.string.str_hour));
                } else {
                    ((HomeViewHolder) viewHolder).tv_duration_name.setText(hour + " " + mContext.getResources().getString(R.string.str_hour) + " " + min +
                            " " + mContext.getResources().getString(R.string.str_min));
                }


            }


            if (mList.get(position).getPeopleRegisterWebinar() == 0) {
                ((HomeViewHolder) viewHolder).tv_attend_views.setText("" + 0);
            } else {
                ((HomeViewHolder) viewHolder).tv_attend_views.setText("" + mList.get(position).getPeopleRegisterWebinar());
            }


            if (mList.get(position).getFavWebinarCount() == 0) {
                ((HomeViewHolder) viewHolder).tv_favorite_count.setText("" + 0);
            } else {
                ((HomeViewHolder) viewHolder).tv_favorite_count.setText("" + mList.get(position).getFavWebinarCount());
            }


            if (!mList.get(position).getStartDate().equalsIgnoreCase("")) {


                StringTokenizer tokens = new StringTokenizer(mList.get(position).getStartDate(), "-");
                String year = tokens.nextToken();// this will contain year
                String month = tokens.nextToken();//this will contain month
                String day = tokens.nextToken();//this will contain day

                // year = year.substring(2);


                if (month.equalsIgnoreCase("01")) {
                    month = mContext.getResources().getString(R.string.jan);

                } else if (month.equalsIgnoreCase("02")) {
                    month = mContext.getResources().getString(R.string.feb);

                } else if (month.equalsIgnoreCase("03")) {
                    month = mContext.getResources().getString(R.string.march);

                } else if (month.equalsIgnoreCase("04")) {
                    month = mContext.getResources().getString(R.string.april);

                } else if (month.equalsIgnoreCase("05")) {
                    month = mContext.getResources().getString(R.string.may);

                } else if (month.equalsIgnoreCase("06")) {
                    month = mContext.getResources().getString(R.string.june);

                } else if (month.equalsIgnoreCase("07")) {
                    month = mContext.getResources().getString(R.string.july);

                } else if (month.equalsIgnoreCase("08")) {
                    month = mContext.getResources().getString(R.string.aug);

                } else if (month.equalsIgnoreCase("09")) {
                    month = mContext.getResources().getString(R.string.sept);

                } else if (month.equalsIgnoreCase("10")) {
                    month = mContext.getResources().getString(R.string.oct);

                } else if (month.equalsIgnoreCase("11")) {
                    month = mContext.getResources().getString(R.string.nov);

                } else if (month.equalsIgnoreCase("12")) {
                    month = mContext.getResources().getString(R.string.dec);

                }


                ((HomeViewHolder) viewHolder).tv_webinar_date.setText(day + " " + month + " " + year);


            }
            if (mList.get(position).getTimeZone() != null) {
                ((HomeViewHolder) viewHolder).tv_timezone.setText(mList.get(position).getTimeZone());
            }

       /*     if (Constant.checklikedislikestatusall.size() > 0) {
                String webinarlikestatus = Constant.checklikedislikestatusall.get(mList.get(position).getWebinarTitle());

                if (webinarlikestatus.equalsIgnoreCase(mContext
                        .getResources().getString(R.string.fav_yes))) {
                    ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like_hover);
                } else {
                    ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like);
                }
            }*/


            if (mList.get(position).getWebinarLike().equalsIgnoreCase(mContext
                    .getResources().getString(R.string.fav_yes))) {
                ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.mipmap.like_orange);
            } else {
                ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like);
            }

            if (!mList.get(position).getStartTime().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_time.setText(mList.get(position).getStartTime());
            }


           /* if (!mList.get(clickedposition).getStartTime().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_time.setVisibility(View.VISIBLE);

                StringTokenizer tokens = new StringTokenizer(mList.get(clickedposition).getStartTime(), " ");
                String date = tokens.nextToken();// this will contain date

                start_time = tokens.nextToken();


                StringTokenizer tokens_split_time = new StringTokenizer(start_time, ":");
                String hours = tokens_split_time.nextToken();
                String min = tokens_split_time.nextToken();
                String second = tokens_split_time.nextToken();


                if (Integer.parseInt(hours) > 12) {
                    ((HomeViewHolder) viewHolder).tv_webinar_time.setText(hours + " : " + min + " " +
                            mContext.getResources().getString(R.string.time_pm));

                } else {
                    ((HomeViewHolder) viewHolder).tv_webinar_time.setText(hours + " : " + min + " " +
                            mContext.getResources().getString(R.string.time_am));

                }


            } else {
                ((HomeViewHolder) viewHolder).tv_webinar_time.setVisibility(View.GONE);
            }*/


            ((HomeViewHolder) viewHolder).ivshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!AppSettings.get_login_token(mContext).isEmpty()) {

                        if (!mList.get(position).getWebinarShareLink().equalsIgnoreCase("")) {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = mList.get(position).getWebinarShareLink();
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        } else {
                            Constant.toast(mContext, mContext.getResources().getString(R.string.str_sharing_not_avilable));
                        }

                    }

                }
            });


            ((HomeViewHolder) viewHolder).ivwebinar_thumbhel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppSettings.get_login_token(mContext).isEmpty()) {
                        Intent i = new Intent(mContext, WebinarDetailsActivity.class);
                        i.putExtra(mContext.getResources().getString(R.string.pass_webinar_id), mList
                                .get(position).getId());
                        i.putExtra(mContext.getResources().getString(R.string.pass_webinar_type), mList
                                .get(position).getWebinarType());

                        mContext.startActivity(i);
                    } else {
                        ShowPopUp();
                    }

                   /* Intent i = new Intent(mContext, StripePaymentActivity.class);
                    mContext.startActivity(i);*/


                }
            });


            ((HomeViewHolder) viewHolder).webinar_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!AppSettings.get_login_token(mContext).isEmpty()) {
                        if (mList.get(position).getStatus().equalsIgnoreCase(mContext
                                .getResources().getString(R.string.str_webinar_status_register))) {
                            if (!mList.get(position).getFee().equalsIgnoreCase("")) {
                                Constant.ShowPopUp(mContext.getResources().getString(R.string.payment_validate_msg), mContext);
                            } else {
                                if (Constant.isNetworkAvailable(mContext)) {
                                    progressDialog = DialogsUtils.showProgressDialog(mContext, mContext.getResources().getString(R.string.progrees_msg));
                                    RegisterWebinar(mList.get(position).getId(), mList.get(position).getScheduleid(), ((HomeViewHolder) viewHolder).webinar_status, position);
                                } else {
                                    Snackbar.make(((HomeViewHolder) viewHolder).webinar_status, mContext.getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        } else if (mList.get(position).getStatus().equalsIgnoreCase(mContext
                                .getResources().getString(R.string.str_webinar_status_certificate))) {
                            checkAndroidVersion();
                        } else if (mList.get(position).getWebinarType().equalsIgnoreCase(mContext.getResources().getString(R.string.str_filter_live))) {
                            if (mList.get(position).getStatus().equalsIgnoreCase(
                                    mContext.getResources().getString(R.string.str_webinar_status_enroll))) {
                                String url = mList.get(position).getJoinurl();
                                if (!url.equalsIgnoreCase("")) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    mContext.startActivity(i);
                                } else if (!join_url.equalsIgnoreCase("")) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(join_url));
                                    mContext.startActivity(i);
                                } else {
                                    Constant.toast(mContext, mContext.getResources().getString(R.string.str_joinlink_not_avilable));
                                }
                            }
                        }
                    }


                }
            });

            ((HomeViewHolder) viewHolder).ivfavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppSettings.get_login_token(mContext).isEmpty()) {
                        if (Constant.isNetworkAvailable(mContext)) {
                            progressDialog = DialogsUtils.showProgressDialog(mContext, mContext.getResources().getString(R.string.progrees_msg));
                            WebinarFavoriteLikeDislike(((HomeViewHolder) viewHolder).tv_favorite_count, mList.get(position).getId(), ((HomeViewHolder) viewHolder).ivfavorite, position);
                        } else {
                            Snackbar.make(((HomeViewHolder) viewHolder).ivfavorite, mContext.getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                        }

                    }

                }
            });


        } /*else {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }*/

    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            // write your logic here
            if (!certificate_link.equalsIgnoreCase("")) {
              /*  downloadTask = new DownloadTask(mContext);
                downloadTask.execute(certificate_link);*/
                DownloadCertificate(certificate_link);
            } else {
                Constant.toast(mContext, mContext.getResources().getString(R.string.str_certificate_link_not_found));
            }


        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    ((Activity) mContext, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            ((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity) mContext).requestPermissions(new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }


            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity) mContext).requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        } else {
            // write your logic code if permission already granted

            if (!certificate_link.equalsIgnoreCase("")) {
               /* downloadTask = new DownloadTask(mContext);
                downloadTask.execute(certificate_link);*/

                DownloadCertificate(certificate_link);
            } else {
                Constant.toast(mContext, mContext.getResources().getString(R.string.str_certificate_link_not_found));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writePermission && readExternalFile) {
                        // write your logic here
                        if (!certificate_link.equalsIgnoreCase("")) {
                            /*downloadTask = new DownloadTask(mContext);
                            downloadTask.execute(certificate_link);*/

                            DownloadCertificate(certificate_link);
                        } else {
                            Constant.toast(mContext, mContext.getResources().getString(R.string.str_certificate_link_not_found));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity) mContext).requestPermissions(
                                    new String[]{Manifest.permission
                                            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSIONS_MULTIPLE_REQUEST);
                        }
                    }
                }
                break;


        }


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
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.show();
            mProgressDialog.setMessage("downloading");
            mProgressDialog.setMax(100);
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
                            "/sdcard/certificate" + new Date().getTime() + "." + extension);

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
                Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
        }
    }


    public void ShowPopUp() {

        myDialog = new Dialog(mContext);
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

                Intent i = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(i);

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


    public String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }


    public void add(com.entigrity.model.myfavorites.WebinarItem webinarItem) {
        mList.add(webinarItem);
        notifyItemInserted(mList.size());
    }

    public void addAll(List<com.entigrity.model.myfavorites.WebinarItem> mcList) {
        for (com.entigrity.model.myfavorites.WebinarItem mc : mcList) {
            add(mc);
        }
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new com.entigrity.model.myfavorites.WebinarItem());
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (position == mList.size() - 1 && isLoadingAdded) ? VIEW_ITEM : VIEW_PROG;
    }


    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_webinar_title, tv_webinar_price_status, tv_webinar_date, tv_webinar_time, tv_duration_name,
                tv_favorite_count, tv_attend_views, tv_favorite_speaker_name, tv_company_name, tv_timezone;
        ImageView ivwebinar_thumbhel, ivshare;
        View dv_time_duration, dv_divider;
        Button credit_status, webinar_status, tv_webinar_type;
        ImageView ivfavorite;
        RelativeLayout rel_item;


        private HomeViewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);

            dv_time_duration = (View) itemView.findViewById(R.id.dv_time_duration);
            dv_divider = (View) itemView.findViewById(R.id.dv_divider);

            ivfavorite = (ImageView) itemView.findViewById(R.id.ivfavorite);
            ivfavorite = (ImageView) itemView.findViewById(R.id.ivfavorite);
            credit_status = (Button) itemView.findViewById(R.id.credit_status);
            webinar_status = (Button) itemView.findViewById(R.id.webinar_status);
            ivwebinar_thumbhel = (ImageView) itemView.findViewById(R.id.ivwebinar_thumbhel);
            ivshare = (ImageView) itemView.findViewById(R.id.ivshare);
            tv_webinar_title = (TextView) itemView.findViewById(R.id.tv_webinar_title);
            tv_webinar_price_status = (TextView) itemView.findViewById(R.id.tv_webinar_price_status);
            tv_webinar_date = (TextView) itemView.findViewById(R.id.tv_webinar_date);
            tv_webinar_time = (TextView) itemView.findViewById(R.id.tv_webinar_time);
            tv_duration_name = (TextView) itemView.findViewById(R.id.tv_duration_name);
            tv_webinar_type = (Button) itemView.findViewById(R.id.tv_webinar_type);
            tv_favorite_count = (TextView) itemView.findViewById(R.id.tv_favorite_count);
            tv_attend_views = (TextView) itemView.findViewById(R.id.tv_attend_views);
            tv_favorite_speaker_name = (TextView) itemView.findViewById(R.id.tv_favorite_speaker_name);
            tv_timezone = (TextView) itemView.findViewById(R.id.tv_timezone);
            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            rel_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);


        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.loadmore_progress);
        }
    }

    private void WebinarFavoriteLikeDislike(final TextView textView, final int webinar_id, final ImageView ImageView, final int position) {

        mAPIService.PostWebinarLikeDislike(mContext.getResources().getString(R.string.accept),
                mContext.getResources().getString(R.string.bearer) +" "+AppSettings.get_login_token(mContext),
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

                        String message = Constant.GetReturnResponse(mContext, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(ImageView, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(Webinar_Like_Dislike_Model webinar_like_dislike_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (webinar_like_dislike_model.isSuccess()) {
                            if (webinar_like_dislike_model.getPayload().getIsLike().equalsIgnoreCase(mContext
                                    .getResources().getString(R.string.fav_yes))) {
                               /* ImageView.setImageResource(R.drawable.like_hover);
                                int favcount = mList.get(position).getFavWebinarCount() + 1;
                                textView.setText("" + favcount);
                                mList.get(position).setFavWebinarCount(favcount);
                                mList.get(position).setWebinarLike(mContext
                                        .getResources().getString(R.string.fav_yes));*/
                               /* Constant.checklikedislikestatusall.put(mList.get(position).getWebinarTitle(),
                                        webinar_like_dislike_model.getPayload().getIsLike());*/
                            } else {
                                ImageView.setImageResource(R.drawable.like);
                                /*int favcount = mList.get(position).getFavWebinarCount() - 1;
                                textView.setText("" + favcount);
                                mList.get(position).setFavWebinarCount(favcount);
                                mList.get(position).setWebinarLike(mContext
                                        .getResources().getString(R.string.fav_No));*/
                                mList.remove(position);
                                notifyDataSetChanged();

                                /*Constant.checklikedislikestatusall.put(mList.get(position).getWebinarTitle(),
                                        webinar_like_dislike_model.getPayload().getIsLike());*/
                            }
                            Snackbar.make(ImageView, webinar_like_dislike_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(ImageView, webinar_like_dislike_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }




    public void RegisterWebinar(int webinar_id, int schedule_id, final Button button, final int position) {

        mAPIService.RegisterWebinar(mContext.getResources().getString(R.string.accept), mContext.getResources().getString(R.string.bearer) +" "+AppSettings.get_login_token(mContext), webinar_id, schedule_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelRegisterWebinar>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        String message = Constant.GetReturnResponse(mContext, e);
                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(button, message, Snackbar.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onNext(ModelRegisterWebinar modelRegisterWebinar) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (modelRegisterWebinar.isSuccess() == true) {

                            Snackbar.make(button, modelRegisterWebinar.getMessage(), Snackbar.LENGTH_SHORT).show();

                            button.setText(modelRegisterWebinar.getPayload().getRegisterStatus());
                            button.setBackgroundResource(R.drawable.rounded_webinar_status);
                            mList.get(position).setStatus(modelRegisterWebinar.getPayload().getRegisterStatus());

                            if (!modelRegisterWebinar.getPayload().getJoinUrl().equalsIgnoreCase("")) {
                                join_url = modelRegisterWebinar.getPayload().getJoinUrl();
                                Constant.Log("joinurl", "joinurl" + join_url);
                            }


                           /* if (mList.get(position).getWebinarType().equalsIgnoreCase(mContext.getResources()
                                    .getString(R.string.str_filter_live))) {

                            } else if (mList.get(position).getWebinarType().equalsIgnoreCase(mContext.getResources()
                                    .getString(R.string.str_self_study_on_demand))) {
                                button.setText("WATCH NOW");
                                button.setBackgroundResource(R.drawable.rounded_webinar_status);
                                mList.get(position).setStatus("WATCH NOW");
                            }*/
                        } else {
                            Snackbar.make(button, modelRegisterWebinar.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }

                });

    }


}
