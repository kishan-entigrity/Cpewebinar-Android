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
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<NotificationListItem> mList;
    LayoutInflater mInflater;

    public NotificationAdapter(Context mContext, List<NotificationListItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notificationlist, viewGroup, false);
        return new NotificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, final int position) {

        if (!mList.get(position).getNotificationTitle().equalsIgnoreCase("")) {
            viewHolder.tv_notification_title.setText(mList.get(position).getNotificationTitle());

        }

        if (!mList.get(position).getImage().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getImage())
                    .placeholder(R.mipmap.placeholder)
                    .into(viewHolder.iv_notification_image);
        }

        if (mList.get(position).getTimestamp() != 0) {

            viewHolder.tv_notification_time.setText(""+mList.get(position)
                    .getTimestamp());


        }


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
