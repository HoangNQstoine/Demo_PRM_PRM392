package com.example.kingpho.service;

import com.example.kingpho.dto.RegisterDTO;
import com.example.kingpho.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("users/register")
    Call<User> registerUser(@Body RegisterDTO registerDTO);

    @GET("users/username/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @PUT("user/update/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);
}
