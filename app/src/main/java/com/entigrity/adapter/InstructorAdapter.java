package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.activity.InstructorDetailsActivity;
import com.entigrity.model.instructor.SpeakersItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {

    private Context mContext;
    private List<SpeakersItem> mList;
    private List<SpeakersItem> mListFiltered;
    LayoutInflater mInflater;

    public InstructorAdapter(Context mContext, List<SpeakersItem> mList) {
        this.mContext = mContext;
        this.mListFiltered = mList;
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

        viewHolder.ivinstrctorprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, InstructorDetailsActivity.class);
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_id), mList.get(position).getId());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_name), mList.get(position).getName());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_email), mList.get(position).getEmail());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_contact_number), mList.get(position).getContactNo());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_logo), mList.get(position).getLogo());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_expiritise), mList.get(position).getExpertise());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_about_speaker), mList.get(position).getAboutSpeaker());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_company), mList.get(position).getCompany());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_state), mList.get(position).getState());
                i.putExtra(mContext.getResources().getString(R.string.pass_inst_city), mList.get(position).getCity());
                mContext.startActivity(i);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mListFiltered = mList;
                } else {
                    List<SpeakersItem> filteredList = new ArrayList<>();
                    for (SpeakersItem row : mList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCompany().equalsIgnoreCase(charString)) {
                            filteredList.add(row);
                        }
                    }

                    mListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListFiltered = (ArrayList<SpeakersItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
