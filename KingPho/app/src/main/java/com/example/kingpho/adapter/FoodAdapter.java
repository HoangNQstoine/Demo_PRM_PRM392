package com.example.kingpho.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kingpho.R;
import com.example.kingpho.callback.UserCallback;
import com.example.kingpho.dto.CartDTO;
import com.example.kingpho.dto.UpdateCartDTO;
import com.example.kingpho.dto.UserFavouriteDTO;
import com.example.kingpho.model.Product;
import com.example.kingpho.model.User;
import com.example.kingpho.service.CartService;
import com.example.kingpho.service.UserFavouriteService;
import com.example.kingpho.service.UserService;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private String username;
    private int userId;
    private CartService cartService;

    public FoodAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.filteredProductList = new ArrayList<>(productList);
        this.username = SharedPrefManager.getInstance(context).getUser().getUsername();
        this.cartService = RetrofitClient.getRetrofitInstance(context).create(CartService.class);
    }

    @Override
    public int getCount() {
        return filteredProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.pic);
            holder.titleTextView = convertView.findViewById(R.id.titleMethod);
            holder.priceTextView = convertView.findViewById(R.id.price);
            holder.addToCartButton = convertView.findViewById(R.id.addtocartBtn);
            holder.favouriteButton = convertView.findViewById(R.id.favouriteBtnAdd);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = filteredProductList.get(position);

        getUserByUsername(username, new UserCallback() {
            @Override
            public void onUserFetched(User user) {
                if (user != null) {
                    userId = user.getUserId();
                    getFavouriteButton(product);
                }
            }

            private void getFavouriteButton(Product product) {
                UserFavouriteService userFavouriteService = RetrofitClient.getRetrofitInstance(context).create(UserFavouriteService.class);
                userFavouriteService.getUserFavourite(userId, product.getId()).enqueue(new Callback<UserFavouriteDTO>() {
                    @Override
                    public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isFavourite()) {
                            Log.d("Favourite", response.body().isFavourite() + "");
                            holder.favouriteButton.setImageResource(R.drawable.heart_ic_change);
                        } else {
                            holder.favouriteButton.setImageResource(R.drawable.btn3);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                        Toast.makeText(context, "Failed to fetch favourite status: " + product.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });


        holder.titleTextView.setText(product.getName());
        holder.priceTextView.setText(formatMoney(String.valueOf((int) product.getPrice())));

//        int drawableResourceId = context.getResources().getIdentifier(product.getFoodImage(), "drawable", context.getPackageName());
//        Glide.with(context).load(drawableResourceId).into(holder.imageView);

        if (product.getImageUrls() != null) {
            if (!product.getImageUrls().isEmpty()) {
                String imageProductUrls = product.getImageUrls().get(0);
                Glide.with(context).load(imageProductUrls).into(holder.imageView);
            }
            else {
                holder.imageView.setImageResource(R.drawable.icon_pho);
            }
        }
        else {
            holder.imageView.setImageResource(R.drawable.icon_pho);
        }

        holder.addToCartButton.setOnClickListener(v -> {
            addToCart(product);
        });

        holder.favouriteButton.setOnClickListener(v -> {
            try {
                UserFavouriteService userFavouriteService = RetrofitClient.getRetrofitInstance(context).create(UserFavouriteService.class);
                userFavouriteService.getUserFavourite(userId, product.getId()).enqueue(new Callback<UserFavouriteDTO>() {
                    @Override
                    public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Already a favorite, so remove it
                            userFavouriteService.deleteUserFavourite(response.body().getId()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "Removed from favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                        updateFavouriteButton(product);
                                    } else {
                                        Toast.makeText(context, "Failed to remove from favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(context, "Failed to remove from favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Not a favorite, so add it
                            UserFavouriteDTO userFavouriteDTO = new UserFavouriteDTO();
                            userFavouriteDTO.setUserId(userId);
                            userFavouriteDTO.setProductId(product.getId());
                            userFavouriteDTO.setIsFavorite(true);

                            userFavouriteService.addToFavourites(userFavouriteDTO).enqueue(new Callback<UserFavouriteDTO>() {
                                @Override
                                public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "Added to favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                        updateFavouriteButton(product);
                                    } else {
                                        Toast.makeText(context, "Failed to add to favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                                    t.printStackTrace();
                                    Toast.makeText(context, "Throwable: Failed to add to favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    public void updateFavouriteButton(Product product) {
                        UserFavouriteService userFavouriteService = RetrofitClient.getRetrofitInstance(context).create(UserFavouriteService.class);
                        userFavouriteService.getUserFavourite(userId, product.getId()).enqueue(new Callback<UserFavouriteDTO>() {
                            @Override
                            public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().isFavourite()) {
                                    Log.d("Favourite", response.body().isFavourite() + "");
                                    holder.favouriteButton.setImageResource(R.drawable.heart_ic_change);
                                } else {
                                    holder.favouriteButton.setImageResource(R.drawable.btn3);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                                Toast.makeText(context, "Failed to fetch favourite status: " + product.getName(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                        Toast.makeText(context, "Failed to fetch favourite status: " + product.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        return convertView;
    }

    private void addToCart(Product product) {
        cartService.getCartItems(userId).enqueue(new Callback<List<CartDTO>>() {
            @Override
            public void onResponse(Call<List<CartDTO>> call, Response<List<CartDTO>> response) {
                if (response.isSuccessful()) {
                    List<CartDTO> cartItems = response.body();
                    if (cartItems != null) {
                        handleCartItems(cartItems, product);
                    }
                } else if (response.code() == 404) {
                    handleCartItems(new ArrayList<>(), product);
                } else {
                    Toast.makeText(context, "Failed to fetch cart items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartDTO>> call, Throwable t) {
                Toast.makeText(context, "Failed to fetch cart items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleCartItems(List<CartDTO> cartItems, Product product) {
        CartDTO existingCartItem = null;

        for (CartDTO item : cartItems) {
            if (item.getProductId() == product.getId()) {
                existingCartItem = item;
                break;
            }
        }

        if (existingCartItem != null) {
            // Only update the quantity field
            UpdateCartDTO updatedCartItem = new UpdateCartDTO();
            updatedCartItem.setUserId(existingCartItem.getUserId());
            updatedCartItem.setProductId(existingCartItem.getProductId());
            updatedCartItem.setQuantity(existingCartItem.getQuantity() + 1);

            cartService.updateCartItemByUserIdAndProductId(updatedCartItem.getUserId(), updatedCartItem.getProductId(), updatedCartItem).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Quantity updated: " + product.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to update quantity: " + product.getName(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Failed to update quantity: " + product.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            CartDTO newCartItem = new CartDTO();
            newCartItem.setUserId(userId);
            newCartItem.setProductId(product.getId());
            newCartItem.setQuantity(1);

            cartService.addToCart(newCartItem).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Added to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to add to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Failed to add to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void filter(String query) {
        filteredProductList.clear();

        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
               query = query.toLowerCase();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateData(List<Product> newFoodList) {
        this.productList.clear();
        this.productList.addAll(newFoodList);
        this.filteredProductList.clear();
        this.filteredProductList.addAll(newFoodList);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;
        ImageView addToCartButton;
        ImageView favouriteButton;
    }

    public void getUserByUsername(String username, UserCallback callback) {
        UserService userService = RetrofitClient.getRetrofitInstance(context).create(UserService.class);

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

    public String formatMoney(String moneyString) {
        try {
            int moneyAmount = Integer.parseInt(moneyString);

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.applyPattern("#,###");

            return decimalFormat.format(moneyAmount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return moneyString;
        }
    }
}
