package com.example.kingpho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kingpho.adapter.FoodItemAdapter;
import com.example.kingpho.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {
    private TextView tvOrderId, tvTotalAmount;
    private GridView gvOrderedItems;
    private Button btnSubmitReview;
    private ImageView imgGoBack;
    private RatingBar rbRating;
    private EditText etProductReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mapping();
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating > 0) {
                    etProductReview.setEnabled(true);
                    btnSubmitReview.setEnabled(true);
                } else {
                    etProductReview.setEnabled(false);
                    btnSubmitReview.setEnabled(false);
                }
            }
        });
        String orderId = "#123456";
        double totalAmount = 150.00;

        tvOrderId.setText(orderId);
        tvTotalAmount.setText(totalAmount + "");


        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("Pizza", "Cheese Pizza", 2, 10.00, R.drawable.phocuon));
        foodItems.add(new FoodItem("Burger", "Beef Burger", 1, 8.00, R.drawable.photron));
        foodItems.add(new FoodItem("Pasta", "Chicken Pasta", 3, 12.00, R.drawable.phokho));
        foodItems.add(new FoodItem("Pasta", "Chicken Pasta", 3, 12.00, R.drawable.phonuoc));
        foodItems.add(new FoodItem("Pasta", "Chicken Pasta", 3, 12.00, R.drawable.phoran));


        FoodItemAdapter adapter = new FoodItemAdapter(this, foodItems);
        gvOrderedItems.setAdapter(adapter);

        // Handle review submission
        btnSubmitReview.setOnClickListener(v -> {
            String reviewText = etProductReview.getText().toString();
            float rating = rbRating.getRating();

            Toast.makeText(FeedbackActivity.this, "Review submitted! Rating: " + rating + " stars. Review: " + reviewText, Toast.LENGTH_SHORT).show();

            rbRating.setRating(0);
            etProductReview.setText("");
        });
    }

    private void mapping() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        gvOrderedItems = findViewById(R.id.gvOrderedItems);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        imgGoBack = findViewById(R.id.imgGoBack);
        rbRating = findViewById(R.id.rbRating);
        etProductReview = findViewById(R.id.etProductReview);
    }
}
