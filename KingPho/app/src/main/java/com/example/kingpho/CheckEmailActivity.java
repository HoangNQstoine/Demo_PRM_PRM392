package com.example.kingpho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckEmailActivity extends AppCompatActivity {

    EditText edtCode;
    Button btnVerifyCode;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);

        edtCode = findViewById(R.id.edtCode);
        btnVerifyCode = findViewById(R.id.btnVerifyCode);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckEmailActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edtCode.getText().toString().trim();

                if (code.isEmpty()) {
                    Toast.makeText(CheckEmailActivity.this, "Please enter the code", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CheckEmailActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
