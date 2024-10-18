package com.example.kingpho.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kingpho.R;
import com.example.kingpho.model.FoodItem;

import java.util.List;

public class FoodItemAdapter extends BaseAdapter {
    private Context context;
    private List<FoodItem> foodItems;

    public FoodItemAdapter(Context context, List<FoodItem> foodItems) {
        this.context = context;
        this.foodItems = foodItems;
    }

    @Override
    public int getCount() {
        return foodItems.size();
    }

    @Override
    public Object getItem(int position) {
        return foodItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        }

        FoodItem foodItem = foodItems.get(position);

        TextView tvFoodDetail = convertView.findViewById(R.id.tvFoodDetail);
        ImageView ivFoodImage = convertView.findViewById(R.id.ivFoodImage);
        TextView tvFoodName = convertView.findViewById(R.id.tvFoodName);
        TextView tvFoodDetails = convertView.findViewById(R.id.tvFoodDetails);

        tvFoodDetail.setText("" + foodItem.getQuantity());
        ivFoodImage.setImageResource(foodItem.getImageResId());
        tvFoodName.setText(foodItem.getName());
        tvFoodDetails.setText("" + foodItem.getPrice());

        return convertView;
    }
}

