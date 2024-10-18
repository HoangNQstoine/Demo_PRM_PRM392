    package com.example.kingpho.adapter;

    import android.content.Context;
    import android.content.Intent;
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
    import com.example.kingpho.dto.CartDTO;
    import com.example.kingpho.dto.UpdateCartDTO;
    import com.example.kingpho.model.Product;
    import com.example.kingpho.service.CartService;
    import com.example.kingpho.service.UserFavouriteService;

    import java.text.DecimalFormat;
    import java.text.NumberFormat;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
        private ArrayList<Product> favouritesList;
        private HashMap<Integer, Integer> favouriteIdMap; // Map to store productId to favouriteId
        private UserFavouriteService userFavouriteService;
        private OnEmptyListListener onEmptyListListener;
        private CartService cartService;
        private int userId;
        private Context context;

        public FavouriteAdapter(ArrayList<Product> favouritesList, HashMap<Integer, Integer> favouriteIdMap, UserFavouriteService userFavouriteService, CartService cartService, int userId, OnEmptyListListener onEmptyListListener, Context context) {
            this.favouritesList = favouritesList;
            this.favouriteIdMap = favouriteIdMap;
            this.userFavouriteService = userFavouriteService;
            this.cartService = cartService;
            this.userId = userId;
            this.onEmptyListListener = onEmptyListListener;
            this.context = context;
        }

        @NonNull
        @Override
        public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favourite, parent, false);
            return new FavouriteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
            Product product = favouritesList.get(position);
            holder.bind(product);
        }

        @Override
        public int getItemCount() {
            return favouritesList.size();
        }

        public void updateFavourites(ArrayList<Product> updatedFavourites, HashMap<Integer, Integer> updatedFavouriteIdMap) {
            this.favouritesList = updatedFavourites;
            this.favouriteIdMap = updatedFavouriteIdMap;
            notifyDataSetChanged();
        }

        class FavouriteViewHolder extends RecyclerView.ViewHolder {
            private ImageView productImage;
            private TextView productName;
            private TextView productPrice;
            private ImageView removeFavouriteButton;
            private ImageView addToCartButton;

            public FavouriteViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.pic);
                productName = itemView.findViewById(R.id.titleMethod);
                productPrice = itemView.findViewById(R.id.price);
                removeFavouriteButton = itemView.findViewById(R.id.favBtn);
                addToCartButton = itemView.findViewById(R.id.addtocartBtn);
            }

            public void bind(Product product) {
                productName.setText(product.getName());
                productPrice.setText(formatMoney(String.valueOf((int) product.getPrice())));

                if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(product.getImageUrls().get(0))
                            .into(productImage);
                } else {
                    productImage.setImageResource(R.drawable.icon_pho);
                }

                addToCartButton.setOnClickListener(v -> {
                    // Add to cart logic here
                    addToCart(product);
                });

                removeFavouriteButton.setImageResource(R.drawable.heart_ic_change);
                removeFavouriteButton.setOnClickListener(v -> {
                    Integer favouriteId = favouriteIdMap.get(product.getId());
                    if (favouriteId != null) {
                        Call<Void> call = userFavouriteService.deleteUserFavourite(favouriteId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    favouritesList.remove(product);
                                    notifyItemRemoved(getAdapterPosition());
                                    if (favouritesList.isEmpty()) {
                                        onEmptyListListener.onEmptyList();
                                    }
                                    Toast.makeText(v.getContext(), "Removed from favourites: " + product.getName(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                });

                productImage.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), DetailProductActivity.class);
                    // Pass necessary product details
                    intent.putExtra("foodId", product.getId());
                    v.getContext().startActivity(intent);
                    Toast.makeText(v.getContext(), "Selected product: " + product.getName(), Toast.LENGTH_SHORT).show();
                });
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
        }

        public interface OnEmptyListListener {
            void onEmptyList();
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