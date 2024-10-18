package com.example.kingpho;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.adapter.TrackingStepsAdapter;
import com.example.kingpho.model.TrackingStep;

import java.util.ArrayList;
import java.util.List;

public class TrackingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTrackingSteps;
    private TrackingStepsAdapter trackingStepsAdapter;
    private List<TrackingStep> trackingSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        recyclerViewTrackingSteps = findViewById(R.id.recyclerViewTrackingSteps);
        recyclerViewTrackingSteps.setLayoutManager(new LinearLayoutManager(this));

        // Fetch tracking information (replace with your actual logic)
        fetchTrackingInformation();

        // Retrieve data passed from OrderAdapter
        int foodImage = getIntent().getIntExtra("foodImage", R.drawable.pdb);
        String foodName = getIntent().getStringExtra("foodName");
        String estimatedTime = getIntent().getStringExtra("estimatedTime");
        String price = getIntent().getStringExtra("price");

        // Populate food information in views
        ImageView imageViewFood = findViewById(R.id.imageViewFood);
        TextView textViewFoodName = findViewById(R.id.textViewFoodName);
        TextView textViewEstimatedTime = findViewById(R.id.textViewEstimatedTime);
        TextView textViewPrice = findViewById(R.id.textViewPrice);

        imageViewFood.setImageResource(foodImage);
        textViewFoodName.setText(foodName);
        textViewEstimatedTime.setText("Estimated Time: " + estimatedTime);
        textViewPrice.setText("Price: " + price);

        // Set up back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Example data (replace with actual fetched data)
        trackingSteps = new ArrayList<>();
        trackingSteps.add(new TrackingStep("Order Placed", "We have received your order.", R.drawable.order_placed, "completed"));
        trackingSteps.add(new TrackingStep("Order Confirmed", "Your order has been confirmed.", R.drawable.order_confirmed, "completed"));
        trackingSteps.add(new TrackingStep("Order Processed", "We are preparing your order.", R.drawable.order_processing, "ongoing"));
        trackingSteps.add(new TrackingStep("Ready to Pickup", "Your order is ready for pickup.", R.drawable.order_ready, "pending"));

        // Set up RecyclerView adapter
        trackingStepsAdapter = new TrackingStepsAdapter(trackingSteps);
        recyclerViewTrackingSteps.setAdapter(trackingStepsAdapter);
    }

    private void fetchTrackingInformation() {
        // Placeholder for fetching tracking information from backend or other source
        // Replace with actual implementation to fetch food order information
        // Example:
        // String foodOrderId = getIntent().getStringExtra("foodOrderId");
        // Use foodOrderId to fetch tracking steps for that specific order
    }
}
