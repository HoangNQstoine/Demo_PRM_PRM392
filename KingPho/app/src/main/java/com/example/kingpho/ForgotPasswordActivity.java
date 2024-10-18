package com.example.kingpho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtPhone;
    Button btnSendRequest;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtPhone = findViewById(R.id.edtPhone);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString().trim();

                if (validatePhone(phone)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset request sent to " + phone, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, CheckEmailActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validatePhone(String phone) {
        String vietnamPhonePattern = "^(03|05|07|08|09)\\d{8}$";

        if (phone.isEmpty()) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter your phone", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phone.matches(vietnamPhonePattern)) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
