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
import com.entigrity.model.instructor.SpeakersItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {

    private Context mContext;
    private List<SpeakersItem> mList;
    LayoutInflater mInflater;

    public InstructorAdapter(Context mContext, List<SpeakersItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public InstructorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_instructorlist, viewGroup, false);
        return new InstructorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorAdapter.ViewHolder viewHolder, int position) {


        if (!mList.get(position).getName().equalsIgnoreCase("")) {
            viewHolder.tv_instructorname.setText(mList.get(position).getName());
        }

        if (!mList.get(position).getCompany().equalsIgnoreCase("")) {
            viewHolder.tv_companyname.setText(mList.get(position).getCompany());
        }


        if (!mList.get(position).getLogo().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getLogo())
                    .placeholder(R.mipmap.placeholder)
                    .into(viewHolder.ivinstrctorprofileimage);
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivinstrctorprofileimage;
        TextView tv_instructorname, tv_companyname;


        private ViewHolder(View itemView) {
            super(itemView);
            ivinstrctorprofileimage = (ImageView) itemView.findViewById(R.id.ivinstrctorprofileimage);
            tv_instructorname = (TextView) itemView.findViewById(R.id.tv_instructorname);
            tv_companyname = (TextView) itemView.findViewById(R.id.tv_companyname);


        }
    }
}
