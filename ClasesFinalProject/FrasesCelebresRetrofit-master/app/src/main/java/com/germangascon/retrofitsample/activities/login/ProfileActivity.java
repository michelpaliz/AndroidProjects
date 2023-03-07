package com.germangascon.retrofitsample.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.germangascon.retrofitsample.FragmentContainer;
import com.germangascon.retrofitsample.MainActivity;
import com.germangascon.retrofitsample.MainPage;
import com.germangascon.retrofitsample.R;
import com.germangascon.retrofitsample.models.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String NOMBRE = "NOMBRE";
    public static boolean SAVEUSER;

    private User user;
    private Toolbar toolbar;

    private TextView tvEmail, tvFirstName, tvLastName, tvType;
    private Button btnSingOut, btnFragment, btnFragmentAdmin;


//En esta clase decidimos 2 caminos
    //- admin
    //- consultas

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(NOMBRE, user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        setSupportActionBar(toolbar);


        if (savedInstanceState != null) {
            user = (User) savedInstanceState.getSerializable(NOMBRE);
            if (!user.getType().equalsIgnoreCase("admin")) {
                btnFragmentAdmin.setVisibility(View.INVISIBLE);
                goToAdminPage();

            }

        }

        SAVEUSER = true;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName, lastName, email, password, type;

        firstName = (prefs.getString("usernamePref", ""));
        lastName = (prefs.getString("lastNamePref", ""));
        password = (prefs.getString("passwordPref", ""));
        email = (prefs.getString("emailPref", ""));
        type =(prefs.getString("typePref", ""));
        user = new User(firstName, lastName, email, password, type);

        tvFirstName.setText(user.getName());
        tvLastName.setText(user.getLastName());
        tvEmail.setText(user.getEmail());
        tvType.setText(user.getType());


        if (user.getType().equalsIgnoreCase("admin")){
            goToAdminPage();
        }else{
            btnFragmentAdmin.setVisibility(View.INVISIBLE);
        }

        goToMainPage();
        signOut();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.iPreferencias) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FragmentContainer())
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvEmail = findViewById(R.id.tvEmail);
        btnSingOut = findViewById(R.id.btnSingOut_profile);
        btnFragment = findViewById(R.id.btnListPhrases);
        btnFragmentAdmin = findViewById(R.id.btnFunctionsFrases);
        tvType = findViewById(R.id.tvType);
        toolbar = findViewById(R.id.toolbar);
    }


    public void signOut() {
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

    public void goToMainPage() {
        btnFragment.setOnClickListener(v -> {
            Intent goToMainPage = new Intent(this, MainPage.class);
            startActivity(goToMainPage);
            finish();
        });
    }

    public void goToAdminPage() {
        btnFragmentAdmin.setOnClickListener(v -> {
            String value = "admin";
            Intent intent = new Intent(ProfileActivity.this, MainPage.class);
            intent.putExtra("key", value);
            startActivity(intent);
        });
    }

}