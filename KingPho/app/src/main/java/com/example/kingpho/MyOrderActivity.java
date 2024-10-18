package com.example.kingpho;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kingpho.fragment.CompletedFragment;
import com.example.kingpho.fragment.GoingOnFragment;

public class MyOrderActivity extends AppCompatActivity {

    private Button btnGoingOn, btnCompleted;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        btnBack = findViewById(R.id.btnBack);
        btnGoingOn = findViewById(R.id.btnGoingOn);
        btnCompleted = findViewById(R.id.btnCompleted);

        // Handle back button click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Load the GoingOnFragment by default and set button states
        loadOngoingTransactions();
        btnGoingOn.setBackgroundColor(ContextCompat.getColor(MyOrderActivity.this, R.color.yellow));
        btnCompleted.setBackgroundColor(ContextCompat.getColor(MyOrderActivity.this, android.R.color.white));

        btnGoingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoingOn.setBackgroundColor(ContextCompat.getColor(MyOrderActivity.this, R.color.yellow));
                btnCompleted.setBackgroundColor(ContextCompat.getColor(MyOrderActivity.this, android.R.color.white));
                loadOngoingTransactions();
            }
        });

        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCompleted.setBackgroundColor(ContextCompat.getColor(MyOrderActivity.this, R.color.yellow));
                btnGoingOn.setBackgroundColor(ContextCompat.getColor(MyOrderActivity.this, android.R.color.white));
                loadCompletedTransactions();
            }
        });
    }

    private void loadOngoingTransactions() {
        loadFragment(new GoingOnFragment());
    }

    private void loadCompletedTransactions() {
        loadFragment(new CompletedFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.orderFrameLayout, fragment);
        fragmentTransaction.commit();
    }
}
