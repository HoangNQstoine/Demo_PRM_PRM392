package com.example.kingpho.utils;

import android.content.Context;
import android.content.Intent;

import com.example.kingpho.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private Context context;

    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String accessToken = SharedPrefManager.getInstance(context).getUser().getToken();

        Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken);

        Response response = chain.proceed(builder.build());

        if (response.code() == 401) { // Token expired
            // Clear user session and redirect to login
            SharedPrefManager.getInstance(context).clear();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}
