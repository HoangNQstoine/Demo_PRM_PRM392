package com.example.kingpho.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.model.AccountItem;

import java.util.List;

public class AccountItemAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<AccountItem> itemList;

    public AccountItemAdapter(Context context, int layout, List<AccountItem> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        // ánh xạ
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        ImageView imgNext = (ImageView) convertView.findViewById(R.id.imgNext);
        TextView tvFunction = (TextView) convertView.findViewById(R.id.tvFunction);

        //gán giá trị
        AccountItem item = itemList.get(position);
        imgIcon.setImageResource(item.getIcon());
        imgNext.setImageResource(R.drawable.arrow_right);
        tvFunction.setText(item.getName());

        return convertView;
    }


}
