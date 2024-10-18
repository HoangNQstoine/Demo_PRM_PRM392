package com.example.kingpho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kingpho.adapter.PaymentMethodAdapter;
import com.example.kingpho.fragment.HomeFragment;
import com.example.kingpho.model.PaymentMethod;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    public static final String EXTRA_TOTAL_PRICE = "com.example.kingpho.EXTRA_TOTAL_PRICE";
    private ImageView imgGoBack;
    private ListView lvMethod;
    private TextView totalPriceTextView;
    private Button btnCheckout;
    private ArrayList<PaymentMethod> methodArray;
    private PaymentMethodAdapter methodAdapter;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mapping();

        methodArray = getData();

        Toast.makeText(PaymentActivity.this, methodArray.size() + "", Toast.LENGTH_SHORT).show();
        methodAdapter = new PaymentMethodAdapter(this, R.layout.item_payment, methodArray);
        lvMethod.setAdapter(methodAdapter);


        lvMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaymentMethod payment = methodArray.get(position);
                if (index != -1) {
                    methodAdapter.setSelectedItem(-1); // Reset previously selected item
                }
                methodAdapter.setSelectedItem(position); // Set the new selected item
                methodAdapter.notifyDataSetChanged();
                index = position; // Update previously selected position
            }
        });
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "Thanh toan thanh cong", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentActivity.this, BillingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        double totalPrice = getIntent().getDoubleExtra("total_price", 0.0);
        totalPriceTextView.setText(String.format("%.2fđ", totalPrice));
    }
    private void mapping() {
        imgGoBack = findViewById(R.id.imgGoBack);
        totalPriceTextView = findViewById(R.id.totalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        lvMethod = findViewById(R.id.lvMethod);
    }
    private ArrayList<PaymentMethod> getData() {
        ArrayList<PaymentMethod> list = new ArrayList<>();
        list.add(new PaymentMethod(R.drawable.cash, "Cash"));
        list.add(new PaymentMethod(R.drawable.zalopay, "Zalo Pay"));
        return list;
    }
}
