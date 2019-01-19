package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.model.webinarfavorites.MyFavoriteWebinarItem;

import java.util.List;

public class WebinarFavoriteAdapter extends RecyclerView.Adapter<WebinarFavoriteAdapter.ViewHolder> {

    private Context mContext;
    private List<MyFavoriteWebinarItem> mList;
    LayoutInflater mInflater;

    public WebinarFavoriteAdapter(Context mContext, List<MyFavoriteWebinarItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public WebinarFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_webinar_favorite, viewGroup, false);
        return new WebinarFavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {



    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {




        private ViewHolder(View itemView) {
            super(itemView);


        }
    }
}
