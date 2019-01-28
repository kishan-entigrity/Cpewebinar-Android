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
import com.entigrity.activity.CompanyDetailsActivity;
import com.entigrity.activity.InstructorDetailsActivity;
import com.entigrity.model.companyfavorites.MyFavoriteCompanyItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyFavoriteAdapter extends RecyclerView.Adapter<CompanyFavoriteAdapter.ViewHolder> {

    private Context mContext;
    private List<MyFavoriteCompanyItem> mList;
    LayoutInflater mInflater;

    public CompanyFavoriteAdapter(Context mContext, List<MyFavoriteCompanyItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public CompanyFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_company_favorite, viewGroup, false);
        return new CompanyFavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {


        if (!mList.get(position).getCompanyName().equalsIgnoreCase("")) {
            viewHolder.companyname.setText(mList.get(position).getCompanyName());
        }

        if (!mList.get(position).getLogo().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(mList.get(position).getLogo())
                    .placeholder(R.mipmap.placeholder)
                    .into(viewHolder.ivcompanylogo);
        }


        viewHolder.relviewnavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, CompanyDetailsActivity.class);
                i.putExtra(mContext.getResources().getString(R.string.pass_company_id), mList.get(position).getCompanyId());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_name), mList.get(position).getCompanyName());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_website), mList.get(position).getCompanyWebsite());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_contact_number), mList.get(position).getCompanyMobile());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_logo), mList.get(position).getLogo());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_description), mList.get(position).getAboutCompany());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_favorite_status), mList.get(position).getFavoriteUnfavoriteStatus());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_number_of_speaker), mList.get(position).getNoOfSpeaker());
                i.putExtra(mContext.getResources().getString(R.string.pass_company_number_of_webinar), mList.get(position).getNoOfWebinar());
                mContext.startActivity(i);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView ivcompanylogo;
        TextView companyname;
        RelativeLayout relviewnavigate;


        private ViewHolder(View itemView) {
            super(itemView);

            ivcompanylogo = (CircleImageView) itemView.findViewById(R.id.ivcompanylogo);
            companyname = (TextView) itemView.findViewById(R.id.companyname);
            relviewnavigate = (RelativeLayout) itemView.findViewById(R.id.relviewnavigate);


        }
    }
}
