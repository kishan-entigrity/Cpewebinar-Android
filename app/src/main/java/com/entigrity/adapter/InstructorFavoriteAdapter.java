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
import com.entigrity.model.instructorfavorites.MyFavoriteSpeakerItem;

import java.util.List;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.speakername.setText(mList.get(i).getSpeakerName());

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivinstructorprofile;
        TextView speakername;


        private ViewHolder(View itemView) {
            super(itemView);
            ivinstructorprofile = (ImageView) itemView.findViewById(R.id.ivinstructorprofile);
            speakername = (TextView) itemView.findViewById(R.id.speakername);


        }
    }
}
