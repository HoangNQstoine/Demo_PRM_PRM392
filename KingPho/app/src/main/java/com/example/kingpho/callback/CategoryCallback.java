package com.example.kingpho.callback;

import com.example.kingpho.model.Category;

import java.util.List;

public interface CategoryCallback {
    void onListCategoryFetched(List<Category> categories);
    void onError(Throwable throwable);
}
