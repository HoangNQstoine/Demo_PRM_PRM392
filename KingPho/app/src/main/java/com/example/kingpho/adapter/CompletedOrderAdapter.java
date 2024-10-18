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

import com.example.kingpho.FeedbackActivity;
import com.example.kingpho.R;
import com.example.kingpho.model.Order;

import java.util.List;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.CompletedOrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnItemClickListener onItemClickListener;

    public CompletedOrderAdapter(Context context, List<Order> orderList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_completed_order, parent, false);
        return new CompletedOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.textViewTitle.setText(order.getTitle());
        holder.textViewPrice.setText(order.getPrice());
        holder.textViewStatus.setText(order.getStatus());
        holder.imageViewOrder.setImageResource(order.getImageResource());

        holder.buttonReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(order);
                Intent intent = new Intent(context, FeedbackActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public static class CompletedOrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewOrder;
        TextView textViewTitle, textViewPrice, textViewStatus;
        Button buttonReorder;

        public CompletedOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOrder = itemView.findViewById(R.id.imageViewOrder);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonReorder = itemView.findViewById(R.id.buttonReorder);
        }
    }
}
