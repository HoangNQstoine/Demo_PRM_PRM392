
package com.example.kingpho.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kingpho.DetailProductActivity;
import com.example.kingpho.R;
import com.example.kingpho.callback.UserCallback;
import com.example.kingpho.dto.CartDTO;
import com.example.kingpho.dto.UpdateCartDTO;
import com.example.kingpho.dto.UserFavouriteDTO;
import com.example.kingpho.helper.Manager;
import com.example.kingpho.model.Product;
import com.example.kingpho.model.User;
import com.example.kingpho.service.CartService;
import com.example.kingpho.service.UserFavouriteService;
import com.example.kingpho.service.UserService;
import com.example.kingpho.utils.RetrofitClient;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> productList;
    private Manager manager;
    private String username;

    public ProductAdapter(ArrayList<Product> productList, Manager manager, String username) {
        this.productList = productList;
        this.manager = manager;
        this.username = username;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProducts(ArrayList<Product> updatedProducts) {
        this.productList = updatedProducts;
        notifyDataSetChanged();

    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private UserFavouriteService userFavouriteService = RetrofitClient.getRetrofitInstance(itemView.getContext()).create(UserFavouriteService.class);
        private CartService cartService = RetrofitClient.getRetrofitInstance(itemView.getContext()).create(CartService.class);
        private int userId;

        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private ImageView addToCartButton;
        private ImageView favouriteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.pic);
            productName = itemView.findViewById(R.id.titleMethod);
            productPrice = itemView.findViewById(R.id.price);
            addToCartButton = itemView.findViewById(R.id.addtocartBtn);
            favouriteButton = itemView.findViewById(R.id.favouriteBtnAdd);
        }

        public void bind(Product product) {
            productName.setText(product.getName());
            productPrice.setText(formatMoney(String.valueOf((int) product.getPrice())));

//            int drawableResourceId = itemView.getContext().getResources().getIdentifier(product.getFoodImage(), "drawable", itemView.getContext().getPackageName());

            if (product.getImageUrls() != null) {
                if (!product.getImageUrls().isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(product.getImageUrls().get(0))
                            .into(productImage);
                }
            }

            getUserByUsername(username, new UserCallback() {
                @Override
                public void onUserFetched(User user) {
                    if (user != null) {
                        userId = user.getUserId();
                        updateFavouriteButton(product);
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });

            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart(product);
                }
            });
//
//
            favouriteButton.setOnClickListener(v -> {
                userFavouriteService.getUserFavourite(userId, product.getId()).enqueue(new Callback<UserFavouriteDTO>() {
                    @Override
                    public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Already a favorite, so remove it
                            userFavouriteService.deleteUserFavourite(response.body().getId()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        updateFavouriteButton(product);
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Failed to remove from favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(itemView.getContext(), "Failed to remove from favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
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
                                        updateFavouriteButton(product);
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Failed to add to favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                                    t.printStackTrace();
                                    Toast.makeText(itemView.getContext(), "Throwable: Failed to add to favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "Failed to fetch favourite status: " + product.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DetailProductActivity.class);
                    intent.putExtra("foodId", product.getId());
                    v.getContext().startActivity(intent);
                    Toast.makeText(v.getContext(), "Bạn đã chọn sản phẩm " + product.getName(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void updateFavouriteButton(Product product) {
            userFavouriteService.getUserFavourite(userId, product.getId()).enqueue(new Callback<UserFavouriteDTO>() {
                @Override
                public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isFavourite()) {
                        favouriteButton.setImageResource(R.drawable.heart_ic_change);
                    } else {
                        favouriteButton.setImageResource(R.drawable.btn3);
                    }
                }

                @Override
                public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Failed to fetch favourite status: " + product.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void getUserByUsername(String username, UserCallback callback) {
            UserService userService = RetrofitClient.getRetrofitInstance(itemView.getContext()).create(UserService.class);

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
                        Toast.makeText(itemView.getContext(), "Failed to fetch cart items", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CartDTO>> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Failed to fetch cart items", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(itemView.getContext(), "Quantity updated: " + product.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "Failed to update quantity: " + product.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "Failed to update quantity: " + product.getName(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(itemView.getContext(), "Added to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "Failed to add to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "Failed to add to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}