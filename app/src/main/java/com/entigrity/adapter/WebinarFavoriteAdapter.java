package com.entigrity.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.model.webinar_like.Webinar_Like_Model;
import com.entigrity.model.webinarfavorites.MyFavoriteWebinarItem;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WebinarFavoriteAdapter extends RecyclerView.Adapter<WebinarFavoriteAdapter.ViewHolder> {

    private Context mContext;
    private List<MyFavoriteWebinarItem> mList;
    ProgressDialog progressDialog;
    private APIService mAPIService;
    LayoutInflater mInflater;

    public WebinarFavoriteAdapter(Context mContext, List<MyFavoriteWebinarItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mAPIService = ApiUtils.getAPIService();
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public WebinarFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_webinar_favorite, viewGroup, false);
        return new WebinarFavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        if (!mList.get(position).getWebinatTitle().equalsIgnoreCase("")) {
            viewHolder.tv_webinar_title.setText(mList.get(position).getWebinatTitle());
        }

        if (!mList.get(position).getWebinarDurationMin().equalsIgnoreCase("")) {
            viewHolder.tv_duration_name.setText(mList.get(position).getWebinarDurationMin() + " MIN");
        }


        if (!mList.get(position).getFee().equalsIgnoreCase("")) {

            if (!mList.get(position).getFee().equalsIgnoreCase("Free")) {
                viewHolder.btnPrice.setText("$" + mList.get(position).getFee());
            } else {
                viewHolder.btnPrice.setText(mList.get(position).getFee());

            }
        }


        if (!mList.get(position).getCreditNo().equalsIgnoreCase("")) {
            viewHolder.tv_cpe_credit.setText(mList.get(position).getCreditNo() + "  " + mContext.getResources().getString(R.string.cpe));
        }


        if (!mList.get(position).getSpeakerName().equalsIgnoreCase("")) {
            viewHolder.tv_favorite_speaker_name.setText(mList.get(position).getSpeakerName());
        }


        if (!mList.get(position).getCompanyName().equalsIgnoreCase("")) {
            viewHolder.tv_company_name.setText(mList.get(position).getCompanyName());
        }


        if (!mList.get(position).getViewNumber().equalsIgnoreCase("")) {
            viewHolder.tv_favorite_views.setText(mList.get(position).getViewNumber());
        }


        if (!mList.get(position).getThumbImage().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getThumbImage())
                    .placeholder(R.mipmap.webinar_placeholder)
                    .into(viewHolder.ivwebinar_thumbhel);
        }


        if (!mList.get(position).getDate().equalsIgnoreCase("")) {


            StringTokenizer tokens = new StringTokenizer(mList.get(position).getDate(), "/");
            String day = tokens.nextToken();// this will contain day
            String month = tokens.nextToken();//this will contain month
            String year = tokens.nextToken();//this will contain year

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


            viewHolder.tv_webinar_date.setText(day + " " + month + ", " + year);


        }


        if (!mList.get(position).getTime().equalsIgnoreCase("")) {

            StringTokenizer tokens = new StringTokenizer(mList.get(position).getTime(), "+");
            String time = tokens.nextToken();// this will contain day
            String timezone = tokens.nextToken();//this will contain month


            viewHolder.tv_webinar_time.setText(time + " " + timezone);

        }


        viewHolder.ivfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Constant.isNetworkAvailable(mContext)) {
                    // progressDialog = DialogsUtils.showProgressDialog(mContext, mContext.getResources().getString(R.string.progrees_msg));
                    viewHolder.ivfavorite.setEnabled(false);
                    WebinarFavoriteStatus(mList.get(position).getWebinatId(), viewHolder.ivfavorite);
                } else {
                    Constant.ShowPopUp(mContext.getResources().getString(R.string.please_check_internet_condition), mContext);
                }

            }
        });


    }

    private void WebinarFavoriteStatus(final int webinarid, final CheckBox checkBox) {

        mAPIService.WebinarFavoriteStatus(String.valueOf(webinarid), mContext.getResources().getString(R.string.bearer) + AppSettings.get_login_token(mContext), webinarid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Like_Model>() {
                    @Override
                    public void onCompleted() {

                        checkBox.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                       /* if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }*/

                        String message = Constant.GetReturnResponse(mContext, e);
                        Constant.ShowPopUp(message, mContext);


                    }

                    @Override
                    public void onNext(Webinar_Like_Model webinar_like_model) {

                        /*if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
*/

                        if (webinar_like_model.isSuccess()) {
                            if (webinar_like_model.getMessage().equalsIgnoreCase(mContext.getResources().getString(R.string.str_webinar_like))) {
                                checkBox.setChecked(true);
                            } else {
                                checkBox.setChecked(false);
                            }


                            // Constant.ShowPopUp(webinar_like_model.getMessage(), mContext);


                        } else {
                            if (webinar_like_model.getPayload().getAccessToken() != null && !webinar_like_model.getPayload().getAccessToken()
                                    .equalsIgnoreCase("")) {

                                if (Constant.isNetworkAvailable(mContext)) {
                                    AppSettings.set_login_token(mContext, webinar_like_model.getPayload().getAccessToken());
                                    WebinarFavoriteStatus(webinarid, checkBox);
                                } else {
                                    Constant.ShowPopUp(mContext.getResources().getString(R.string.please_check_internet_condition), mContext);
                                }

                            } else {
                                Constant.ShowPopUp(webinar_like_model.getMessage(), mContext);
                            }

                        }


                    }
                });


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_webinar_title, tv_duration_name, tv_webinar_date, tv_webinar_time, tv_cpe_credit, tv_favorite_views,
                tv_favorite_speaker_name, tv_company_name;

        ImageView ivwebinar_thumbhel;


        CheckBox ivfavorite;


        Button btnPrice;


        private ViewHolder(View itemView) {
            super(itemView);
            tv_webinar_title = (TextView) itemView.findViewById(R.id.tv_webinar_title);
            tv_duration_name = (TextView) itemView.findViewById(R.id.tv_duration_name);
            tv_webinar_date = (TextView) itemView.findViewById(R.id.tv_webinar_date);
            tv_webinar_time = (TextView) itemView.findViewById(R.id.tv_webinar_time);
            tv_cpe_credit = (TextView) itemView.findViewById(R.id.tv_cpe_credit);
            tv_favorite_views = (TextView) itemView.findViewById(R.id.tv_favorite_views);
            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            tv_favorite_speaker_name = (TextView) itemView.findViewById(R.id.tv_favorite_speaker_name);
            ivwebinar_thumbhel = (ImageView) itemView.findViewById(R.id.ivwebinar_thumbhel);
            ivfavorite = (CheckBox) itemView.findViewById(R.id.ivfavorite);
            btnPrice = (Button) itemView.findViewById(R.id.btnPrice);


        }
    }
}
