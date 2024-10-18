package com.example.kingpho.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kingpho.R;
import com.example.kingpho.adapter.FavouriteAdapter;
import com.example.kingpho.callback.UserCallback;
import com.example.kingpho.dto.UserFavouriteDTO;
import com.example.kingpho.helper.Manager;
import com.example.kingpho.helper.TinyDB;
import com.example.kingpho.model.Food;
import com.example.kingpho.model.Product;
import com.example.kingpho.model.User;
import com.example.kingpho.service.CartService;
import com.example.kingpho.service.ProductService;
import com.example.kingpho.service.UserFavouriteService;
import com.example.kingpho.service.UserService;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavouriteFragment extends Fragment implements FavouriteAdapter.OnEmptyListListener {
    private UserFavouriteService userFavouriteService;
    private ProductService productService;
    private UserService userService;
    private CartService cartService;
    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private ImageView imgEmpty;
    private int userId = -1; // Assume you have a way to get the userId

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        String username = SharedPrefManager.getInstance(getContext()).getUser().getUsername();

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(getContext());
        userFavouriteService = retrofit.create(UserFavouriteService.class);
        productService = retrofit.create(ProductService.class);
        userService = retrofit.create(UserService.class);
        cartService = retrofit.create(CartService.class);

        getUserByUsername(username, new UserCallback() {
            @Override
            public void onUserFetched(User user) {
                if (user != null) {
                    userId = user.getUserId();
                    getFavourite(userId);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        imgEmpty = view.findViewById(R.id.imgEmpty);

        ArrayList<Product> favouritesList = new ArrayList<>();
        HashMap<Integer, Integer> favouriteIdMap = new HashMap<>();
        recyclerView = view.findViewById(R.id.recyclerViewFav);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favouriteAdapter = new FavouriteAdapter(favouritesList, favouriteIdMap , userFavouriteService, cartService, userId, this, getContext());
        recyclerView.setAdapter(favouriteAdapter);

        return view;
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

    private void getFavourite(int userId) {
        try {
            Call<List<UserFavouriteDTO>> call = userFavouriteService.getUserFavourites(userId);
            call.enqueue(new Callback<List<UserFavouriteDTO>>() {
                @Override
                public void onResponse(Call<List<UserFavouriteDTO>> call, Response<List<UserFavouriteDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<UserFavouriteDTO> favourites = response.body();
                        if (favourites.isEmpty()) {
                            imgEmpty.setVisibility(View.VISIBLE);
                        } else {
                            imgEmpty.setVisibility(View.GONE);
                        }

                        ArrayList<Product> favouriteProducts = new ArrayList<>();
                        HashMap<Integer, Integer> favouriteIdMap = new HashMap<>();

                        for (UserFavouriteDTO favourite : favourites) {
                            favouriteIdMap.put(favourite.getProductId(), favourite.getId());
                            fetchProductById(favourite.getProductId(), favouriteProducts, favouriteIdMap);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<UserFavouriteDTO>> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchProductById(int productId, ArrayList<Product> favouriteProducts, HashMap<Integer, Integer> favouriteIdMap) {
        Call<Product> call = productService.getProductById(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    favouriteProducts.add(product);
                    favouriteAdapter.updateFavourites(favouriteProducts, favouriteIdMap);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onEmptyList() {
        imgEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        getFavourite(userId);
    }
}