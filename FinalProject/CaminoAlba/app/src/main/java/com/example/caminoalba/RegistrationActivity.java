package com.example.caminoalba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.caminoalba.Config.Config;
import com.example.caminoalba.Config.EmailHelper;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnSingUp, btnHome;
    private EditText edFirstName, edLastName, edEmail, edPassword, edConfirm;
    private ProgressBar progressBar;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        backHome();
        authenticateUser();
    }

    public void init() {
        btnSingUp = findViewById(R.id.btnSingUp_signup);
        edEmail = findViewById(R.id.edEmail_signup);
        btnHome = findViewById(R.id.btnHome_singup);
        edFirstName = findViewById(R.id.edName_signup);
        edLastName = findViewById(R.id.edLastName_signup);
        edConfirm = findViewById(R.id.edConfirm_signup);
        edPassword = findViewById(R.id.edPassword_signup);
        progressBar = findViewById(R.id.progress_circular);
    }

    public void backHome() {
        btnHome.setOnClickListener(v -> {
            intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void authenticateUser() {

        btnSingUp.setOnClickListener(v -> {
            //Check for Errors
            if (!validateFirstName() || !validateLastName()
                    || !validateEmail() || !validatePassword()) {
                return;
            }
            //End for checking errors
            //Instantiate the request queue:
//            String url = "http://192.168.9.127:9080/api/v1/user/register";
            RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_API,
                    response -> {
                        if (response.equalsIgnoreCase("success")) {
                            //we are removing the data to avoid more post petitions of new data that the user can put more.
                            edFirstName.setText(null);
                            edLastName.setText(null);
                            edEmail.setText(null);
                            edPassword.setText(null);
                            edConfirm.setText(null);
                            Toast.makeText(this, "Registration successfully", Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(this, "Registrarion unsuccessfully", Toast.LENGTH_SHORT).show();
            }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
//                    return super.getParams();
                    Map<String, String> params = new HashMap<>();
                    params.put("first_name", edFirstName.getText().toString());
                    params.put("last_name", edLastName.getText().toString());
                    params.put("email", edEmail.getText().toString());
                    params.put("password", edPassword.getText().toString());
                    return params;
                }
            };//End of string request object
            requestQueue.add(stringRequest);
        });


    }


    //    ******** VALIDATION ***********

    public boolean validateFirstName() {
        String strFirstName = edFirstName.getText().toString();
        if (strFirstName.isEmpty()) {
            edFirstName.setError("Cannot be empty");
            return false;
        } else {
            edFirstName.setError(null);
            return true;
        }
    }

    public boolean validateLastName() {
        String strLastName = edLastName.getText().toString();
        if (strLastName.isEmpty()) {
            edLastName.setError("Cannot be empty");
            return false;
        } else {
            edLastName.setError(null);
            return true;
        }

    }

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
        String passwordConfirm = edConfirm.getText().toString();
        if (passwordConfirm.isEmpty()) {
            edConfirm.setError("Cannot be empty");
        }
        if (password.isEmpty()) {
            edPassword.setError("Cannot be empty");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            edPassword.setError("Passwords do not match");
            return false;
        } else {
            edPassword.setError(null);
            edConfirm.setError(null);
            return true;
        }

    }


}