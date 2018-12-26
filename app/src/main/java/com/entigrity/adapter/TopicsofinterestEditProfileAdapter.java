package com.entigrity.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.model.editProfile.Topicsofinterestmodel;
import com.entigrity.model.topicsofinterest.TagsItem;

import java.util.ArrayList;

public class TopicsofinterestEditProfileAdapter extends RecyclerView.Adapter<TopicsofinterestEditProfileAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TagsItem> mList;
    LayoutInflater mInflater;
    public ArrayList<Integer> arraylistselectedtag = new ArrayList<Integer>();
    ArrayList<Topicsofinterestmodel> saveselectedlist = new ArrayList<Topicsofinterestmodel>();


    public TopicsofinterestEditProfileAdapter(Context mContext, ArrayList<TagsItem> mList, ArrayList<Topicsofinterestmodel> saveselectedlist) {
        this.mContext = mContext;
        this.mList = mList;
        this.saveselectedlist = saveselectedlist;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topics_of_interset, parent, false);
        ViewHolder v1 = new ViewHolder(v);
        return v1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_topics.setText(mList.get(position).getTag());


        if (mList.get(position).getTag().equals(saveselectedlist.get(position).getTopicsselected())) {
            holder.cbselection.setChecked(true);
            arraylistselectedtag.add(mList.get(position).getId());
        } else {
            holder.cbselection.setChecked(false);
        }


        holder.tv_topics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (holder.cbselection.isChecked() == false) {
                        holder.cbselection.setChecked(true);
                        arraylistselectedtag.add(mList.get(position).getId());
                    } else {
                        holder.cbselection.setChecked(false);
                        arraylistselectedtag.remove(mList.get(position).getId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbselection;
        private TextView tv_topics;


        private ViewHolder(View itemView) {
            super(itemView);
            // this.setIsRecyclable(false);

            cbselection = (CheckBox) itemView.findViewById(R.id.cbselection);
            tv_topics = (TextView) itemView.findViewById(R.id.tv_topics);


        }
    }
}
