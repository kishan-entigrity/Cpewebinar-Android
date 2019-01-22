package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entigrity.R;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        if (!mList.get(position).getSpeakerName().equalsIgnoreCase("")) {
            viewHolder.speakername.setText(mList.get(position).getSpeakerName());
        }


        if (!mList.get(position).getSpeakerImage().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getSpeakerImage())
                    .placeholder(R.mipmap.placeholder)
                    .into(viewHolder.ivinstructorprofile);
        }


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivinstructorprofile;
        TextView speakername;


        private ViewHolder(View itemView) {
            super(itemView);
            ivinstructorprofile = (CircleImageView) itemView.findViewById(R.id.ivinstructorprofile);
            speakername = (TextView) itemView.findViewById(R.id.speakername);


        }
    }
}
