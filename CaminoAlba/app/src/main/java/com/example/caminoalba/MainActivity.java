package com.example.caminoalba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnSingUp , btnSingIn;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        goToSingIn();
        goToSingUp();
    }

    //cargamos datos
    public void init(){
        btnSingUp = findViewById(R.id.btnSing_up_register);
        btnSingIn = findViewById(R.id.btnSingIn_register);
    }

    public void goToSingUp(){
        btnSingUp.setOnClickListener( v -> {
            intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void goToSingIn(){
        btnSingIn.setOnClickListener( v -> {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }


}