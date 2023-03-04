package com.germangascon.retrofitsample.activities.login;

//import static com.germangascon.retrofitsample.FragmentContainer.SAVEUSER;

import static com.germangascon.retrofitsample.activities.login.ProfileActivity.SAVEUSER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.germangascon.retrofitsample.MainActivity;
import com.germangascon.retrofitsample.R;
import com.germangascon.retrofitsample.helpers.EmailHelper;
import com.germangascon.retrofitsample.rest.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SingInActivity extends AppCompatActivity {

    private Button btnHome,btnSingIn;
    private TextView tvSignIn;
    private EditText edEmail, edPassword;
    private Intent goToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        init();

        if (SAVEUSER){
            startActivity(goToProfile);
            finish();
        }

        btnSingIn.setOnClickListener(v -> authenticateUser());
        goHome();
        goToSingInAcct();

    }

    public void init() {
        btnHome = findViewById(R.id.btnHome);
        tvSignIn = findViewById(R.id.tvSingUp);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnSingIn = findViewById(R.id.btnSingIn_SingIn);
        goToProfile = new Intent(SingInActivity.this, ProfileActivity.class);
    }


    public void authenticateUser() {


        if (!validateEmail() || !validatePassword()) {
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.9.127:9080/api/v1/user/login";


        //Set Parameters
        HashMap<String, String> params = new HashMap<>();
        params.put("email", edEmail.getText().toString());
        params.put("password", edPassword.getText().toString());



        //Set JSON request Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), (Response.Listener<JSONObject>) response -> {
            try {
                //Get values from Response Object
                String first_name = (String) response.get("first_name");
                String last_name = (String) response.get("last_name");
                String email = (String) response.get("email");
                String password = (String) response.get("password");
                String type = (String) response.get("type");

                //Set Intent Actions;
                goToProfile.putExtra("first_name", first_name);
                goToProfile.putExtra("last_name", last_name);
                goToProfile.putExtra("email", email);
                goToProfile.putExtra("password", password);
                goToProfile.putExtra("type", type);
                SAVEUSER = true;

                //Start Activity
                startActivity(goToProfile);
                finish();
                //Pass Values to profile activity

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("usernamePref", first_name);
                editor.putString("lastNamePref", last_name);
                editor.putString("passwordPref", password);
                editor.putString("emailPref", email);
                editor.putString("typePref", type);
                editor.putString("ip", RestClient.IP);
                editor.putString("port", String.valueOf(RestClient.PORT));
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

    public void goHome() {
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(SingInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void goToSingInAcct() {
        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SingUpActivity.class);
            startActivity(intent);
            finish();
        });
    }


}