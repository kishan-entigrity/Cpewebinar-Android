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
import com.entigrity.model.companyfavorites.MyFavoriteCompanyItem;

import java.util.List;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        viewHolder.companyname.setText(mList.get(position).getCompanyName());


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView ivcompanylogo;
        TextView companyname;


        private ViewHolder(View itemView) {
            super(itemView);

            ivcompanylogo = (ImageView) itemView.findViewById(R.id.ivcompanylogo);
            companyname = (TextView) itemView.findViewById(R.id.companyname);


        }
    }
}
