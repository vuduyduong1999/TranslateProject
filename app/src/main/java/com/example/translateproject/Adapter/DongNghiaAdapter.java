package com.example.translateproject.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.translateproject.R;
import com.example.translateproject.TranslateFM;

import java.util.ArrayList;

public class DongNghiaAdapter extends BaseAdapter {
    ArrayList<String> arrayList;
    Context context;

    public DongNghiaAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public String getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewDN;
        if(convertView == null)
        {
            viewDN = View.inflate(parent.getContext(), R.layout.item_listview_dongnghia,null);
        }
        else viewDN = convertView;
        TextView tvS = viewDN.findViewById(R.id.tvSynonyms);
        tvS.setText(arrayList.get(position));
        TextView tvST = viewDN.findViewById(R.id.tvSynonymsTranslate);
        TranslateFM t = new TranslateFM(context);
        tvST.setText(t.translate(arrayList.get(position)));
        return viewDN;
    }
}
