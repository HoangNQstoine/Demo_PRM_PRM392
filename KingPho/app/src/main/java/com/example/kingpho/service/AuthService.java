package com.example.kingpho.service;

import com.example.kingpho.dto.LoginDTO;
import com.example.kingpho.model.UserAuthentication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<UserAuthentication> signInUser(@Body LoginDTO loginDTO);

    @POST("auth/login")
    Call<ResponseBody> signInUserDebug(@Body LoginDTO loginDTO);
}
