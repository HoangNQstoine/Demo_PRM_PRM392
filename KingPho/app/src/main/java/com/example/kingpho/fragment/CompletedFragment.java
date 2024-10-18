package com.example.kingpho.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kingpho.R;
import com.example.kingpho.adapter.CompletedOrderAdapter;
import com.example.kingpho.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CompletedFragment extends Fragment {

    private RecyclerView recyclerViewCompletedOrders;
    private CompletedOrderAdapter completedOrderAdapter;
    private List<Order> completedOrderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);

        recyclerViewCompletedOrders = view.findViewById(R.id.recyclerViewCompletedOrders);
        recyclerViewCompletedOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the completed order list and adapter
        completedOrderList = new ArrayList<>();
        // Add some demo data
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Special", "$10.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Special", "$10.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Beef", "$9.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Special", "$10.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Special", "$10.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Special", "$10.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Beef", "$9.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Beef", "$9.99", "Completed"));
        completedOrderList.add(new Order(R.drawable.pdb, "Pho Beef", "$9.99", "Completed"));

        completedOrderAdapter = new CompletedOrderAdapter(getContext(), completedOrderList, new CompletedOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                // Handle reorder button click
            }
        });

        recyclerViewCompletedOrders.setAdapter(completedOrderAdapter);

        return view;
    }
}
