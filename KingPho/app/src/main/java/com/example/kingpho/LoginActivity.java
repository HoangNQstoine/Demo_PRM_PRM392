package com.example.kingpho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kingpho.dto.LoginDTO;
import com.example.kingpho.fragment.HomeFragment;
import com.example.kingpho.fragment.MainActivity;
import com.example.kingpho.model.UserAuthentication;
import com.example.kingpho.service.AuthService;
import com.example.kingpho.utils.JwtUtils;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private AuthService authService;

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvForgotPassword, tvCreate;
    CheckBox checkboxRememberMe;
    ImageView ivEye;
    boolean isPasswordVisible = false;
    SharedPreferences sharedPreferences;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(this);
        authService = retrofit.create(AuthService.class);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvCreate = findViewById(R.id.tvCreate);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
        ivEye = findViewById(R.id.ivEye);

//        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        loadPreferences();

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    LoginDTO loginDTO = new LoginDTO(username, password);
                    signInUser(loginDTO);
                }
            }
        });

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void signInUser(LoginDTO loginDTO) {
        try {
            Call<UserAuthentication> call = authService.signInUser(loginDTO);
            call.enqueue(new Callback<UserAuthentication>() {
                @Override
                public void onResponse(Call<UserAuthentication> call, Response<UserAuthentication> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserAuthentication userAuthentication = response.body();

                        String username = JwtUtils.getUsernameFromToken(userAuthentication.getToken());
                        if (username != null) {
                            SharedPrefManager.getInstance(LoginActivity.this).saveUser(userAuthentication);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Failed to get user", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<UserAuthentication> call, Throwable throwable) {
                    Toast.makeText(LoginActivity.this, "An error has been occurred!", Toast.LENGTH_LONG).show();
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            Log.e("Login Error", e.getMessage());
        }
    }
//
//    private void savePreferences(String username, String password) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("username", username);
//        editor.putString("password", password);
//        editor.putBoolean("rememberMe", true);
//        editor.apply();
//    }
//
//    private void clearPreferences() {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//    }

    private void loadPreferences() {
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");
            edtUsername.setText(username);
            edtPassword.setText(password);
            checkboxRememberMe.setChecked(true);
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivEye.setImageResource(R.drawable.baseline_remove_red_eye_24);
            isPasswordVisible = false;
        } else {
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
        }
    }
}
