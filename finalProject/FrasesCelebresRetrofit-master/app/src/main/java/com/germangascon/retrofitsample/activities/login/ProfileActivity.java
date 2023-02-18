package com.germangascon.retrofitsample.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.germangascon.retrofitsample.MainActivity;
import com.germangascon.retrofitsample.R;

public class ProfileActivity extends AppCompatActivity {

    TextView tvEmail, tvFirstName, tvLastName;
    Button btnSingOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        //Get Intent Extra Value
        String firstName = getIntent().getStringExtra("first_name");
        String lastName = getIntent().getStringExtra("last_name");
        String email = getIntent().getStringExtra("email");
        //Set Profile Values;
        tvFirstName.setText(firstName);
        tvLastName.setText(lastName);
        tvEmail.setText(email);

        signOut();
    }

    public void init(){
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvEmail = findViewById(R.id.tvEmail);
        btnSingOut = findViewById(R.id.btnSingOut_profile);

    }

    public void signOut(){
        btnSingOut.setOnClickListener(v -> {
            //Set Profile Values;
            tvFirstName.setText(null);
            tvLastName.setText(null);
            tvEmail.setText(null);
            //Return user back to the home page
            Intent goToHome = new Intent(this, MainActivity.class);
//            goToHome.putExtra("logout","successful");
            startActivity(goToHome);
            finish();
        });
    }



}