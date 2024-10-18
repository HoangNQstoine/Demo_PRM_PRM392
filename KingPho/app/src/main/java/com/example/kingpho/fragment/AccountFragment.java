package com.example.kingpho.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.kingpho.LoginActivity;
import com.example.kingpho.MapActivity;
import com.example.kingpho.MyOrderActivity;
import com.example.kingpho.ProfileActivity;
import com.example.kingpho.R;
import com.example.kingpho.adapter.AccountItemAdapter;
import com.example.kingpho.callback.UserCallback;
import com.example.kingpho.model.AccountItem;
import com.example.kingpho.model.User;
import com.example.kingpho.service.UserService;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountFragment extends Fragment {
    private ShapeableImageView imgAvatar;
    private TextView tvName, tvEmail;
    private ListView lvItem;
    private Button btnLogOut;
    private ArrayList<AccountItem> arrayItem;
    private AccountItemAdapter adapter;
    private UserService userService;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account, container, false);

        mapping(view);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(getContext());
        userService = retrofit.create(UserService.class);

        String username = SharedPrefManager.getInstance(getContext()).getUser().getUsername();
        getUserByUsername(username, new UserCallback() {
            @Override
            public void onUserFetched(User user) {
                if (user != null) {
                    tvName.setText(user.getFullname());
                    tvEmail.setText(user.getEmail());
                    String userAvatarUrl = user.getAvatar();
                    if (userAvatarUrl != null) {
                        Glide.with(getContext())
                                .load(userAvatarUrl)
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar)
                                .into(imgAvatar);
                    }
                    else {
                        imgAvatar.setImageResource(R.drawable.avatar);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        arrayItem = getDataList();
        adapter = new AccountItemAdapter(getContext(), R.layout.item_function_user, arrayItem);
        lvItem.setAdapter(adapter);

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountItem item = arrayItem.get(position);
                if (item.getName().equals("Order")){
                    Intent intent = new Intent(getContext(), MyOrderActivity.class);
                    startActivity(intent);
                } else if (item.getName().equals("My Details")) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    startActivity(intent);
                } else if (item.getName().equals("Delivery Address")) {
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    startActivity(intent);
                }
            }
        });


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAction();
            }
        });
        return view;
    }

    private void mapping(View view) {
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        lvItem = view.findViewById(R.id.lvItem);
        btnLogOut = view.findViewById(R.id.btnLogOut);
    }

    private ArrayList<AccountItem> getDataList() {
        ArrayList<AccountItem> list = new ArrayList<>();
        list.add(new AccountItem(R.drawable.orders, "Order"));
        list.add(new AccountItem(R.drawable.mydetails, "My Details"));
        list.add(new AccountItem(R.drawable.delivery, "Delivery Address"));
        list.add(new AccountItem(R.drawable.payment, "Payment Methods"));
        list.add(new AccountItem(R.drawable.promo, "Promo Cord"));
        list.add(new AccountItem(R.drawable.bell, "Notification"));
        list.add(new AccountItem(R.drawable.help, "Help"));
        list.add(new AccountItem(R.drawable.about, "About"));
        return list;
    }
    private void logoutAction() {
//        Toast.makeText(getContext(), "Logout Action", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        SharedPrefManager.getInstance(getContext()).clear();
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
}
