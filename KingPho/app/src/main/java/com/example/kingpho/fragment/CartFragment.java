package com.example.kingpho.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.Interface.ChangeNumberItemsListener;
import com.example.kingpho.PaymentActivity;
import com.example.kingpho.R;
import com.example.kingpho.adapter.CartAdapter;
import com.example.kingpho.callback.UserCallback;
import com.example.kingpho.dto.CartDTO;
import com.example.kingpho.model.User;
import com.example.kingpho.service.CartService;
import com.example.kingpho.service.ProductService;
import com.example.kingpho.service.UserService;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartFragment extends Fragment implements ChangeNumberItemsListener, CartAdapter.OnItemCheckedChangeListener {

    private RecyclerView recyclerViewCart;
    private TextView itemTotalTxt, itemTotal, deliveryTxt, deliveryTotal, totalTxt, total;
    private Button checkoutBtn;
    private ImageView imageViewEmptyCart;
    private LinearLayout itemTotalLayout, deliveryLayout, totalLayout;
    private ArrayList<CartDTO> cartList;
    private CartAdapter adapter;
    private int userId = -1;
    private CartService cartService;
    private ProductService productService;
    private UserService userService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        itemTotalTxt = view.findViewById(R.id.itemTotalTxt);
        itemTotal = view.findViewById(R.id.itemTotal);
        deliveryTxt = view.findViewById(R.id.deliveryTxt);
        deliveryTotal = view.findViewById(R.id.deliveryTotal);
        totalTxt = view.findViewById(R.id.totalTxt);
        total = view.findViewById(R.id.total);
        checkoutBtn = view.findViewById(R.id.checkoutBtn);
        imageViewEmptyCart = view.findViewById(R.id.imageViewEmptyCart);
        itemTotalLayout = view.findViewById(R.id.itemTotalLayout);
        deliveryLayout = view.findViewById(R.id.deliveryLayout);
        totalLayout = view.findViewById(R.id.totalLayout);

        String username = SharedPrefManager.getInstance(getContext()).getUser().getUsername();

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(getContext());
        cartService = retrofit.create(CartService.class);
        productService = retrofit.create(ProductService.class);
        userService = retrofit.create(UserService.class);

        getUserByUsername(username, new UserCallback() {
            @Override
            public void onUserFetched(User user) {
                if (user != null) {
                    userId = user.getUserId();
                    fetchCartItems(userId);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        checkoutBtn.setOnClickListener(v -> {
            double finalTotalPrice = adapter.calculateTotalPrice();
            Intent intent = new Intent(getContext(), PaymentActivity.class);
            intent.putExtra("total_price", finalTotalPrice);
            startActivity(intent);
        });

        return view;
    }

    private void fetchCartItems(int userId) {
        try {
            Call<List<CartDTO>> call = cartService.getCartItems(userId);
            call.enqueue(new Callback<List<CartDTO>>() {
                @Override
                public void onResponse(Call<List<CartDTO>> call, Response<List<CartDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        cartList = new ArrayList<>(response.body());
                        setupRecyclerView();
                        updateTotal();
                    }
                }

                @Override
                public void onFailure(Call<List<CartDTO>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(cartList, productService, this, this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCart.setAdapter(adapter);
    }

    private void updateTotal() {
        double totalPrice = adapter.calculateTotalPrice();
        double deliveryFee = totalPrice * 0.2;
        int totalCheckedItems = adapter.calculateTotalCheckedItems();

        itemTotal.setText(String.valueOf(totalCheckedItems));
        deliveryTotal.setText(formatMoney(formatMoney(String.valueOf((int) deliveryFee))));
        total.setText(formatMoney(String.valueOf((int) totalPrice + deliveryFee)));

        if (cartList.isEmpty()) {
            imageViewEmptyCart.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
            itemTotalLayout.setVisibility(View.GONE);
            deliveryLayout.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
            checkoutBtn.setVisibility(View.GONE);
        } else {
            imageViewEmptyCart.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.VISIBLE);
            itemTotalLayout.setVisibility(View.VISIBLE);
            deliveryLayout.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
            checkoutBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void changed() {
        updateTotal();
    }

    @Override
    public void onItemCheckedChanged() {
        updateTotal();
    }

    public static String formatMoney(String moneyString) {
        try {
            // Parse moneyString to handle commas if present
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            int moneyAmount = numberFormat.parse(moneyString).intValue();

            // Format the number
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.applyPattern("#,###");

            return decimalFormat.format(moneyAmount) + "đ"; // Append 'đ' symbol
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            // Handle non-numeric input gracefully
            return moneyString + "đ";
        }
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
