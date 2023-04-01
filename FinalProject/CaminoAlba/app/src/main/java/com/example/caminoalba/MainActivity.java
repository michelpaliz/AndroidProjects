package com.example.caminoalba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Button btnSingUp, btnSingIn;
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
    public void init() {
        btnSingUp = findViewById(R.id.btnSing_up_register);
        btnSingIn = findViewById(R.id.btnSingIn_register);
    }

    public void goToSingUp() {
        btnSingUp.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void goToSingIn() {
        btnSingIn.setOnClickListener(v -> {

//            if (Config.USER_SAVED) {
//                //Si no es primera vez que el usuario ya ha entrado se guarda sesion.
//                intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
//            } else {
//                //Si no pues tendra que logearse.
//                intent = new Intent(MainActivity.this, LoginActivity.class);
//            }
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();


        });
    }
}