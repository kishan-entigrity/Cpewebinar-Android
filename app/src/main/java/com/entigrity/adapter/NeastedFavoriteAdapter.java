package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entigrity.R;

import java.util.List;

public class NeastedFavoriteAdapter extends RecyclerView.Adapter<NeastedFavoriteAdapter.ViewHolder> {

    private Context mContext;
    List<com.entigrity.model.view_interest_favorite.TagsItem> itemsInSection;
    LayoutInflater mInflater;


    public NeastedFavoriteAdapter(Context mContext, List<com.entigrity.model.view_interest_favorite.TagsItem> mList) {
        this.mContext = mContext;
        this.itemsInSection = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_topicsofinterest, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.itemTitle.setText(itemsInSection.get(position).getName());


    }


    @Override
    public int getItemCount() {
        return itemsInSection.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        LinearLayout lv_topics_item;


        private ViewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            lv_topics_item = (LinearLayout) itemView.findViewById(R.id.lv_topics_item);


        }
    }
}
