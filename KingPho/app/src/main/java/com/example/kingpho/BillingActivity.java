package com.example.kingpho;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.adapter.BillingAdapter;
import com.example.kingpho.model.Billing;

import java.util.ArrayList;

public class BillingActivity extends AppCompatActivity {
    private ImageView imgGoBack;
    private TextView tvAddressUser, priceBilling, deliveryFee, totalBilling, methodPayment;
    private RecyclerView lvPurchase;
    private BillingAdapter adapter;
    private ArrayList<Billing> arrayBilling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        mapping();

        arrayBilling = getArrayBilling();
        adapter = new BillingAdapter(this, R.layout.item_billing, arrayBilling);
        lvPurchase.setLayoutManager(new LinearLayoutManager(this));
        lvPurchase.setAdapter(adapter);
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        imgGoBack = findViewById(R.id.imgGoBack);
        tvAddressUser = findViewById(R.id.tvAddressUser);
        priceBilling = findViewById(R.id.priceBilling);
        deliveryFee = findViewById(R.id.deliveryFee);
        lvPurchase = findViewById(R.id.lvPurchase);
        totalBilling = findViewById(R.id.totalBilling);
        methodPayment = findViewById(R.id.methodPayment);
    }
    private ArrayList<Billing> getArrayBilling() {
        ArrayList<Billing> list = new ArrayList<>();
        list.add(new Billing("Pho bo tai nam Pho bo tai nam Pho bo tai nam Pho bo tai nam Pho bo tai nam Pho bo tai nam", "12"));

        return list;
    }
}
