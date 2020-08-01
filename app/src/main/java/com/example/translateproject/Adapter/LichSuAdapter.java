package com.example.translateproject.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.translateproject.R;
import com.example.translateproject.TranslateFM;
import com.example.translateproject.model.WordHistory;

import java.util.ArrayList;

public class LichSuAdapter extends BaseAdapter {
    ArrayList<WordHistory> arrayList;
    Context context;

    public LichSuAdapter() {
    }

    public LichSuAdapter(ArrayList<WordHistory> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public WordHistory getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewLS;
        if(convertView == null)
        {
            viewLS = View.inflate(parent.getContext(), R.layout.item_listview_history,null);
        }
        else viewLS = convertView;
        TextView tvW = viewLS.findViewById(R.id.tvWordH);
        tvW.setText(arrayList.get(position).getWord());
        TextView tvCT = viewLS.findViewById(R.id.tvContentH);
        tvCT.setText(arrayList.get(position).getContent());
        TextView tvT = viewLS.findViewById(R.id.tvtimeH);
        tvT.setText(arrayList.get(position).getTime());
        return viewLS;

    }
}
