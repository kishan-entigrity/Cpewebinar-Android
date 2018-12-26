package com.entigrity.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.model.MenuModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by anupamchugh on 22/12/17.
 */


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);
        ImageView iv_picture = convertView.findViewById(R.id.iv_picture);

        if (childPosition == 0) {
            iv_picture.setImageResource(R.mipmap.dashboard);
        } else if (childPosition == 1) {
            iv_picture.setImageResource(R.mipmap.change_password);
        } else if (childPosition == 2) {
            iv_picture.setImageResource(R.mipmap.my_favorites);
        } else if (childPosition == 3) {
            iv_picture.setImageResource(R.mipmap.my_assessment);
        }


        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        ImageView iv_picture = convertView.findViewById(R.id.iv_picture);

        if (groupPosition == 0) {
            iv_picture.setImageResource(R.mipmap.profile);
        } else if (groupPosition == 1) {
            iv_picture.setImageResource(R.mipmap.home);
        } else if (groupPosition == 2) {
            iv_picture.setImageResource(R.mipmap.series_webinars);
        } else if (groupPosition == 3) {
            iv_picture.setImageResource(R.mipmap.contact_us);
        } else if (groupPosition == 4) {
            iv_picture.setImageResource(R.mipmap.instructor_profile);
        } else if (groupPosition == 5) {
            iv_picture.setImageResource(R.mipmap.company_profile);
        } else if (groupPosition == 6) {
            iv_picture.setImageResource(R.mipmap.faq);
        } else if (groupPosition == 7) {
            iv_picture.setImageResource(R.mipmap.privacy_policy);
        } else if (groupPosition == 8) {
            iv_picture.setImageResource(R.mipmap.logout);
        }


        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}