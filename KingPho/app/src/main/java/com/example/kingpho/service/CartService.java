package com.example.kingpho.service;

import com.example.kingpho.dto.CartDTO;
import com.example.kingpho.dto.UpdateCartDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartService {

    @POST("cart")
    Call<Void> addToCart(@Body CartDTO cartDTO);

    @GET("cart/{userId}")
    Call<List<CartDTO>> getCartItems(@Path("userId") int userId);

    @PUT("cart/{cartId}")
    Call<Void> updateCartItem(@Path("cartId") int cartId, @Body CartDTO cartDTO);

    @PUT("cart/user/{userId}/product/{productId}")
    Call<Void> updateCartItemByUserIdAndProductId(@Path("userId") int userId, @Path("productId") int productId, @Body UpdateCartDTO cartDTO);

    @DELETE("cart/{cartId}")
    Call<Void> deleteCartItem(@Path("cartId") int cartId);
}
