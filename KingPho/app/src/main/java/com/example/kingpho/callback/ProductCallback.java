package com.example.kingpho.callback;

import com.example.kingpho.model.Product;

import java.util.List;

public interface ProductCallback {
    void onListProductFetched(List<Product> productLists);
    void onProductFetched(Product product);
    void onError(Throwable throwable);
}
