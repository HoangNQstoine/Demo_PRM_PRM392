package com.example.kingpho.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.model.Billing;

import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {
    private final Context context;
    private final int layout;
    private final List<Billing> billingList;

    public BillingAdapter(Context context, int layout, List<Billing> billingList) {
        this.context = context;
        this.layout = layout;
        this.billingList = billingList;
    }


    @NonNull
    @Override
    public BillingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout,parent,false);
        return new BillingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingAdapter.ViewHolder holder, int position) {
        Billing billing = billingList.get(position);
        holder.foodPaid.setText(billing.getName());
        holder.amountPaid.setText(billing.getAmount());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return billingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodPaid;
        private final TextView amountPaid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodPaid = itemView.findViewById(R.id.foodPaid);
            amountPaid = itemView.findViewById(R.id.amountPaid);
        }
    }
}
