package com.example.translateproject.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.translateproject.R;
import com.example.translateproject.TranslateFM;
import com.example.translateproject.model.Definition;

import java.util.ArrayList;

public class LoaiTuAdapter extends BaseAdapter {
    Context context;
    ArrayList<Definition> arrayList;

    public LoaiTuAdapter() {
    }

    public LoaiTuAdapter(Context context, ArrayList<Definition> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Definition getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewLT;
        if(convertView == null)
        {
            viewLT = View.inflate(parent.getContext(), R.layout.item_listview_loaitu,null);
        }
        else viewLT = convertView;
        TextView definition = viewLT.findViewById(R.id.tvdefiLT);
        TextView partOfSpeech = viewLT.findViewById(R.id.tvpartofspeechLT);
        TextView dichLT = viewLT.findViewById(R.id.tvdichLT);

        definition.setText(getItem(position).getDefinition());
        partOfSpeech.setText("("+getItem(position).getPartOfSpeech().toUpperCase()+")");

        TranslateFM t = new TranslateFM(context);
        dichLT.setText("("+t.translate(definition.getText().toString())+")");
        return viewLT;

    }
}
