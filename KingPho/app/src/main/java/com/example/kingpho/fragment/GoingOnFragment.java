package com.example.kingpho.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.adapter.OrderAdapter;
import com.example.kingpho.model.Order;

import java.util.ArrayList;
import java.util.List;

public class GoingOnFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    public GoingOnFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_going_on, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize order list (replace with your data fetching logic)
        orderList = new ArrayList<>();
        orderList.add(new Order(R.drawable.pdb, "Pho Beef", "$12.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));
        orderList.add(new Order(R.drawable.pdb,"Pho Chicken", "$10.99", "In progress"));


        // Initialize and set adapter with listener
        orderAdapter = new OrderAdapter(getContext(), orderList);
        recyclerView.setAdapter(orderAdapter);
    }
}
