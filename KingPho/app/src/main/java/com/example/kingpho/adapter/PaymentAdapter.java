package com.example.kingpho.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.model.Food;
import java.util.ArrayList;
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private ArrayList<Food> foodItems;

    public PaymentAdapter(ArrayList<Food> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_payment , parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Food food = foodItems.get(position);
        holder.foodTitleTextView.setText(food.getFoodTitle());
        holder.foodPriceTextView.setText(String.format("$%.2f", food.getFoodPrice()));
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {

        TextView foodTitleTextView;
        TextView foodPriceTextView;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTitleTextView = itemView.findViewById(R.id.foodTitleTextView);
            foodPriceTextView = itemView.findViewById(R.id.foodPriceTextView);
        }
    }
}