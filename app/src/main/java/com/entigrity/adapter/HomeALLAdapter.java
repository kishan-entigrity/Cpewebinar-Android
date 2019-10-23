package com.entigrity.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.activity.LoginActivity;
import com.entigrity.activity.PaymentActivity;
import com.entigrity.activity.WebinarDetailsActivity;
import com.entigrity.model.registerwebinar.ModelRegisterWebinar;
import com.entigrity.model.webinar_like_dislike.Webinar_Like_Dislike_Model;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;
import com.squareup.picasso.Picasso;

import java.lang.invoke.VolatileCallSite;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeALLAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isLoadingAdded = false;
    private Context mContext;
    LayoutInflater mInflater;
    public List<com.entigrity.model.homewebinarnew.WebinarItem> mList;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    public Dialog myDialog;
    private TextView tv_cancel, tv_login;
    public String certificate_link = "";
    String join_url = "";


    public HomeALLAdapter(Context mContext, List<com.entigrity.model.homewebinarnew.WebinarItem> mList) {
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

            vh = new HomeViewHolder(v);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof HomeViewHolder) {
            Constant.Log("size", "" + mList.size());

          /*  if (!mList.get(position).getWebinarTitle().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_title.setText(mList.get(position).getWebinarTitle());
            }*/

            if (!mList.get(position).getCertificatelink().equalsIgnoreCase("")) {
                certificate_link = mList.get(position).getCertificatelink();
            }


            if (!mList.get(position).getRatingaverage().equalsIgnoreCase("")) {


                if (Float.parseFloat(mList.get(position).getRatingaverage()) == 0.0) {

                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.GONE);

                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 0.5
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 1.0) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.half_one);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);

                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 1.0
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 1.5) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.orange_star_one);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 1.6
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 2.0) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.half_two);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 2.1
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 2.5) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.orange_star_two);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 2.6
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 3.0) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.half_three);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 3.1
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 3.5) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.orange_star_three);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 3.5
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 4.0) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.half_four);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 4.1
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 4.5) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.orange_star_four);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) >= 4.5
                        && Float.parseFloat(mList.get(position).getRatingaverage()) < 5.0) {

                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.half_five);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                } else if (Float.parseFloat(mList.get(position).getRatingaverage()) == 5.0) {
                    ((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.orange_star_five);
                    ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.VISIBLE);
                }


            } else {
                //((HomeViewHolder) viewHolder).iv_rating.setImageResource(R.mipmap.orange_star_line);
                ((HomeViewHolder) viewHolder).iv_rating.setVisibility(View.GONE);

            }


         /*   tv_lable = (TextView) itemView.findViewById(R.id.tv_lable);
            tv_enrolled = (TextView) itemView.findViewById(R.id.tv_enrolled);
            tv_rating_number = (TextView) itemView.findViewById(R.id.tv_rating_number);
            tv_rating_count = (TextView) itemView.findViewById(R.id.tv_rating_count);
            iv_rating = (ImageView) itemView.findViewById(R.id.iv_rating);*/


            if (!mList.get(position).getRatingaverage().equalsIgnoreCase("")) {

                if (Float.parseFloat(mList.get(position).getRatingaverage()) == 0.0) {
                    ((HomeViewHolder) viewHolder).tv_rating_number.setVisibility(View.GONE);
                } else {
                    ((HomeViewHolder) viewHolder).tv_rating_number.setVisibility(View.VISIBLE);
                }

                ((HomeViewHolder) viewHolder).tv_rating_number.setText("" + mList.get(position).getRatingaverage());
            } else {
                ((HomeViewHolder) viewHolder).tv_rating_number.setVisibility(View.GONE);

                //((HomeViewHolder) viewHolder).tv_rating_number.setText("0");
            }


            if (mList.get(position).getRatingcount() != 0) {
                ((HomeViewHolder) viewHolder).tv_rating_count.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).tv_rating_count.setText("" + "(" + mList.get(position).getRatingcount() + ")");
            } else {
                ((HomeViewHolder) viewHolder).tv_rating_count.setVisibility(View.GONE);
                //((HomeViewHolder) viewHolder).tv_rating_count.setText("(0)");
            }


            if (mList.get(position).getEnrolled() != 0) {
                ((HomeViewHolder) viewHolder).tv_enrolled.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).tv_enrolled.setText("" + mList.get(position).getEnrolled() + " " +
                        "Enrolled");
            } else {
                ((HomeViewHolder) viewHolder).tv_enrolled.setVisibility(View.INVISIBLE);
            }


            if (!mList.get(position).getWebinarlable().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_lable.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).tv_lable.setText(mList.get(position).getWebinarlable());
            } else {
                ((HomeViewHolder) viewHolder).tv_lable.setVisibility(View.INVISIBLE);
            }


            if (!mList.get(position).getStatus().equalsIgnoreCase("")) {

                if (mList.get(position).getStatus().equalsIgnoreCase(mContext
                        .getResources().getString(R.string.str_webinar_status_register))) {
                    ((HomeViewHolder) viewHolder).webinar_status.setBackgroundResource(R.drawable.rounded_webinar_home);
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
                        .into(((HomeViewHolder) viewHolder).ivwebinar_thumbhel);
            } else {
                ((HomeViewHolder) viewHolder).ivwebinar_thumbhel.setImageResource(R.mipmap.webinar_placeholder);
            }

            if (!mList.get(position).getCpaCredit().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).credit_status.setText(mList.get(position).getCpaCredit());
            }


            if (!mList.get(position).getWebinarType().equalsIgnoreCase("")) {

                String webinar_type = Constant.capitalize(mList.get(position).getWebinarType());
                ((HomeViewHolder) viewHolder).tv_webinar_type.setText(webinar_type);
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

                ((HomeViewHolder) viewHolder).dv_divider.setVisibility(View.VISIBLE);
                ((HomeViewHolder) viewHolder).tv_timezone.setVisibility(View.GONE);


            } else {
                ((HomeViewHolder) viewHolder).tv_webinar_date.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).tv_webinar_time.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).dv_divider.setVisibility(View.INVISIBLE);
                ((HomeViewHolder) viewHolder).tv_timezone.setVisibility(View.GONE);
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


                ((HomeViewHolder) viewHolder).tv_webinar_date.setText(month + " " + day + ", " + year);


            }


            if (mList.get(position).getWebinarLike().equalsIgnoreCase(mContext
                    .getResources().getString(R.string.fav_yes))) {
                ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.mipmap.like_orange);
            } else {
                ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like);
            }

            if (!mList.get(position).getStartTime().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_time.setText(mList.get(position).getStartTime()
                        + " " + mList.get(position).getTimeZone());
            }


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

                    }else
                    {
                        ShowPopUp();
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
                        i.putExtra(mContext.getResources().getString(R.string.screen_detail), 1);
                        i.putExtra(mContext.getResources().getString(R.string.pass_webinar_type), mList
                                .get(position).getWebinarType());

                        mContext.startActivity(i);
                        ((Activity) mContext).finish();
                    } else {
                        ShowPopUp();
                    }


                }
            });


            ((HomeViewHolder) viewHolder).webinar_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!AppSettings.get_login_token(mContext).isEmpty()) {
                        if (mList.get(position).getStatus().equalsIgnoreCase(mContext
                                .getResources().getString(R.string.str_webinar_status_register))) {

                            if (!mList.get(position).getFee().equalsIgnoreCase("")) {
                                if (!mList.get(position).getPaymentlink().equalsIgnoreCase("")) {
                                    Intent i = new Intent(mContext, PaymentActivity.class);
                                    i.putExtra(mContext.getResources().getString(R.string.pass_webinar_id), mList
                                            .get(position).getId());
                                    i.putExtra(mContext.getResources().getString(R.string.str_payment_link), mList
                                            .get(position).getPaymentlink());
                                    i.putExtra(mContext.getResources().getString(R.string.pass_webinar_type), mList
                                            .get(position).getWebinarType());

                                    mContext.startActivity(i);
                                    ((Activity) mContext).finish();


                                } else {
                                    Snackbar.make(((HomeViewHolder) viewHolder).webinar_status, mContext.getResources().getString(R.string.payment_link_not_available), Snackbar.LENGTH_SHORT).show();
                                }


                            } else {
                                if (Constant.isNetworkAvailable(mContext)) {
                                    progressDialog = DialogsUtils.showProgressDialog(mContext, mContext.getResources().getString(R.string.progrees_msg));
                                    RegisterWebinar(mList.get(position).getId(), mList.get(position).getScheduleid(), ((HomeViewHolder) viewHolder).webinar_status, position);
                                } else {
                                    Snackbar.make(((HomeViewHolder) viewHolder).webinar_status, mContext.getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                                }
                            }


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
                            } else if (mList.get(position).getStatus().equalsIgnoreCase(
                                    mContext.getResources().getString(R.string.str_webinar_status_in_progress))) {
                                String url = mList.get(position).getJoinurl();
                                if (!url.equalsIgnoreCase("")) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    mContext.startActivity(i);
                                } else if (!join_url.equalsIgnoreCase("")) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(join_url));
                                    mContext.startActivity(i);
                                }
                            }
                        }
                    } else {
                        ShowPopUp();
                    }


                }
            });

            ((HomeViewHolder) viewHolder).ivfavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppSettings.get_login_token(mContext).isEmpty()) {
                        if (Constant.isNetworkAvailable(mContext)) {
                            ((HomeViewHolder) viewHolder).ivfavorite.setEnabled(false);
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
                ((Activity) mContext).finish();


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


    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_webinar_date, tv_webinar_time,
                tv_favorite_count, tv_attend_views, tv_favorite_speaker_name, tv_company_name, tv_timezone, tv_lable, tv_enrolled,
                tv_rating_number, tv_rating_count;
        ImageView ivwebinar_thumbhel, ivshare, iv_rating;

        View dv_divider;
        Button credit_status, webinar_status, tv_webinar_type, tv_webinar_price_status;
        ImageView ivfavorite;
        RelativeLayout rel_item;


        private HomeViewHolder(View itemView) {
            super(itemView);

            dv_divider = (View) itemView.findViewById(R.id.dv_divider);


            tv_lable = (TextView) itemView.findViewById(R.id.tv_lable);
            tv_enrolled = (TextView) itemView.findViewById(R.id.tv_enrolled);
            tv_rating_number = (TextView) itemView.findViewById(R.id.tv_rating_number);
            tv_rating_count = (TextView) itemView.findViewById(R.id.tv_rating_count);
            iv_rating = (ImageView) itemView.findViewById(R.id.iv_rating);


            ivfavorite = (ImageView) itemView.findViewById(R.id.ivfavorite);
            credit_status = (Button) itemView.findViewById(R.id.credit_status);
            webinar_status = (Button) itemView.findViewById(R.id.webinar_status);
            ivwebinar_thumbhel = (ImageView) itemView.findViewById(R.id.ivwebinar_thumbhel);
            ivshare = (ImageView) itemView.findViewById(R.id.ivshare);

            tv_webinar_price_status = (Button) itemView.findViewById(R.id.tv_webinar_price_status);
            tv_webinar_date = (TextView) itemView.findViewById(R.id.tv_webinar_date);
            tv_webinar_time = (TextView) itemView.findViewById(R.id.tv_webinar_time);

            tv_webinar_type = (Button) itemView.findViewById(R.id.tv_webinar_type);
            tv_favorite_count = (TextView) itemView.findViewById(R.id.tv_favorite_count);
            tv_attend_views = (TextView) itemView.findViewById(R.id.tv_attend_views);
            tv_favorite_speaker_name = (TextView) itemView.findViewById(R.id.tv_speaker_name);
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
                mContext.getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(mContext),
                webinar_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Like_Dislike_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {

                        String message = Constant.GetReturnResponse(mContext, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(ImageView, message, Snackbar.LENGTH_SHORT).show();
                        }
                        ImageView.setEnabled(true);


                    }

                    @Override
                    public void onNext(Webinar_Like_Dislike_Model webinar_like_dislike_model) {


                        if (webinar_like_dislike_model.isSuccess()) {

                            if (webinar_like_dislike_model.getPayload().getIsLike().equalsIgnoreCase(mContext
                                    .getResources().getString(R.string.fav_yes))) {
                                ImageView.setImageResource(R.mipmap.like_orange);
                                textView.setText("" + webinar_like_dislike_model.getPayload().getTotalcount());
                                mList.get(position).setFavWebinarCount(webinar_like_dislike_model.getPayload().getTotalcount());
                                mList.get(position).setWebinarLike(mContext
                                        .getResources().getString(R.string.fav_yes));

                            } else {
                                ImageView.setImageResource(R.drawable.like);
                                textView.setText("" + webinar_like_dislike_model.getPayload().getTotalcount());
                                mList.get(position).setFavWebinarCount(webinar_like_dislike_model.getPayload().getTotalcount());
                                mList.get(position).setWebinarLike(mContext
                                        .getResources().getString(R.string.fav_No));
                            }

                        } else {
                            Snackbar.make(ImageView, webinar_like_dislike_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }

                        ImageView.setEnabled(true);


                    }
                });


    }


    public void RegisterWebinar(int webinar_id, int schedule_id, final Button button, final int position) {

        mAPIService.RegisterWebinar(mContext.getResources().getString(R.string.accept), mContext.getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(mContext), webinar_id, schedule_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelRegisterWebinar>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        assert e != null;
                        if (e instanceof HttpException) {
                            assert button != null;
                            if (((HttpException) e).code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                                //Snackbar.make(button, "Socket Timeout", Snackbar.LENGTH_SHORT).show();
                            } else {
                                String message = Constant.GetReturnResponse(mContext, e);
                                if (Constant.status_code == 401) {
                                    MainActivity.getInstance().AutoLogout();
                                } else {
                                    //  Snackbar.make(button, message, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            String message = Constant.GetReturnResponse(mContext, e);
                            if (Constant.status_code == 401) {
                                MainActivity.getInstance().AutoLogout();
                            } else {
                                // Snackbar.make(button, message, Snackbar.LENGTH_SHORT).show();
                            }
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

                            }


                        } else {
                            Snackbar.make(button, modelRegisterWebinar.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }

                });

    }


}
