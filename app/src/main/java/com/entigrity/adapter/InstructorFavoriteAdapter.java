package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.activity.InstructorDetailsActivity;
import com.entigrity.model.instructorfavorites.MyFavoriteSpeakerItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstructorFavoriteAdapter extends RecyclerView.Adapter<InstructorFavoriteAdapter.ViewHolder> {

    private Context mContext;
    private List<MyFavoriteSpeakerItem> mList;
    LayoutInflater mInflater;

    public InstructorFavoriteAdapter(Context mContext, List<MyFavoriteSpeakerItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public InstructorFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_instructor_favorite, viewGroup, false);
        return new InstructorFavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {


        if (!mList.get(position).getSpeakerName().equalsIgnoreCase("")) {
            viewHolder.speakername.setText(mList.get(position).getSpeakerName());
        }


        if (!mList.get(position).getSpeakerImage().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getSpeakerImage())
                    .placeholder(R.mipmap.placeholder)
                    .into(viewHolder.ivinstructorprofile);
        }


        viewHolder.relviewnavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, InstructorDetailsActivity.class);
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_id), mList.get(position).getSpeakerId());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_name), mList.get(position).getSpeakerName());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_email), mList.get(position).getEmail());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_contact_number), mList.get(position).getSpeakerMobileNo());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_logo), mList.get(position).getLogo());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_expiritise), mList.get(position).getAreaOfExpertise());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_about_speaker), mList.get(position).getAboutSpeaker());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_company), mList.get(position).getCompanyName());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_state), mList.get(position).getState());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_city), mList.get(position).getCity());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_followers), mList.get(position).getNoOfFollowersCount());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_favorite_unfavorite_status), mList.get(position).getFavoriteUnfavoriteStatus());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_follow_unfollow_status), mList.get(position).getFollowUnfollowStatus());
                mContext.startActivity(i);


            }
        });


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivinstructorprofile;
        TextView speakername;
        RelativeLayout relviewnavigate;


        private ViewHolder(View itemView) {
            super(itemView);
            ivinstructorprofile = (CircleImageView) itemView.findViewById(R.id.ivinstructorprofile);
            speakername = (TextView) itemView.findViewById(R.id.speakername);
            relviewnavigate = (RelativeLayout) itemView.findViewById(R.id.relviewnavigate);


        }
    }
}
