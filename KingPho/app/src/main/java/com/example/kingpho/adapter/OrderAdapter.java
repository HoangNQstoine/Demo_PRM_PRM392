package com.example.kingpho.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.TrackingActivity;
import com.example.kingpho.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.imageViewOrder.setImageResource(order.getImageResource());
        holder.textViewTitle.setText(order.getTitle());
        holder.textViewPrice.setText(order.getPrice());
        holder.textViewStatus.setText(order.getStatus());

        // Set onClickListener for the tracking button
        holder.buttonTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TrackingActivity and pass data
                Intent intent = new Intent(context, TrackingActivity.class);
                intent.putExtra("foodImage", order.getImageResource());  // Passing the image resource
                intent.putExtra("foodName", order.getTitle());  // Example: passing order title as food name
                intent.putExtra("estimatedTime", "30 minutes");  // Replace with actual data fields
                intent.putExtra("price", order.getPrice());  // Replace with actual data fields
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewOrder;
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewStatus;
        Button buttonTracking;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOrder = itemView.findViewById(R.id.imageViewOrder);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonTracking = itemView.findViewById(R.id.buttonTracking);
        }
    }
}
