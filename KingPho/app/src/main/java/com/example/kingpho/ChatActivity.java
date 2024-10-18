package com.example.kingpho;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.adapter.ChatingAdapter;
import com.example.kingpho.fragment.MainActivity;
import com.example.kingpho.model.Chating;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private ImageView imgGoBack, imgAvatarHelper, camera, album_photo, imgSend;
    private TextView nameHelper;
    private EditText contentMessage;
    private RecyclerView recyclerChat;
    private ArrayList<Chating> arrayChat;
    private ChatingAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mapping();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerChat.setLayoutManager(layoutManager);
        arrayChat = getDataList();
        adapter = new ChatingAdapter(arrayChat);
        recyclerChat.setAdapter(adapter);

        imgAvatarHelper.setImageResource(R.drawable.avatar);
        nameHelper.setText("NoodleNinja");
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraForm();
            }
        });
        album_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPhotoForm();
            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = contentMessage.getText().toString().trim();
                if (!question.isEmpty()) {
                    arrayChat.add(new Chating(question, "14:23",true));
                    adapter.notifyItemInserted(arrayChat.size() - 1);
                    recyclerChat.scrollToPosition(arrayChat.size() - 1);
                    contentMessage.setText("");
                }
            }
        });
    }
    private void mapping() {
        imgGoBack = findViewById(R.id.imgGoBack);
        imgAvatarHelper = findViewById(R.id.imgAvatarHelper);
        nameHelper = findViewById(R.id.nameHelper);
        recyclerChat = findViewById(R.id.recyclerChat);
        camera = findViewById(R.id.camera);
        album_photo = findViewById(R.id.album_photo);
        imgSend = findViewById(R.id.imgSend);
        contentMessage = findViewById(R.id.contentMessage);
    }
    private ArrayList<Chating> getDataList() {
        ArrayList<Chating> list = new ArrayList<>();
        list.add(new Chating("Hi", "14:23", true));
        list.add(new Chating("Can I help you ?", "14:23", false));
        list.add(new Chating("I just bought a set of rare beef noodle soup at my store. But I have a few issues to comment on here", "14:25", true));
        list.add(new Chating("Yes, I am happy for you to give your comments about our products and services. Don't know what problem you are having?", "14:26", false));
        list.add(new Chating("Hi", "14:23", true));
        list.add(new Chating("Can I help you ?", "14:23", false));
        list.add(new Chating("I just bought a set of rare beef noodle soup at my store. But I have a few issues to comment on here", "14:25", true));
        list.add(new Chating("Yes, I am happy for you to give your comments about our products and services. Don't know what problem you are having?", "14:26", false));
        list.add(new Chating("Yes, I am happy for you to give your comments about our products and services. Don't know what problem you are having?", "14:26", false));
        list.add(new Chating("Yes, I am happy for you to give your comments about our products and services. Don't know what problem you are having?", "14:26", false));
        return list;
    }
    private void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void cameraForm(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void albumPhotoForm(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
