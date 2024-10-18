package com.example.kingpho;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kingpho.adapter.MyDetailAdapt;
import com.example.kingpho.callback.DetailUserCallBack;
import com.example.kingpho.callback.UserCallback;
import com.example.kingpho.model.MyDetail;
import com.example.kingpho.model.User;
import com.example.kingpho.service.UserService;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imgGoBack, camera;
    private Button save;
    private ListView lvInfoUser;
    private List<MyDetail> arrayInfo;
    private MyDetailAdapt adapter;
    private UserService userService;
    private ShapeableImageView avatar;
    private int idUser = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail);

        mapping();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(ProfileActivity.this);
        userService = retrofit.create(UserService.class);

        getDataList(new DetailUserCallBack() {
            @Override
            public void onDetailUserFetched(List<MyDetail> detailList) {
                if (detailList != null) {
                    arrayInfo = detailList;
                    adapter = new MyDetailAdapt(ProfileActivity.this, R.layout.item_my_detail, arrayInfo);
                    lvInfoUser.setAdapter(adapter);
                } else {
                    // Handle error case
                    Log.d("Profile list", "Failed to fetch data");
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        lvInfoUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyDetail detail = arrayInfo.get(position);
                detail.setInfo("1234");
            }
        });
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void mapping() {
        imgGoBack = findViewById(R.id.imgGoBack);
        camera = findViewById(R.id.camera);
        save = findViewById(R.id.save);
        lvInfoUser = findViewById(R.id.lvInfoUser);
        avatar = findViewById(R.id.avatar);
    }
    private void getDataList(DetailUserCallBack callback) {
        ArrayList<MyDetail> list = new ArrayList<>();
        String username = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUsername();

        getUserByUsername(username, new UserCallback() {
            @Override
            public void onUserFetched(User user) {
                list.add(new MyDetail("Full Name", user.getFullname()));
                list.add(new MyDetail("Phone", user.getPhone()));
                list.add(new MyDetail("Address", user.getAddress()));
                String userAvatarUrl = user.getAvatar();
                if (userAvatarUrl != null) {
                    Glide.with(ProfileActivity.this)
                            .load(userAvatarUrl)
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar)
                            .into(avatar);
                } else {
                    avatar.setImageResource(R.drawable.avatar);
                }
                callback.onDetailUserFetched(list);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                callback.onDetailUserFetched(null);  // Return null or handle the error case
            }
        });
    }
    private void goBack() {
        finish();
    }

    public void getUserByUsername(String username, UserCallback callback) {
        try {
            Call<User> call = userService.getUserByUsername(username);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        callback.onUserFetched(user);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(int userId, User userUpdate, UserCallback userCallback) {
        try {
            Call<User> call = userService.updateUser(userId, userUpdate);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User userUpdate = response.body();
                        userCallback.onUserFetched(userUpdate);
                        Toast.makeText(ProfileActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
