package com.germangascon.retrofitsample.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.germangascon.retrofitsample.MainActivity;
import com.germangascon.retrofitsample.R;
import com.germangascon.retrofitsample.helpers.EmailHelper;

import java.util.HashMap;
import java.util.Map;

public class SingUpActivity extends AppCompatActivity {

    private Button btnHome, btnSignUp;
    private TextView tvSignIn;
    private EditText edFirstName, edLastName, edEmail, edPassword, edConfirm;
    private Spinner spinner;
    private String[] campos ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        init();
        registrationProcess();
        goHome();
        goToSingInAcct();

    }

    public void init() {
        edFirstName = findViewById(R.id.tvName_signup);
        edLastName = findViewById(R.id.tvLastName_signup);
        edEmail = findViewById(R.id.tvEmail_signup);
        edPassword = findViewById(R.id.tvPassword_signup);
        edConfirm = findViewById(R.id.tvConfirm_signup);
        campos = getResources().getStringArray(R.array.type);
        btnSignUp = findViewById(R.id.btnSingUp_signup);
        btnHome = findViewById(R.id.btnHome_singup);
        tvSignIn = findViewById(R.id.tvSingIn_singup);

        spinner = findViewById(R.id.spinner);
    }

    // ********* ACTION BUTTONS ***********

    public void goToSingInAcct() {
        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SingInActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void goHome() {
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

//   ********* VALIDATION PROCESS ***********

    public void registrationProcess() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.layout, campos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        btnSignUp.setOnClickListener(v -> {

            String result = spinner.getSelectedItem().toString();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            //Check for Errors
            if (!validateFirstName() || !validateLastName()
                    || !validateEmail() || !validatePassword()) {
                return;
            }
            //End for checking errors

            //Instantiate the request queue:
            RequestQueue requestQueue = Volley.newRequestQueue(SingUpActivity.this);
            //The url posting to:
//            String url = "http://192.168.1.15:9080/api/v1/user/register";
            String url = "http://192.168.9.127:9080/api/v1/user/register";

            //String Request Object
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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
                    params.put("type", result);
                    return params;
                }
            };//End of string request object
            requestQueue.add(stringRequest);
        });

        //End of Process Form Fields Method

    }

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