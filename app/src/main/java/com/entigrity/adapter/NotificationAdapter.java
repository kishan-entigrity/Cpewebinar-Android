package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.model.notification.NotificationListItem;
import com.entigrity.utility.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<NotificationListItem> mList;
    LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isLoadingAdded = false;

    public NotificationAdapter(Context mContext, List<NotificationListItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notificationlist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        if (!mList.get(position).getNotificationTitle().equalsIgnoreCase("")) {
            viewHolder.tv_notification_title.setText(mList.get(position).getNotificationTitle());

        }


        if (mList.get(position).getTimestamp() != 0) {

            String notificationdate = getDateCurrentTimeZone(mList.get(position).getTimestamp());
            Constant.Log("notifification date", "date" + notificationdate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                Date pastdate = sdf.parse(notificationdate);
                Date currentdate = new Date();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(currentdate.getTime() - pastdate.getTime());
                long minutes = TimeUnit.MILLISECONDS.toMinutes(currentdate.getTime() - pastdate.getTime());
                long hours = TimeUnit.MILLISECONDS.toHours(currentdate.getTime() - pastdate.getTime());
                long days = TimeUnit.MILLISECONDS.toDays(currentdate.getTime() - pastdate.getTime());


                if (seconds < 60) {
                    viewHolder.tv_notification_time.setText("" + seconds + " seconds ago");
                } else if (minutes < 60) {
                    viewHolder.tv_notification_time.setText("" + minutes + " minutes ago");
                } else if (hours < 24) {
                    viewHolder.tv_notification_time.setText("" + hours + " hours ago");
                } else {
                    viewHolder.tv_notification_time.setText("" + days + " days ago");
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


    }

    public String getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_notification_image;
        TextView tv_notification_title, tv_notification_time;


        private ViewHolder(View itemView) {
            super(itemView);

            iv_notification_image = (ImageView) itemView.findViewById(R.id.iv_notification_image);
            tv_notification_title = (TextView) itemView.findViewById(R.id.tv_notification_title);
            tv_notification_time = (TextView) itemView.findViewById(R.id.tv_notification_time);


        }
    }
}
