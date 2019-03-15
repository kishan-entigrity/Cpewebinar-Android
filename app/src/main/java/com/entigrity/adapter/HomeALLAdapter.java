package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.entigrity.model.homewebinarlist.WebinarItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

public class HomeALLAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isLoadingAdded = false;
    private Context mContext;
    LayoutInflater mInflater;
    private List<WebinarItem> mList;
    private String start_time;
    public boolean checkfavoritestate = false;


    public HomeALLAdapter(Context mContext, List<WebinarItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
                    R.layout.row_webinar_favorite, parent, false);

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

            if (!mList.get(position).getWebinarStatus().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).webinar_status.setText(mList.get(position).getWebinarStatus());
            }


            if (!mList.get(position).getFee().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_price_status.setText("$" + mList.get(position).getFee());

            }

            if (!mList.get(position).getWebinarThumbnailImage().equalsIgnoreCase("")) {
                Picasso.with(mContext).load(mList.get(position).getWebinarThumbnailImage())
                        .placeholder(R.mipmap.webinar_placeholder)
                        .into(((HomeViewHolder) viewHolder).ivwebinar_thumbhel);
            }

            if (!mList.get(position).getCpaCredit().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).credit_status.setText(mList.get(position).getCpaCredit());
            }


            if (!mList.get(position).getWebinaType().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_type.setText(mList.get(position).getWebinaType());
            }

            if (!mList.get(position).getSpeakerName().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_favorite_speaker_name.setText(mList.get(position).getSpeakerName());
            }

            if (!mList.get(position).getCompanyName().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_company_name.setText(mList.get(position).getCompanyName());
            }


            if (!mList.get(position).getDuration().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_duration_name.setText(mList.get(position).getDuration() + " " + mContext.getResources().getString(R.string.str_fav_duration));

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


            if (!mList.get(position).getRecordedDate().equalsIgnoreCase("")) {


                StringTokenizer tokens = new StringTokenizer(mList.get(position).getRecordedDate(), "-");
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

            if (mList.get(position).getWebinarLike().equalsIgnoreCase(mContext
                    .getResources().getString(R.string.Yes))) {
                checkfavoritestate = false;
                ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like_hover);
            } else {
                checkfavoritestate = true;
                ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like);
            }


            ((HomeViewHolder) viewHolder).ivshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = mList.get(position).getWebinarShareLink();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });


            ((HomeViewHolder) viewHolder).rel_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, WebinarDetailsActivity.class);
                    mContext.startActivity(i);

                }
            });


            ((HomeViewHolder) viewHolder).ivfavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkfavoritestate == false) {
                        checkfavoritestate = true;
                        ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like);
                    } else {
                        checkfavoritestate = false;
                        ((HomeViewHolder) viewHolder).ivfavorite.setImageResource(R.drawable.like_hover);
                    }


                }
            });


            if (!mList.get(position).getStartTime().equalsIgnoreCase("")) {
                ((HomeViewHolder) viewHolder).tv_webinar_time.setVisibility(View.VISIBLE);

                StringTokenizer tokens = new StringTokenizer(mList.get(position).getStartTime(), " ");
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
            }

        } else {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }

    }

    public void add(WebinarItem webinarItem) {
        mList.add(webinarItem);
        notifyItemInserted(mList.size() - 1);
    }

    public void addAll(List<WebinarItem> mcList) {
        for (WebinarItem mc : mcList) {
            add(mc);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new WebinarItem());
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
                tv_webinar_type, tv_favorite_count, tv_attend_views, tv_favorite_speaker_name, tv_company_name;
        ImageView ivwebinar_thumbhel, ivshare;
        Button credit_status, webinar_status;
        ImageView ivfavorite;
        RelativeLayout rel_item;


        private HomeViewHolder(View itemView) {
            super(itemView);

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
            tv_webinar_type = (TextView) itemView.findViewById(R.id.tv_webinar_type);
            tv_favorite_count = (TextView) itemView.findViewById(R.id.tv_favorite_count);
            tv_attend_views = (TextView) itemView.findViewById(R.id.tv_attend_views);
            tv_favorite_speaker_name = (TextView) itemView.findViewById(R.id.tv_favorite_speaker_name);
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
}
