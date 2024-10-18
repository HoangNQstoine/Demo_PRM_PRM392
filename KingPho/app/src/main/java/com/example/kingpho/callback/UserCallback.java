package com.example.kingpho.callback;


import com.example.kingpho.model.User;


public interface UserCallback {
    void onUserFetched(User user);
    void onError(Throwable throwable);
}
