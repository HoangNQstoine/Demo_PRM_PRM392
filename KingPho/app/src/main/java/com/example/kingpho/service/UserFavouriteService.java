package com.example.kingpho.service;

import com.example.kingpho.dto.UserFavouriteDTO;
import com.example.kingpho.model.UserFavourite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserFavouriteService {

    @POST("favorites")
    Call<UserFavouriteDTO> addToFavourites(@Body UserFavouriteDTO userFavouriteDTO);

    @GET("favorites/{userId}")
    Call<List<UserFavouriteDTO>> getUserFavourites(@Path("userId") Integer userId);

    @GET("favorites/{userId}/{productId}")
    Call<UserFavouriteDTO> getUserFavourite(@Path("userId") Integer userId, @Path("productId") Integer productId);

    @DELETE("favorites/{id}")
    Call<Void> deleteUserFavourite(@Path("id") Integer id);
}
