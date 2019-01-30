package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.activity.CompanyDetailsActivity;
import com.entigrity.model.company.CompanyItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    private Context mContext;
    private List<CompanyItem> mList;
    LayoutInflater mInflater;

    public CompanyAdapter(Context mContext, List<CompanyItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_companylist, viewGroup, false);
        return new CompanyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder viewHolder, final int position) {


        if (!mList.get(position).getName().equalsIgnoreCase("")) {
            viewHolder.tv_companyname.setText(mList.get(position).getName());
        }


        if (!mList.get(position).getLogo().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getLogo())
                    .placeholder(R.mipmap.placeholder)
                    .into(viewHolder.ivcompanylogo);
        }


        viewHolder.ivcompanylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, CompanyDetailsActivity.class);
                i.putExtra(mContext.getResources().getString(R.string.pass_company_id), mList.get(position).getId());
              /*  i.putExtra(mContext.getResources().getString(R.string.pass_company_name), mList.get(position).getName());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_website), mList.get(position).getWebsite());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_contact_number), mList.get(position).getContactNumber());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_logo), mList.get(position).getLogo());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_description), mList.get(position).getDescription());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_favorite_status), mList.get(position).getFavouriteunfavoritestatus());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_number_of_speaker), mList.get(position).getNumberOfSpeaker());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_number_of_webinar), mList.get(position).getNumberOfWebinar());*/

                mContext.startActivity(i);

            }
        });


    }

    public void setSearchResult(List<CompanyItem> result) {
        mList = result;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivcompanylogo;
        TextView tv_companyname;


        private ViewHolder(View itemView) {
            super(itemView);
            ivcompanylogo = (ImageView) itemView.findViewById(R.id.ivcompanylogo);
            tv_companyname = (TextView) itemView.findViewById(R.id.tv_companyname);


        }
    }
}
