package com.example.caminoalba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button btnSingIn, btnHome;
    private EditText edEmail, edPassword;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        backHome();
    }

    public void init(){
        btnSingIn = findViewById(R.id.btnSingIn_login);
        btnHome = findViewById(R.id.btnHome);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
    }

    public void backHome(){
        btnHome.setOnClickListener(v -> {
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


}