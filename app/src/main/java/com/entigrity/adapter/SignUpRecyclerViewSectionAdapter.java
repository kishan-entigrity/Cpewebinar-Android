package com.entigrity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.model.topicsofinterestn.TagsItem;
import com.entigrity.model.topicsofinterestn.TopicOfInterestsItem;
import com.entigrity.utility.Constant;
import com.entigrity.view.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class SignUpRecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<TopicOfInterestsItem> mlist;
    public static Context mContext;
    List<TagsItem> itemsInSection;
    List<Boolean> checkstate = new ArrayList<Boolean>();


    public SignUpRecyclerViewSectionAdapter(Context context, List<TopicOfInterestsItem> data) {
        this.mContext = context;
        this.mlist = data;


        for (int i = 0; i < mlist.size(); i++) {
            for (int k = 0; k < mlist.get(i).getTags().size(); k++) {
                checkstate.add(false);
            }

        }
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, final int relativePosition, final int absolutePosition) {


        if (mlist.get(position).getTags().size() > 0) {
            itemsInSection = mlist.get(position).getTags();

            for (int i = 0; i < mlist.size(); i++) {
                for (int k = 0; k < mlist.get(i).getTags().size(); k++) {
                    ((ItemViewHolder) holder).cbselection.setChecked(checkstate.get(k));

                }

            }


            String itemName = itemsInSection.get(relativePosition).getName();


            if (!itemName.equalsIgnoreCase("")) {
                ((ItemViewHolder) holder).itemTitle.setText(itemName);
            }

            ((ItemViewHolder) holder).cbselection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        checkstate.set(position, true);
                    } else {
                        checkstate.set(position, false);
                    }

                }
            });


        }


        ((ItemViewHolder) holder).lv_topics_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (((ItemViewHolder) holder).cbselection.isChecked() == false) {
                        ((ItemViewHolder) holder).cbselection.setChecked(true);
                        Constant.arraylistselectedvalue.add(mlist.get(position).getTags().get(relativePosition).getId());
                    } else {
                        ((ItemViewHolder) holder).cbselection.setChecked(false);
                        Constant.arraylistselectedvalue.remove(mlist.get(position).getTags().get(relativePosition).getId());
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }
        });


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
                    .inflate(R.layout.list_item, parent, false);
            return new ItemViewHolder(v);
        }

    }


    // SectionViewHolder Class for Sections
    public static class SectionViewHolder extends RecyclerView.ViewHolder {


        final TextView sectionTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);

            sectionTitle = (TextView) itemView.findViewById(R.id.sectionTitle);


        }
    }

    // ItemViewHolder Class for Items in each Section
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView itemTitle;
        RelativeLayout lv_topics_item;
        CheckBox cbselection;


        public ItemViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            lv_topics_item = (RelativeLayout) itemView.findViewById(R.id.lv_topics_item);
            cbselection = (CheckBox) itemView.findViewById(R.id.cbselection);


        }
    }
}