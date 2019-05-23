package com.entigrity.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.activity.WebinarDetailsActivity;
import com.entigrity.model.registerwebinar.ModelRegisterWebinar;
import com.entigrity.model.webinar_like_dislike.Webinar_Like_Dislike_Model;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeMyWebinarAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isLoadingAdded = false;
    private Context mContext;
    LayoutInflater mInflater;
    public List<com.entigrity.model.homewebinarnew.WebinarItem> mList;
    private String start_time;
    private APIService mAPIService;
    ProgressDialog progressDialog;

    public HomeMyWebinarAdapter(Context mContext, List<com.entigrity.model.homewebinarnew.WebinarItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mAPIService = ApiUtilsNew.getAPIService();
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }


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

            vh = new MyWebinarHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof MyWebinarHolder) {
            if (!mList.get(position).getWebinarTitle().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).tv_webinar_title.setText(mList.get(position).getWebinarTitle());
            }

            if (!mList.get(position).getStatus().equalsIgnoreCase("")) {

                if (mList.get(position).getStatus().equalsIgnoreCase(mContext
                        .getResources().getString(R.string.str_webinar_status_register))) {
                    ((MyWebinarHolder) viewHolder).webinar_status.setBackgroundResource(R.drawable.rounded_credit_home);
                } else {
                    ((MyWebinarHolder) viewHolder).webinar_status.setBackgroundResource(R.drawable.rounded_webinar_status);
                }


                ((MyWebinarHolder) viewHolder).webinar_status.setText(mList.get(position).getStatus());
            }


            if (!mList.get(position).getFee().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).tv_webinar_price_status.setText("$" + mList.get(position).getFee());
            } else {
                ((MyWebinarHolder) viewHolder).tv_webinar_price_status.setText("Free");
            }


            if (!mList.get(position).getWebinarThumbnailImage().equalsIgnoreCase("")) {
                Picasso.with(mContext).load(mList.get(position).getWebinarThumbnailImage())
                        .placeholder(R.mipmap.webinar_placeholder)
                        .into(((MyWebinarHolder) viewHolder).ivwebinar_thumbhel);
            }


            if (!mList.get(position).getWebinarThumbnailImage().equalsIgnoreCase("")) {
                Picasso.with(mContext).load(mList.get(position).getWebinarThumbnailImage())
                        .placeholder(R.mipmap.webinar_placeholder)
                        .fit()
                        .centerCrop()
                        .into(((MyWebinarHolder) viewHolder).ivwebinar_thumbhel);
            } else {
                ((MyWebinarHolder) viewHolder).ivwebinar_thumbhel.setImageResource(R.mipmap.webinar_placeholder);
            }


            if (!mList.get(position).getCpaCredit().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).credit_status.setText(mList.get(position).getCpaCredit());
            }


            if (!mList.get(position).getWebinarType().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).tv_webinar_type.setText(mList.get(position).getWebinarType());
            }

            if (!mList.get(position).getSpeakerName().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).tv_favorite_speaker_name.setText(mList.get(position).getSpeakerName());
            }

            if (!mList.get(position).getCompanyName().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).tv_company_name.setText(mList.get(position).getCompanyName());
            }

            if (mList.get(position).getWebinarType().equalsIgnoreCase(mContext
                    .getResources().getString(R.string.str_filter_live))) {
                ((MyWebinarHolder) viewHolder).tv_webinar_date.setVisibility(View.VISIBLE);
                ((MyWebinarHolder) viewHolder).tv_webinar_time.setVisibility(View.VISIBLE);
                ((MyWebinarHolder) viewHolder).tv_duration_name.setVisibility(View.VISIBLE);
                ((MyWebinarHolder) viewHolder).dv_time_duration.setVisibility(View.VISIBLE);
                ((MyWebinarHolder) viewHolder).dv_divider.setVisibility(View.VISIBLE);


            } else {
                ((MyWebinarHolder) viewHolder).tv_webinar_date.setVisibility(View.GONE);
                ((MyWebinarHolder) viewHolder).tv_webinar_time.setVisibility(View.GONE);
                ((MyWebinarHolder) viewHolder).tv_duration_name.setVisibility(View.GONE);
                ((MyWebinarHolder) viewHolder).dv_time_duration.setVisibility(View.GONE);
                ((MyWebinarHolder) viewHolder).dv_divider.setVisibility(View.GONE);
            }


            if (mList.get(position).getDuration() != 0) {
                ((MyWebinarHolder) viewHolder).tv_duration_name.setVisibility(View.VISIBLE);

                String result = formatHoursAndMinutes(mList.get(position).getDuration());


                StringTokenizer tokens = new StringTokenizer(result, ":");
                String hour = tokens.nextToken();// this will contain year
                String min = tokens.nextToken();//th

                if (min.equalsIgnoreCase("00")) {
                    ((MyWebinarHolder) viewHolder).tv_duration_name.setText(hour + " " + mContext.getResources().getString(R.string.str_hour));
                } else {
                    ((MyWebinarHolder) viewHolder).tv_duration_name.setText(hour + " " + mContext.getResources().getString(R.string.str_hour) + " " + min +
                            " " + mContext.getResources().getString(R.string.str_min));
                }


            }


            if (mList.get(position).getPeopleRegisterWebinar() == 0) {
                ((MyWebinarHolder) viewHolder).tv_attend_views.setText("" + 0);
            } else {
                ((MyWebinarHolder) viewHolder).tv_attend_views.setText("" + mList.get(position).getPeopleRegisterWebinar());
            }


            if (mList.get(position).getFavWebinarCount() == 0) {
                ((MyWebinarHolder) viewHolder).tv_favorite_count.setText("" + 0);
            } else {
                ((MyWebinarHolder) viewHolder).tv_favorite_count.setText("" + mList.get(position).getFavWebinarCount());
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


                ((MyWebinarHolder) viewHolder).tv_webinar_date.setText(day + " " + month + " " + year);


            }

            if (!mList.get(position).getStartTime().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).tv_webinar_time.setText(mList.get(position).getStartTime());
            }



            if (mList.get(position).getWebinarLike().equalsIgnoreCase(mContext
                    .getResources().getString(R.string.fav_yes))) {
                ((MyWebinarHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like_hover);
            } else {
                ((MyWebinarHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like);
            }



            ((MyWebinarHolder) viewHolder).ivshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
            });

            if (mList.get(position).getTimeZone() != null) {
                ((MyWebinarHolder) viewHolder).tv_timezone.setText(mList.get(position).getTimeZone());
            }


            ((MyWebinarHolder) viewHolder).ivwebinar_thumbhel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, WebinarDetailsActivity.class);
                    i.putExtra(mContext.getResources().getString(R.string.pass_webinar_id), mList
                            .get(position).getId());
                    i.putExtra(mContext.getResources().getString(R.string.pass_webinar_type), mList
                            .get(position).getWebinarType());
                    mContext.startActivity(i);

                }
            });

            ((MyWebinarHolder) viewHolder).webinar_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mList.get(position).getStatus().equalsIgnoreCase(mContext
                            .getResources().getString(R.string.str_webinar_status_register))) {
                        if (Constant.isNetworkAvailable(mContext)) {
                            progressDialog = DialogsUtils.showProgressDialog(mContext, mContext.getResources().getString(R.string.progrees_msg));
                            RegisterWebinar(mList.get(position).getId(), ((MyWebinarHolder) viewHolder).webinar_status, position);
                        } else {
                            Snackbar.make(((MyWebinarHolder) viewHolder).webinar_status, mContext.getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                        }

                    }


                }
            });


            ((MyWebinarHolder) viewHolder).ivfavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constant.isNetworkAvailable(mContext)) {
                        progressDialog = DialogsUtils.showProgressDialog(mContext, mContext.getResources().getString(R.string.progrees_msg));
                        WebinarFavoriteLikeDislike(
                                ((MyWebinarHolder) viewHolder).tv_favorite_count, mList.get(position).getId(), ((MyWebinarHolder) viewHolder).ivfavorite, position);
                    } else {
                        Snackbar.make(((MyWebinarHolder) viewHolder).ivfavorite, mContext.getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                    }


                }
            });


           /* if (!mList.get(clickedposition).getStartTime().equalsIgnoreCase("")) {
                ((MyWebinarHolder) viewHolder).tv_webinar_time.setVisibility(View.VISIBLE);

                StringTokenizer tokens = new StringTokenizer(mList.get(clickedposition).getStartTime(), " ");
                String date = tokens.nextToken();// this will contain date

                start_time = tokens.nextToken();


                StringTokenizer tokens_split_time = new StringTokenizer(start_time, ":");
                String hours = tokens_split_time.nextToken();
                String min = tokens_split_time.nextToken();
                String second = tokens_split_time.nextToken();


                if (Integer.parseInt(hours) > 12) {
                    ((MyWebinarHolder) viewHolder).tv_webinar_time.setText(hours + " : " + min + " " +
                            mContext.getResources().getString(R.string.time_pm));

                } else {
                    ((MyWebinarHolder) viewHolder).tv_webinar_time.setText(hours + " : " + min + " " +
                            mContext.getResources().getString(R.string.time_am));

                }


            } else {
                ((MyWebinarHolder) viewHolder).tv_webinar_time.setVisibility(View.GONE);
            }
*/
        } /*else {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);

        }*/

    }


    public void add(com.entigrity.model.homewebinarnew.WebinarItem webinarItem) {
        mList.add(webinarItem);
        notifyItemInserted(mList.size());
    }

    public void addAll(List<com.entigrity.model.homewebinarnew.WebinarItem> mcList) {
        for (com.entigrity.model.homewebinarnew.WebinarItem mc : mcList) {
            add(mc);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new com.entigrity.model.homewebinarnew.WebinarItem());
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (position == mList.size() - 1 && isLoadingAdded) ? VIEW_ITEM : VIEW_PROG;
    }

    public String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }


    public static class MyWebinarHolder extends RecyclerView.ViewHolder {

        TextView tv_webinar_title, tv_webinar_price_status, tv_webinar_date, tv_webinar_time, tv_duration_name,
                tv_webinar_type, tv_favorite_count, tv_attend_views, tv_favorite_speaker_name, tv_company_name, tv_timezone;
        ImageView ivwebinar_thumbhel, ivshare, dv_time_duration, dv_divider;
        Button credit_status, webinar_status;
        ImageView ivfavorite;
        RelativeLayout rel_item;


        private MyWebinarHolder(View itemView) {
            super(itemView);

            ivfavorite = (ImageView) itemView.findViewById(R.id.ivfavorite);
            credit_status = (Button) itemView.findViewById(R.id.credit_status);
            webinar_status = (Button) itemView.findViewById(R.id.webinar_status);
            dv_time_duration = (ImageView) itemView.findViewById(R.id.dv_time_duration);
            dv_divider = (ImageView) itemView.findViewById(R.id.dv_divider);
            ivwebinar_thumbhel = (ImageView) itemView.findViewById(R.id.ivwebinar_thumbhel);
            ivshare = (ImageView) itemView.findViewById(R.id.ivshare);
            tv_webinar_title = (TextView) itemView.findViewById(R.id.tv_webinar_title);
            tv_webinar_price_status = (TextView) itemView.findViewById(R.id.tv_webinar_price_status);
            tv_webinar_date = (TextView) itemView.findViewById(R.id.tv_webinar_date);
            tv_webinar_time = (TextView) itemView.findViewById(R.id.tv_webinar_time);
            tv_duration_name = (TextView) itemView.findViewById(R.id.tv_duration_name);
            tv_webinar_type = (TextView) itemView.findViewById(R.id.tv_webinar_type);
            tv_favorite_count = (TextView) itemView.findViewById(R.id.tv_favorite_count);
            tv_attend_views = (TextView) itemView.findViewById(R.id.tv_attend_views);
            tv_favorite_speaker_name = (TextView) itemView.findViewById(R.id.tv_favorite_speaker_name);
            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            rel_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);
            tv_timezone = (TextView) itemView.findViewById(R.id.tv_timezone);


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
                mContext.getResources().getString(R.string.bearer) + AppSettings.get_login_token(mContext),
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
                        Snackbar.make(ImageView, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(Webinar_Like_Dislike_Model webinar_like_dislike_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (webinar_like_dislike_model.isSuccess()) {
                            if (webinar_like_dislike_model.getPayload().getIsLike().equalsIgnoreCase(mContext
                                    .getResources().getString(R.string.fav_yes))) {
                                ImageView.setImageResource(R.drawable.like_hover);
                                int favcount = mList.get(position).getFavWebinarCount() + 1;
                                textView.setText("" + favcount);
                                mList.get(position).setFavWebinarCount(favcount);
                                mList.get(position).setWebinarLike(mContext
                                        .getResources().getString(R.string.fav_yes));
                               /* Constant.checklikedislikestatusmywebinar.put(mList.get(position).getWebinarTitle(),
                                        webinar_like_dislike_model.getPayload().getIsLike());*/
                            } else {
                                ImageView.setImageResource(R.drawable.like);
                                int favcount = mList.get(position).getFavWebinarCount() - 1;
                                textView.setText("" + favcount);
                                mList.get(position).setFavWebinarCount(favcount);
                                mList.get(position).setWebinarLike(mContext
                                        .getResources().getString(R.string.fav_No));

                                /*Constant.checklikedislikestatusmywebinar.put(mList.get(position).getWebinarTitle(),
                                        webinar_like_dislike_model.getPayload().getIsLike());*/
                            }
                            Snackbar.make(ImageView, webinar_like_dislike_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(ImageView, webinar_like_dislike_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    public void RegisterWebinar(int webinar_id, final Button button, final int position) {

        mAPIService.RegisterWebinar(mContext.getResources().getString(R.string.accept), mContext.getResources().getString(R.string.bearer) + AppSettings.get_login_token(mContext), webinar_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        Snackbar.make(button, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(ModelRegisterWebinar modelRegisterWebinar) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (modelRegisterWebinar.isSuccess() == true) {
                            Snackbar.make(button, modelRegisterWebinar.getMessage(), Snackbar.LENGTH_SHORT).show();
                            if (mList.get(position).getWebinarType().equalsIgnoreCase(mContext.getResources()
                                    .getString(R.string.str_filter_live))) {
                                button.setText("Enrolled");
                                button.setBackgroundResource(R.drawable.rounded_webinar_status);
                                mList.get(position).setStatus("Enrolled");
                            } else if (mList.get(position).getWebinarType().equalsIgnoreCase(mContext.getResources()
                                    .getString(R.string.str_filter_selfstudy))) {
                                button.setText("WatchNow");
                                button.setBackgroundResource(R.drawable.rounded_webinar_status);
                                mList.get(position).setStatus("WatchNow");
                            } else {
                                button.setText("My certificate");
                                button.setBackgroundResource(R.drawable.rounded_webinar_status);
                                mList.get(position).setStatus("My certificate");
                            }

                        } else {
                            Snackbar.make(button, modelRegisterWebinar.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }

                });

    }


}
