package com.example.caminoalba;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    // ------ Vistas   -------
    private Button btnSingIn, btnHome, btnForgotPassword;
    private TextView tvSingUp, tvTitleSingIn;
    private EditText edEmail, edPassword;
    private Intent intent;
    private ProgressBar progressBar;
    private String email;
    private User userFound;
    // ------ Otras referencias    -------
    private Gson gson;
    private FirebaseAuth.AuthStateListener authStateListener;


    public void init() {

        // ------ Inicializamos vistas   -------
        btnSingIn = findViewById(R.id.btnSingIn_login);
        btnHome = findViewById(R.id.btnHome);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        progressBar = findViewById(R.id.progressBar);
        tvSingUp = findViewById(R.id.tvSingUp);
        tvTitleSingIn = findViewById(R.id.tvTitleSingUp);

        // ------ Inicializamos variables  -------
        // ------ Para obtener todos los datos del usuario  -------
        gson = new Gson();
        tvSingUp.setText(getText(R.string.text_dont_have_account));
        tvTitleSingIn.setText(getText(R.string.text_sing_in).toString().toUpperCase(Locale.ROOT));
        tvSingUp.setOnClickListener(v -> {
            intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }



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
        // Set up AuthStateListener
        setupAuthStateListener();
    }




    public void authenticateUser() {

        forgottenPassword();

        btnSingIn.setOnClickListener(v -> {
            if (!validateEmail() || !validatePassword()) {
                return;
            }
            email = edEmail.getText().toString();
            String password = edPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            logIn(email, password);
        });

    }

    private void setupAuthStateListener() {
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is logged in
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                userRef.orderByChild("email").equalTo(user.getEmail())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User loggedInUser = null;
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        loggedInUser = userSnapshot.getValue(User.class);
                                        if (loggedInUser != null) {
                                            break;
                                        }
                                    }
                                    if (loggedInUser != null) {
                                        // Store the user data in SharedPreferences
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = prefs.edit();
                                        String userStr = gson.toJson(loggedInUser);
                                        editor.putString("user", userStr);
                                        editor.apply();

                                        // Retrieve the profile data for the user
                                        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profiles").child(loggedInUser.getUser_id());
                                        User finalLoggedInUser = loggedInUser;
                                        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Profile profile = snapshot.getValue(Profile.class);
                                                if (profile != null) {
                                                    profile.setUser(finalLoggedInUser);
                                                    // Store the profile data in SharedPreferences
                                                    String profileStr = gson.toJson(profile);
                                                    editor.putString("profile", profileStr);
                                                    editor.apply();

                                                    // Navigate to the main activity
                                                    startActivity(new Intent(LoginActivity.this, NavigationDrawerActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, R.string.profile_not_found, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(LoginActivity.this, "Error loading profile data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.w(TAG, "loadProfile:onCancelled", error.toException());
                                            }
                                        });
                                    } else {
                                        Toast.makeText(LoginActivity.this, R.string.profile_not_found, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.profile_not_found, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, R.string.error_uploading_data + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "loadUser:onCancelled", error.toException());
                            }
                        });
            } else {
                // User is not logged in
                // You can show the login screen or take appropriate action
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }




    private void stopAuthStateListener() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Call stopAuthStateListener to stop listening for the authentication state
        stopAuthStateListener();
    }


    public void forgottenPassword() {
        // Reset password code
        btnForgotPassword.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.forgot_password);
            View view = LayoutInflater.from(this).inflate(R.layout.forgotten_password, null);
            EditText edEmail = view.findViewById(R.id.ed_email);
            Button btnResetPassword = view.findViewById(R.id.reset_password_button);
            btnResetPassword.setOnClickListener(view1 -> {
                String email1 = edEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email1)) {
                    Toast.makeText(LoginActivity.this, R.string.enter_email_address, Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email1)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, R.string.password_reset_send + email1, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.error_password_reset_send + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            builder.setView(view);
            builder.create().show();
        });
    }


    public void logIn(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                            userRef.orderByChild("email").equalTo(firebaseUser.getEmail())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                User user = null;
                                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                    user = userSnapshot.getValue(User.class);
                                                    if (user != null) {
                                                        break;
                                                    }
                                                }
                                                if (user != null) {
                                                    userFound = user;
                                                    // Save user data to SharedPreferences
                                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                    SharedPreferences.Editor editor = prefs.edit();
                                                    String userStr = gson.toJson(user);
                                                    editor.putString("user", userStr);
                                                    editor.putString("email", user.getEmail());
                                                    editor.apply();

                                                    // Get profile data for the user
                                                    DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profiles").child(user.getUser_id());
                                                    profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Profile profile = snapshot.getValue(Profile.class);
                                                            if (profile != null) {
                                                                profile.setUser(userFound);
                                                                // Save profile data to SharedPreferences
                                                                String profileStr = gson.toJson(profile);
                                                                editor.putString("profile", profileStr);
                                                                editor.apply();

                                                                // Start main activity
                                                                startActivity(new Intent(LoginActivity.this, NavigationDrawerActivity.class));
                                                                finish();
                                                            } else {
                                                                Toast.makeText(LoginActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(LoginActivity.this, R.string.error_uploading_data + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                            Log.w(TAG, "loadProfile:onCancelled", error.toException());
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(LoginActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(LoginActivity.this, R.string.error_uploading_data + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.w(TAG, "loadUser:onCancelled", error.toException());
                                        }
                                    });
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, R.string.auth_failed + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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
        } else if (password.length() < 6) {
            edPassword.setError(R.string.password_must_be_6_long+"");
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