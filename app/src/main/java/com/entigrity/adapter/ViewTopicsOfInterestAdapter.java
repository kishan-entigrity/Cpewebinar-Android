package com.entigrity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.R;


import com.entigrity.model.view_topics_of_interest.TagsItem;
import com.entigrity.model.view_topics_of_interest.TopicOfInterestsItem;
import com.entigrity.view.SectionedRecyclerViewAdapter;

import java.util.List;


public class ViewTopicsOfInterestAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<TopicOfInterestsItem> mlist;
    public static Context mContext;


    public ViewTopicsOfInterestAdapter(Context context, List<TopicOfInterestsItem> data) {
        this.mContext = context;
        this.mlist = data;
    }


    @Override
    public int getSectionCount() {
        return mlist.size();
    }

    @Override
    public int getItemCount(int position) {
        return mlist.get(position).getTags().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (!mlist.get(position).getName().equalsIgnoreCase("")) {
            String sectionName = mlist.get(position).getName();
            if (!sectionName.equalsIgnoreCase("")) {
                ((SectionViewHolder) holder).sectionTitle.setText(sectionName);
            }


        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position, int relativePosition, int absolutePosition) {


        if (mlist.get(position).getTags().size() > 0) {
            List<TagsItem> itemsInSection = mlist.get(position).getTags();

            String itemName = itemsInSection.get(relativePosition).getName();


            if (!itemName.equalsIgnoreCase("")) {
                ((ItemViewHolder) holder).itemTitle.setText(itemName);
            }
        }


        // Try to put a image . for sample i set background color in xml layout file
        // itemViewHolder.itemImage.setBackgroundColor(Color.parseColor("#01579b"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {
        View v = null;
        if (header) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_section, parent, false);
            return new SectionViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_view_topicsofinterest, parent, false);
            return new ItemViewHolder(v);
        }

    }


    // SectionViewHolder Class for Sections
    public static class SectionViewHolder extends RecyclerView.ViewHolder {


        final TextView sectionTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.sectionTitle);


        }
    }

    // ItemViewHolder Class for Items in each Section
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView itemTitle;
        RelativeLayout lv_topics_item;


        public ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            lv_topics_item = (RelativeLayout) itemView.findViewById(R.id.lv_topics_item);


        }
    }
}