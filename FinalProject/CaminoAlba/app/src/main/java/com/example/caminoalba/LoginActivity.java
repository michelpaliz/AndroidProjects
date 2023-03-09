package com.example.caminoalba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.caminoalba.Config.Config;
import com.example.caminoalba.Config.EmailHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    private Boolean SAVEUSER = false;

    private Button btnSingIn, btnHome;
    private EditText edEmail, edPassword;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        backHome();
        authenticateUser();
    }

    public void init() {
        btnSingIn = findViewById(R.id.btnSingIn_login);
        btnHome = findViewById(R.id.btnHome);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);

    }

    public void authenticateUser() {

        btnSingIn.setOnClickListener(v -> {

            if (!validateEmail() || !validatePassword()) {
                return;
            }

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //Set Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("email", edEmail.getText().toString());
            params.put("password", edPassword.getText().toString());


            //Set JSON request Object
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.LOGIN_API, new JSONObject(params), (Response.Listener<JSONObject>) response -> {
                try {
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();

                    //Get values from Response Object
                    String first_name = (String) response.get("first_name");
                    String last_name = (String) response.get("last_name");
                    String email = (String) response.get("email");
                    String password = (String) response.get("password");
                    String type = (String) response.get("type");
//                String type = (String) response.get("type");

                    //Set Intent Actions;
                    intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                    intent.putExtra("first_name", first_name);
                    intent.putExtra("last_name", last_name);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("type", type);
                    SAVEUSER = true;

                    //Start Activity
                    startActivity(intent);
                    finish();
                    //Pass Values to profile activity

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("usernamePref", first_name);
                    editor.putString("lastNamePref", last_name);
                    editor.putString("passwordPref", password);
                    editor.putString("emailPref", email);
//                editor.putString("typePref", type);
                    editor.putString("ip", Config.CLIENT_API);
                    editor.putString("port", Config.PORT_NUMBER);
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }, error -> {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            });//End of the set Request Object
            requestQueue.add(jsonObjectRequest);

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