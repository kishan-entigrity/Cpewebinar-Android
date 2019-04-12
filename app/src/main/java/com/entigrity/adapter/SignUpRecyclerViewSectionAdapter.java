package com.entigrity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.model.SaveTopicsSignUpModel;
import com.entigrity.model.topicsofinterestn.TagsItem;
import com.entigrity.model.topicsofinterestn.TopicOfInterestsItem;
import com.entigrity.utility.Constant;
import com.entigrity.view.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class SignUpRecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<TopicOfInterestsItem> mlist;
    public static Context mContext;
    public ArrayList<SaveTopicsSignUpModel> arraylistselectedtopicsofinterest = new ArrayList<SaveTopicsSignUpModel>();
    List<TagsItem> itemsInSection;
    SaveTopicsSignUpModel saveTopicsSignUpModel;
    public ArrayList<Boolean> topicsofinterestchecked = new ArrayList<>();
    public int positionselected = 0;


    public SignUpRecyclerViewSectionAdapter(Context context, List<TopicOfInterestsItem> data, ArrayList<SaveTopicsSignUpModel> arraylistselectedtopicsofinterest,
                                            ArrayList<Boolean> topicsofinterestchecked) {
        this.mContext = context;
        this.mlist = data;
        this.arraylistselectedtopicsofinterest = arraylistselectedtopicsofinterest;
        this.topicsofinterestchecked = topicsofinterestchecked;
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

            ((ItemViewHolder) holder).cbselection.setTag(mlist.get(position).getTags().get(relativePosition));

            String itemName = itemsInSection.get(relativePosition).getName();


            if (!itemName.equalsIgnoreCase("")) {
                ((ItemViewHolder) holder).itemTitle.setText(itemName);
            }


            if (topicsofinterestchecked.get(relativePosition) == true) {
                ((ItemViewHolder) holder).cbselection.setChecked(true);
            } else {
                ((ItemViewHolder) holder).cbselection.setChecked(false);
            }


        }


        ((ItemViewHolder) holder).lv_topics_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (((ItemViewHolder) holder).cbselection.isChecked() == false) {

                        saveTopicsSignUpModel = new SaveTopicsSignUpModel();
                        saveTopicsSignUpModel.setId(mlist.get(position).getTags().get(relativePosition).getId());
                        saveTopicsSignUpModel.setIschecked(true);
                        saveTopicsSignUpModel.setTopicsofinterest(mlist.get(position).getTags().get(relativePosition).getName());
                        arraylistselectedtopicsofinterest.add(saveTopicsSignUpModel);
                        ((ItemViewHolder) holder).cbselection.setChecked(true);

                        int id = mlist.get(position).getTags().get(relativePosition).getId();

                        for (int i = 0; i < mlist.size(); i++) {
                            for (int j = 0; j < mlist.get(i).getTags().size(); j++) {
                                if (id == mlist.get(i).getTags().get(j).getId()) {
                                    positionselected = j;
                                }
                            }

                        }


                      /*  positionselected = mlist.indexOf(mlist.get(position)
                                .getId());

                        Constant.Log("postion", "+++" + positionselected);*/
                        Constant.Log("postion", "+++" + positionselected);
                        // topicsofinterestchecked.set(positionselected, true);
                    } else {
                        ((ItemViewHolder) holder).cbselection.setChecked(false);
                        arraylistselectedtopicsofinterest.remove(relativePosition);
                        topicsofinterestchecked.set(positionselected, false);
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
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            lv_topics_item = (RelativeLayout) itemView.findViewById(R.id.lv_topics_item);
            cbselection = (CheckBox) itemView.findViewById(R.id.cbselection);


        }
    }
}