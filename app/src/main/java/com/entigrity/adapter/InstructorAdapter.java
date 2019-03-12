package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.activity.InstructorDetailsActivity;
import com.entigrity.model.instructor.SpeakersItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {

    private Context mContext;
    private List<SpeakersItem> mList;
    LayoutInflater mInflater;

    public InstructorAdapter(Context mContext, List<SpeakersItem> mList, Fragment fragment) {
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
    public void onBindViewHolder(@NonNull InstructorAdapter.ViewHolder viewHolder, final int position) {


        if (!mList.get(position).getName().equalsIgnoreCase("") &&
                !mList.get(position).getCompany().equalsIgnoreCase("")) {

            viewHolder.lvall.setVisibility(View.VISIBLE);
            viewHolder.lvone.setVisibility(View.GONE);

            viewHolder.tv_instructorname.setText(mList.get(position).getName());
            viewHolder.tv_companyname.setText(mList.get(position).getCompany());
        } else {

            viewHolder.lvall.setVisibility(View.GONE);
            viewHolder.lvone.setVisibility(View.VISIBLE);

            if (!mList.get(position).getName().equalsIgnoreCase("")) {
                viewHolder.tv_one.setText(mList.get(position).getName());
            } else if (!mList.get(position).getCompany().equalsIgnoreCase("")) {
                viewHolder.tv_one.setText(mList.get(position).getCompany());
            }


        }


        if (!mList.get(position).getLogo().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getLogo())
                    .placeholder(R.mipmap.placeholder)
                    .into(viewHolder.ivinstrctorprofileimage);
        }

        viewHolder.ivinstrctorprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, InstructorDetailsActivity.class);
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_id), mList.get(position).getId());
                mContext.startActivity(i);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setSearchResult(List<SpeakersItem> result) {
        mList = result;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivinstrctorprofileimage;
        TextView tv_instructorname, tv_companyname, tv_one;
        LinearLayout lvall, lvone;


        private ViewHolder(View itemView) {
            super(itemView);
            ivinstrctorprofileimage = (ImageView) itemView.findViewById(R.id.ivinstrctorprofileimage);
            tv_instructorname = (TextView) itemView.findViewById(R.id.tv_instructorname);
            tv_companyname = (TextView) itemView.findViewById(R.id.tv_companyname);


            tv_one = (TextView) itemView.findViewById(R.id.tv_one);
            lvall = (LinearLayout) itemView.findViewById(R.id.lvall);
            lvone = (LinearLayout) itemView.findViewById(R.id.lvone);


        }
    }
}
