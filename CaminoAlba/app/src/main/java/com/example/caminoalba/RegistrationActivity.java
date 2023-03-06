package com.example.caminoalba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnSingUp, btnHome;
    private EditText edName, edLastName, edEmail, edPassword, edConfirmPassword;
    private ProgressBar progressBar;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        backHome();
    }

    public void init(){
        btnSingUp = findViewById(R.id.btnSingUp_signup);
        btnHome = findViewById(R.id.btnHome_singup);
        edName = findViewById(R.id.edName_signup);
        edLastName = findViewById(R.id.edLastName_signup);
        edConfirmPassword = findViewById(R.id.edConfirm_signup);
        edPassword = findViewById(R.id.edPassword);
        progressBar = findViewById(R.id.progress_circular);
    }

    public void backHome(){
        btnHome.setOnClickListener( v -> {
            intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}