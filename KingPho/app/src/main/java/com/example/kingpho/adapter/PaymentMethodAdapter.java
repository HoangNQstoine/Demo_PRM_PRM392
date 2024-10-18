package com.example.kingpho.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kingpho.R;
import com.example.kingpho.model.PaymentMethod;

import java.util.List;

public class PaymentMethodAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<PaymentMethod> methodList;
    private int selectedItem = -1;
    public PaymentMethodAdapter(Context context, int layout, List<PaymentMethod> methodList) {
        this.context = context;
        this.layout = layout;
        this.methodList = methodList;
    }

    @Override
    public int getCount() {
        return methodList.size();
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

        ImageView icon = (ImageView) convertView.findViewById(R.id.imgMethod);
        TextView title = (TextView) convertView.findViewById(R.id.titleMethod);

        PaymentMethod method = methodList.get(position);
        icon.setImageResource(method.getIcon());
        title.setText(method.getTitle());

        if (position == selectedItem) {
            convertView.setBackgroundResource(R.drawable.button_border_choose);
        } else {
            convertView.setBackgroundResource(R.drawable.border_orange);
        }

        return convertView;
    }
    public void setSelectedItem(int position) {
        selectedItem = position;
    }
}
