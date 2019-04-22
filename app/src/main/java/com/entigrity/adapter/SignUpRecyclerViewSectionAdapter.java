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

import static com.entigrity.utility.Constant.setselected;


public class SignUpRecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<TopicOfInterestsItem> mlist;
    public static Context mContext;
    public ArrayList<SaveTopicsSignUpModel> arraylistselectedtopicsofinterest = new ArrayList<SaveTopicsSignUpModel>();
    List<TagsItem> itemsInSection;
    SaveTopicsSignUpModel saveTopicsSignUpModel;


    public SignUpRecyclerViewSectionAdapter(Context context, List<TopicOfInterestsItem> data, ArrayList<SaveTopicsSignUpModel> arraylistselected) {
        this.mContext = context;
        this.arraylistselectedtopicsofinterest = arraylistselectedtopicsofinterest;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, final int relativePosition, final int absolutePosition) {


        if (mlist.get(position).getTags().size() > 0) {
            itemsInSection = mlist.get(position).getTags();


            String itemName = itemsInSection.get(relativePosition).getName();


            if (!itemName.equalsIgnoreCase("")) {
                ((ItemViewHolder) holder).itemTitle.setText(itemName);
            }

            if (Constant.setselected.size() > 0) {

                Boolean checkvalue = Constant.setselected.get(mlist.get(position).getTags().get(relativePosition).getName());

                int positions = mlist.indexOf(mlist.get(position).getTags().get(relativePosition).getName());

                Constant.Log("positions", "positions" + positions);
                saveTopicsSignUpModel = new SaveTopicsSignUpModel();
                if (checkvalue) {
                    ((ItemViewHolder) holder).cbselection.setChecked(true);
                    saveTopicsSignUpModel.setId(mlist.get(position).getTags().get(relativePosition).getId());
                    saveTopicsSignUpModel.setIschecked(checkvalue);
                    saveTopicsSignUpModel.setTopicsofinterest(mlist.get(position).getTags().get(relativePosition).getName());
                    arraylistselectedtopicsofinterest.set(positions, saveTopicsSignUpModel);
                } else {
                    saveTopicsSignUpModel.setId(mlist.get(position).getTags().get(relativePosition).getId());
                    saveTopicsSignUpModel.setIschecked(checkvalue);
                    saveTopicsSignUpModel.setTopicsofinterest(mlist.get(position).getTags().get(relativePosition).getName());
                    arraylistselectedtopicsofinterest.set(positions, saveTopicsSignUpModel);
                }


            }


        }


        ((ItemViewHolder) holder).lv_topics_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (((ItemViewHolder) holder).cbselection.isChecked() == false) {
                        int positions = mlist.indexOf(mlist.get(position).getTags().get(relativePosition).getName());
                        saveTopicsSignUpModel = new SaveTopicsSignUpModel();
                        saveTopicsSignUpModel.setId(mlist.get(position).getTags().get(relativePosition).getId());
                        saveTopicsSignUpModel.setIschecked(true);
                        saveTopicsSignUpModel.setTopicsofinterest(mlist.get(position).getTags().get(relativePosition).getName());
                        arraylistselectedtopicsofinterest.set(positions, saveTopicsSignUpModel);
                        setselected.put(mlist.get(position).getTags().get(relativePosition).getName(), true);
                        ((ItemViewHolder) holder).cbselection.setChecked(true);
                        System.out.println(setselected);

                    } else {
                        setselected.put(mlist.get(position).getTags().get(relativePosition).getName(), false);
                        ((ItemViewHolder) holder).cbselection.setChecked(false);

                       /* boolean position_new = arraylistselectedtopicsofinterest.get(position).isIschecked();
                        System.out.println(position_new);*/
                        //Boolean checkvalue = Constant.setselected.get(mlist.get(position).getTags().get(relativePosition).getName());


                        arraylistselectedtopicsofinterest.remove(Constant.setselected.get(mlist.get(position).getTags().get(relativePosition).getName()));


                      /*  saveTopicsSignUpModel = new SaveTopicsSignUpModel();
                        saveTopicsSignUpModel.setId(mlist.get(position).getTags().get(relativePosition).getId());
                        saveTopicsSignUpModel.setIschecked(false);
                        saveTopicsSignUpModel.setTopicsofinterest(mlist.get(position).getTags().get(relativePosition).getName());
                        arraylistselectedtopicsofinterest.add(saveTopicsSignUpModel);*/



                        /*int position_new = arraylistselectedtopicsofinterest.get(position).getId();
                        System.out.println(position_new);
                        arraylistselectedtopicsofinterest.remove(position_new);*/
                    }

                } catch (Exception e) {
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