package com.example.caminoalba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caminoalba.ui.menuItems.FragmentSettings;
import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity implements FragmentSettings.OnLanguageChangeListener {

    private Button btnSingUp, btnSingIn;
    private Intent intent;
    private NavigationDrawerActivity navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        setContentView(R.layout.activity_main);
        navigationDrawer = new NavigationDrawerActivity(); // Initialize the navigation drawer
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
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onLanguageChanged() {
        refreshNavigationDrawer();
    }

    public void refreshNavigationDrawer() {
        // Update the navigation drawer menu items here
        // For example, you can update the menu items by recreating the navigation drawer
        recreateNavigationDrawer();
    }

    private void recreateNavigationDrawer() {
        navigationDrawer.recreateNavigationDrawer();
    }

}
