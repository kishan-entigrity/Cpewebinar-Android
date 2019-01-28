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
import com.entigrity.model.topicsofinterest.TagsItem;

import java.util.ArrayList;

public class TopicsofinterestAdapter extends RecyclerView.Adapter<TopicsofinterestAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TagsItem> mList;
    LayoutInflater mInflater;
    public ArrayList<Integer> arraylistselectedtag = new ArrayList<Integer>();


    public TopicsofinterestAdapter(Context mContext, ArrayList<TagsItem> mList, ArrayList<Integer> arraylistselectedtag) {
        this.mContext = mContext;
        this.arraylistselectedtag = arraylistselectedtag;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topics_of_interset, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_topics.setText(mList.get(position).getTag());


        if (arraylistselectedtag.size() > 0) {
            for (int k = 0; k < arraylistselectedtag.size(); k++) {
                if (mList.get(position).getId() == (arraylistselectedtag.get(k))) {
                    holder.cbselection.setChecked(true);
                }
            }
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
                        arraylistselectedtag.remove(position);
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
            this.setIsRecyclable(false);

            cbselection = (CheckBox) itemView.findViewById(R.id.cbselection);
            tv_topics = (TextView) itemView.findViewById(R.id.tv_topics);


        }
    }
}
