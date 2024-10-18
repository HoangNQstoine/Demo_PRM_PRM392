package com.example.kingpho.service;

import com.example.kingpho.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {

    @GET("categories")
    Call<List<Category>> getAllCategories();
}
