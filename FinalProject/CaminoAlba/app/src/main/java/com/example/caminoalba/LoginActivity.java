package com.example.caminoalba;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caminoalba.helpers.EmailHelper;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    // ------ Vistas   -------
    private Button btnSingIn, btnHome;
    private EditText edEmail, edPassword;
    private Intent intent;
    private ProgressBar progressBar;
    private String email;
    private SharedPreferences prefs;
    // ------ Otras referencias    -------
    private Gson gson;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
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

            logIn(email, password);

        });

    }

    public void logIn(String email, String password) {
//        ProgressDialog progressDialog = ProgressDialog.show(this, "", "Logging in...", true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    String userEmail = firebaseUser.getEmail();
                    DatabaseReference userRef = database.getReference("users");

                    Query query = userRef.orderByChild("email").equalTo(userEmail);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    User user = userSnapshot.getValue(User.class);
                                    if (user != null) {
                                        // Save user data to SharedPreferences
                                        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        editor = prefs.edit();
                                        String userStr = gson.toJson(user);
                                        editor.putString("user", userStr);
                                        editor.putString("email", user.getEmail());
                                        editor.apply();

                                        // Get profile data for the user
                                        DatabaseReference profileRef = database.getReference("profiles").child(String.valueOf(user.getUser_id()));

                                        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Profile profile = snapshot.getValue(Profile.class);
                                                if (profile != null) {
                                                    // Save profile data to SharedPreferences
                                                    String profileStr = gson.toJson(profile);
                                                    editor.putString("profile", profileStr);
                                                    editor.apply();

                                                    // Start main activity
                                                    Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Profile not found for this user.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(LoginActivity.this, "Error loading profile data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.w(TAG, "loadProfile:onCancelled", error.toException());
                                            }
                                        });
                                        return;
                                    }
                                }
                                Toast.makeText(LoginActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Error loading user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "loadUser:onCancelled", error.toException());
                        }
                    });
                }
            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

        //    ******** VALIDATION ***********

        public boolean validateEmail () {
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

        public boolean validatePassword () {
            String password = edPassword.getText().toString();
            if (password.isEmpty()) {
                edPassword.setError("Cannot be empty");
                return false;
            } else if (password.length() < 6) {
                edPassword.setError("Password must be at least 6 characters long");
                return false;
            } else {
                edPassword.setError(null);
                return true;
            }
        }

//    ********** Button Actions **********

        public void backHome () {
            btnHome.setOnClickListener(v -> {
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        public void goToSingInAcct () {
            btnSingIn.setOnClickListener(v -> {
                intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            });
        }


    }