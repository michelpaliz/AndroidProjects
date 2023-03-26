package com.example.caminoalba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.caminoalba.config.Config;
import com.example.caminoalba.helpers.EmailHelper;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.services.Service;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // ------ Vistas   -------
    private Button btnSingIn, btnHome;
    private EditText edEmail, edPassword;
    private Intent intent;
    private Service service;
    private ProgressBar progressBar;
    private List<User> userList;
    private User user;
    private Profile profile;
    private String email;
    private SharedPreferences prefs;
    // ------ Otras referencias    -------
    private Gson gson;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        backHome();
        authenticateUser();
    }

    public void init() {

        // ------ Inicializamos vistas   -------
        btnSingIn = findViewById(R.id.btnSingIn_login);
        btnHome = findViewById(R.id.btnHome);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        progressBar = findViewById(R.id.progressBar);
        // ------ Inicializamos variables  -------
        // ------ Para obtener todos los datos del usuario  -------
        service = new Service();
        user = new User();
        profile = new Profile();
        userList = new ArrayList<>();
        gson = new Gson();
    }

    public void authenticateUser() {
        btnSingIn.setOnClickListener(v -> {
            if (!validateEmail() || !validatePassword()) {
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            email = edEmail.getText().toString();
            String password = edPassword.getText().toString();

            // Set Parameters
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);

            // Set JSON request Object
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.LOGIN_API, new JSONObject(params), response -> {
                try {
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();

                    // Get values from Response Object
                    email = (String) response.get("email");
                    getCurrentUser();
                    Config.USER_SAVED = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }, error -> {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        });

    }

    //    ******** OBTENER TODOS LOS DATOS DEL USUARIO ***********
    //    ******** MANIPULACION DE DATOS PARA EL USUARIO ***********

    public void getCurrentUser() {
        service.getUsersFromRest(new Service.APICallback() {

            @Override
            public void onSuccess() {
                userList = service.getUsers();

                User userSelected = new User();
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getEmail().equalsIgnoreCase(email)) {
                        userSelected = userList.get(i);
                        System.out.println("esto es user " + userSelected);
                    }
                }
                user = userSelected;
                //Pass Values to profile activity
                prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = prefs.edit();
                String userStr = gson.toJson(userSelected);
                editor.putString("user", userStr);
                editor.putString("email", userSelected.getEmail());


                service.getProfilesFromRest(new Service.APICallback() {
                    @Override
                    public void onSuccess() {
                        List<Profile> profileList = service.getProfiles();
                        // Manipulate the users data here
                        Profile profileSelected = new Profile();
                        System.out.println("Item one by one " + profileList);
                        for (int i = 0; i < profileList.size(); i++) {
                            if (profileList.get(i).getProfile_id() == user.getUser_id()) {
                                profileSelected = profileList.get(i);
                            }
                        }
                        profile = profileSelected;
                        // Save profile data to SharedPreferences
                        String profileStr = gson.toJson(profile);
                        editor.putString("profile", profileStr);
                        editor.apply();
                        //Set Intent Actions;
                        progressBar.setVisibility(View.GONE);
                        intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                        //Start Activity
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        System.out.println(error);
                        // Handle error
                    }
                });
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }


    //    ******** VALIDATION ***********

    public boolean validateEmail() {
        String strEmail = edEmail.getText().toString();
        if (strEmail.isEmpty()) {
            edEmail.setError("Cannot be empty");
            return false;
        } else if (!EmailHelper.isValidEmail(strEmail)) {
            edEmail.setError("Email not valid");
            return false;
        } else {
            edEmail.setError(null);
            return true;
        }

    }


    public boolean validatePassword() {
        String password = edPassword.getText().toString();
        if (password.isEmpty()) {
            edPassword.setError("Cannot be empty");
            return false;
        } else {
            edPassword.setError(null);
            return true;
        }

    }


//    ********** Button Actions **********

    public void backHome() {
        btnHome.setOnClickListener(v -> {
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void goToSingInAcct() {
        btnSingIn.setOnClickListener(v -> {
            intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }


}