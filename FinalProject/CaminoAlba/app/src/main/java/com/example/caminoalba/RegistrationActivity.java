package com.example.caminoalba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caminoalba.helpers.EmailHelper;
import com.example.caminoalba.helpers.Utils;
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.models.dto.UserAndProfileRequest;
import com.example.caminoalba.rest.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnSingUp, btnHome;
    private EditText edFirstName, edLastName, edEmail, edPassword, edConfirm;
    private Intent intent;
    private int cont = 0;


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

            IAPIservice iapIservice = RestClient.getInstance();

            if (!validateFirstName() || !validateLastName()
                    || !validateEmail() || !validatePassword()) {
                return;
            }

            int id = cont++;

//            String verificationCode =
//
//            System.out.println("Esto es la verificacion " + verificationCode);

            User user = new User(id, edFirstName.getText().toString(), edLastName.getText().toString(), edEmail.getText().toString(),
                    edPassword.getText().toString(), "user", Utils.generateVerificationCode(), false);


            Profile profile = new Profile(id, edFirstName.getText().toString(), edLastName.getText().toString(), null, null, null, user);
            UserAndProfileRequest userWithProfile = new UserAndProfileRequest(user, profile);

            Call<UserAndProfileRequest> call = iapIservice.createUserWithProfile(userWithProfile);
            call.enqueue(new Callback<UserAndProfileRequest>() {
                @Override
                public void onResponse(Call<UserAndProfileRequest> call, Response<UserAndProfileRequest> response) {
                    Toast.makeText(RegistrationActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<UserAndProfileRequest> call, Throwable t) {
                    Toast.makeText(RegistrationActivity.this, "Registrarion unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });

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