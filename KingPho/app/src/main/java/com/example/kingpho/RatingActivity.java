package com.example.kingpho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kingpho.adapter.CommentAdapter;
import com.example.kingpho.model.Comment;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    private ImageView imgGoBack;
    private TextView ratingOverall, reviewCount;
    private RatingBar ratingBarOverall;
    private ProgressBar progress5Star, progress4Star, progress3Star, progress2Star, progress1Star;
    private ListView recyclerReviews;
    private CommentAdapter adapter;
    private ArrayList<Comment> arrayComment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        mapping();

        arrayComment = getDataList();

        adapter = new CommentAdapter(this, R.layout.layout_comment, arrayComment);
        recyclerReviews.setAdapter(adapter);

        ratingOverall.setText("4.5");
        reviewCount.setText("6.7k reviews");
        ratingBarOverall.setRating(4.5F);
        progress1Star.setProgress(10);
        progress2Star.setProgress(30);
        progress3Star.setProgress(50);
        progress4Star.setProgress(75);
        progress5Star.setProgress(86);

        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }
    private void mapping() {
        imgGoBack = findViewById(R.id.imgGoBack);
        ratingOverall = findViewById(R.id.ratingOverall);
        reviewCount = findViewById(R.id.reviewCount);
        ratingBarOverall = findViewById(R.id.ratingBarOverall);
        progress1Star = findViewById(R.id.progress1Star);
        progress2Star = findViewById(R.id.progress2Star);
        progress3Star = findViewById(R.id.progress3Star);
        progress4Star = findViewById(R.id.progress4Star);
        progress5Star = findViewById(R.id.progress5Star);
        recyclerReviews = findViewById(R.id.listReviews);
    }
    private ArrayList<Comment> getDataList(){
        ArrayList<Comment> list = new ArrayList<>();
        list.add(new Comment(R.drawable.avatar, "Thu Huong", "huong@gmail.com", 4.0, "Delicious", "Pho bo tai nam", " 13 June 2024"));
        list.add(new Comment(R.drawable.avatar, "Thu Huong", "huong@gmail.com", 4.0, "Delicious", "Pho bo tai nam", " 13 June 2024"));
        list.add(new Comment(R.drawable.avatar, "Thu Huong", "huong@gmail.com", 4.0, "Delicious", "Pho bo tai nam", " 13 June 2024"));
        list.add(new Comment(R.drawable.avatar, "Thu Huong", "huong@gmail.com", 4.0, "Delicious", "Pho bo tai nam", " 13 June 2024"));
        list.add(new Comment(R.drawable.avatar, "Thu Huong", "huong@gmail.com", 4.0, "Delicious", "Pho bo tai nam", " 13 June 2024"));
        list.add(new Comment(R.drawable.avatar, "Thu Huong", "huong@gmail.com", 4.0, "Delicious", "Pho bo tai nam", " 13 June 2024"));
        list.add(new Comment(R.drawable.avatar, "Thu Huong", "huong@gmail.com", 4.0, "Delicious", "Pho bo tai nam", " 13 June 2024"));
        return list;
    }
    private void goBack() {
        finish();
    }
}
