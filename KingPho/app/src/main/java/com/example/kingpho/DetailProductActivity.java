package com.example.kingpho;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kingpho.adapter.ImageProductAdapter;
import com.example.kingpho.callback.ProductCallback;
import com.example.kingpho.callback.UserCallback;
import com.example.kingpho.dto.CartDTO;
import com.example.kingpho.dto.UpdateCartDTO;
import com.example.kingpho.dto.UserFavouriteDTO;
import com.example.kingpho.model.Product;
import com.example.kingpho.model.User;
import com.example.kingpho.service.CartService;
import com.example.kingpho.service.ProductService;
import com.example.kingpho.service.UserFavouriteService;
import com.example.kingpho.service.UserService;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailProductActivity extends AppCompatActivity {

    private ProductService productService;
    private UserFavouriteService userFavouriteService;
    private UserService userService;
    private CartService cartService;
    private int userId;

    private ViewPager2 vpListImage;
    private CircleIndicator3 circleIndicator;
    private ImageView imgGoBack, imgTym, imgMinus, imgPlus, imgNext;
    private TextView tvNameProduct, price, sumPrice, description, seeMore;
    private EditText numberProduct;
    private Button btnAddToCart;
    private boolean isFavor = false;
    private static int PRICE = 45000;
    private List<String> arrayImage;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = vpListImage.getCurrentItem();
            if (currentPosition == arrayImage.size() - 1) {
                vpListImage.setCurrentItem(0);
            } else {
                vpListImage.setCurrentItem(currentPosition +1 );
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Intent intent = getIntent();
        int foodId = intent.getIntExtra("foodId", - 1);

        String username = SharedPrefManager.getInstance(this).getUser().getUsername();

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(this);
        productService = retrofit.create(ProductService.class);
        userFavouriteService = retrofit.create(UserFavouriteService.class);
        userService = retrofit.create(UserService.class);
        cartService = retrofit.create(CartService.class);

        getUserByUsername(username, new UserCallback() {
            @Override
            public void onUserFetched(User user) {
                if (user != null) {
                    userId = user.getUserId();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        mapping();
        vpListImage.setOffscreenPageLimit(3);
        vpListImage.setClipToPadding(false);
        vpListImage.setClipChildren(false);

        arrayImage = new ArrayList<>();

        vpListImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2000);
            }
        });

        getProductById(foodId, new ProductCallback() {
            @Override
            public void onListProductFetched(List<Product> productList) {

            }

            @Override
            public void onProductFetched(Product product) {
                if (product != null) {
                    tvNameProduct.setText(product.getName());
                    price.setText(formatMoney(String.valueOf((int) product.getPrice())));
                    sumPrice.setText(formatMoney(String.valueOf((int) product.getPrice())));
                    description.setText(product.getDescription());
                    PRICE = (int) product.getPrice();

                    arrayImage.addAll(product.getImageUrls());
                    ImageProductAdapter adapter = new ImageProductAdapter(arrayImage);
                    vpListImage.setAdapter(adapter);
                    circleIndicator.setViewPager(vpListImage);

                    checkIfFavourited(product.getId());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        numberProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    return;
                }
                int num = Integer.parseInt(s.toString());
                if (num > 100) {
                    numberProduct.setText(String.valueOf(100));
                    numberProduct.setSelection(numberProduct.getText().length());
                    Toast.makeText(DetailProductActivity.this, "Vui lòng không vượt quá 100", Toast.LENGTH_SHORT).show();
                }
                else {
                    sumPrice.setText(formatMoney(String.valueOf(PRICE*num)));
                }
            }
        });
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusAction();
            }
        });
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusAction();
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeMoreAction();
            }
        });
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeMoreAction();
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(foodId);
            }
        });

        imgTym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavor) {
                    removeFromFavorites(foodId);
                } else {
                    addToFavorites(foodId);
                }
                isFavor = !isFavor;
                updateFavoriteButtonState();
            }
        });
    }

    private void updateFavoriteButtonState() {
        if (isFavor) {
            imgTym.setImageResource(R.drawable.favorite);
        } else {
            imgTym.setImageResource(R.drawable.tym);
        }
    }

    private void addToFavorites(int productId) {
        try {
            UserFavouriteDTO userFavouriteDTO = new UserFavouriteDTO();
            userFavouriteDTO.setUserId(userId);
            userFavouriteDTO.setProductId(productId);
            userFavouriteDTO.setIsFavorite(true);

            userFavouriteService.addToFavourites(userFavouriteDTO).enqueue(new Callback<UserFavouriteDTO>() {
                @Override
                public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DetailProductActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailProductActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                    Toast.makeText(DetailProductActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFromFavorites(int productId) {
        try {
            userFavouriteService.getUserFavourite(userId, productId).enqueue(new Callback<UserFavouriteDTO>() {
                @Override
                public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int userFavoriteId = response.body().getId();
                        userFavouriteService.deleteUserFavourite(userFavoriteId).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(DetailProductActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetailProductActivity.this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(DetailProductActivity.this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });
                    } else {
                        Toast.makeText(DetailProductActivity.this, "Failed to get favorite information", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                    Toast.makeText(DetailProductActivity.this, "Failed to get favorite information", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfFavourited(int productId) {
        try {
            userFavouriteService.getUserFavourite(userId, productId).enqueue(new Callback<UserFavouriteDTO>() {
                @Override
                public void onResponse(Call<UserFavouriteDTO> call, Response<UserFavouriteDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        isFavor = response.body().isFavourite();
                        updateFavoriteButtonState();
                    } else {
                        isFavor = false;
                        updateFavoriteButtonState();
                    }
                }

                @Override
                public void onFailure(Call<UserFavouriteDTO> call, Throwable t) {
                    Toast.makeText(DetailProductActivity.this, "Failed to get favorite information", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mapping() {
        vpListImage = findViewById(R.id.vpListImage);
        circleIndicator = findViewById(R.id.circleIndicator);
        imgGoBack = findViewById(R.id.imgGoBack);
        tvNameProduct = findViewById(R.id.tvNameProduct);
        imgTym = findViewById(R.id.imgTym);
        price = findViewById(R.id.price);
        imgMinus = findViewById(R.id.imgMinus);
        numberProduct = findViewById(R.id.numberProduct);
        imgPlus =findViewById(R.id.imgPlus);
        sumPrice = findViewById(R.id.sumPrice);
        btnAddToCart = findViewById(R.id.btnAddToCard);
        description = findViewById(R.id.description);
        seeMore = findViewById(R.id.seeMore);
        imgNext = findViewById(R.id.imgNext);
    }


    private void goBack() {
        finish();
    }
    private void minusAction() {
        String numString = numberProduct.getText().toString().trim();
        if (numString.equals("0")) {
            return;
        } else {
            int num = Integer.parseInt(numString);
            numberProduct.setText(String.valueOf(num - 1));
            num--;
            sumPrice.setText(formatMoney(String.valueOf(PRICE * num)));
        }

    }
    private void plusAction() {
        String numString = numberProduct.getText().toString().trim();
        int num = Integer.parseInt(numString);
        numberProduct.setText(String.valueOf(num + 1));
        num++;
        sumPrice.setText(formatMoney(String.valueOf(PRICE * num)));
    }
    private void addToCart(int productId) {
        String quantityString = numberProduct.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);

        cartService.getCartItems(userId).enqueue(new Callback<List<CartDTO>>() {
            @Override
            public void onResponse(Call<List<CartDTO>> call, Response<List<CartDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartDTO> cartItems = response.body();
                    boolean productExistsInCart = false;

                    for (CartDTO item : cartItems) {
                        if (item.getProductId() == productId) {
                            productExistsInCart = true;
                            int newQuantity = item.getQuantity() + quantity;
                            UpdateCartDTO updateCartDTO = new UpdateCartDTO();
                            updateCartDTO.setQuantity(newQuantity);
                            cartService.updateCartItemByUserIdAndProductId(userId, productId, updateCartDTO).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(DetailProductActivity.this, "Product quantity updated in cart", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetailProductActivity.this, "Failed to update cart", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(DetailProductActivity.this, "Failed to update cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }

                    if (!productExistsInCart) {
                        CartDTO newCartDTO = new CartDTO(userId, productId, quantity);
                        cartService.addToCart(newCartDTO).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(DetailProductActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetailProductActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(DetailProductActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(DetailProductActivity.this, "Failed to fetch cart items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartDTO>> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Failed to fetch cart items", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void seeMoreAction() {
        Intent intent = new Intent(this, RatingActivity.class);
        startActivity(intent);
    }
    public static String formatMoney(String moneyString) {
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

    public void getProductById(int id, ProductCallback callback) {
        try {
            Call<Product> call = productService.getProductById(id);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Product product = response.body();

                        callback.onProductFetched(product);
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable throwable) {
                    Toast.makeText(DetailProductActivity.this, "Failed to get product", Toast.LENGTH_LONG).show();
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
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

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 2000);
    }
}
