package com.example.translateproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.translateproject.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<String> arrtitle;
    ArrayList<ListView> arrdetail;

    public ExpandableListViewAdapter(Context context, ArrayList<String> arrtitle, ArrayList<ListView> arrdetail) {
        this.context = context;
        this.arrtitle = arrtitle;
        this.arrdetail = arrdetail;
    }

    @Override
    public int getGroupCount() {
        return arrtitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return arrtitle.get(groupPosition);
    }

    @Override
    public ListView getChild(int groupPosition, int childPosition) {
        return arrdetail.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fags_title_item_expandableview, null);
        }
        TextView title = convertView.findViewById(R.id.tvfagstitleview);
        title.setText(arrtitle.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fag_detail_item_expandableview, null);
        }
        ListView lv = convertView.findViewById(R.id.lvDetailFaqItem);
        lv.setAdapter(arrdetail.get(groupPosition).getAdapter());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
