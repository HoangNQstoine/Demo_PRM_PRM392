package com.example.kingpho.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kingpho.R;
import com.example.kingpho.model.MyDetail;

import java.util.List;

public class MyDetailAdapt extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final List<MyDetail> detailList;

    public MyDetailAdapt(Context context, int layout, List<MyDetail> detailList) {
        this.context = context;
        this.layout = layout;
        this.detailList = detailList;
    }

    @Override
    public int getCount() {
        if (detailList != null) {
            return detailList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        TextView properties = (TextView) convertView.findViewById(R.id.properties);
        EditText info = (EditText) convertView.findViewById(R.id.info);

        MyDetail item = detailList.get(position);
        properties.setText(item.getProperties());
        info.setText(item.getInfo());

        return convertView;
    }
}
