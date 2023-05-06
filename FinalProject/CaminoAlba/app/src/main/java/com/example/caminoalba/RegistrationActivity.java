package com.example.caminoalba;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caminoalba.helpers.EmailHelper;
import com.example.caminoalba.helpers.Utils;
import com.example.caminoalba.models.AccountStatus;
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnSingUp, btnHome;
    private EditText edFirstName, edLastName, edEmail, edPassword, edConfirm;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
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
        TextView tvTitleSignUp = findViewById(R.id.tvTitleSingUp);
        TextView tvAlreadySingUp = findViewById(R.id.tvAlreadySingUp);
        tvAlreadySingUp.setText(getText(R.string.text_already_have_account));
        tvTitleSignUp.setText(getText(R.string.text_sign_up).toString().toUpperCase());
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

            if (!validateFirstName() || !validateLastName()
                    || !validateEmail() || !validatePassword()) {
                return;
            }

            String email = edEmail.getText().toString();
            String password = edPassword.getText().toString();

            // Check if email is already in use
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        boolean emailExists = !Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();
                        if (emailExists) {
                            Toast.makeText(RegistrationActivity.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // If email is not in use, create a new user
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, task2 -> {
                                    if (task2.isSuccessful()){
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            String uid = user.getUid();
                                            User newUser = new User(uid, email, "user", Utils.generateVerificationCode(), false, AccountStatus.ACTIVE);
                                            Profile profile = new Profile(uid, edFirstName.getText().toString(), edLastName.getText().toString(), null, null, null, newUser);
                                            Blog blog = new Blog(uid, null, true, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), profile);

                                            // Set the user ID in the objects
                                            profile.getUser().setUser_id(uid);
                                            blog.setBlog_id(uid);

                                            // Register the user in Firebase Realtime Database
                                            registerUserFirebase(newUser, profile, blog);
                                            Toast.makeText(RegistrationActivity.this, "Firebase has registered your user.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this, "User couldn't be registered.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    Log.w(TAG, "createUserWithEmail:failure", e);
                                });
                    });
        });
    }



    public void registerUserFirebase(User user, Profile profile, Blog blog) {

        // Get reference to the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Convert user_id to String
        String user_id = user.getUser_id();

        // Save user object to Firebase
        DatabaseReference userRef = databaseReference.child("users").child(user_id);
        userRef.setValue(user, (error, ref) -> {
            if (error != null) {
                // Handle error
                Log.e(TAG, "Error saving user to Firebase: " + error.getMessage());
                Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            // Save profile object to Firebase
            DatabaseReference profileRef = databaseReference.child("profiles").child(user_id);
            profileRef.setValue(profile, (error1, ref1) -> {
                if (error1 != null) {
                    // Handle error
                    Log.e(TAG, "Error saving profile to Firebase: " + error1.getMessage());
                    Toast.makeText(RegistrationActivity.this, error1.getMessage(), Toast.LENGTH_SHORT).show();
                    // Rollback the user write
                    userRef.removeValue();
                    return;
                }

                // Save blog object to Firebase
                DatabaseReference blogRef = databaseReference.child("blogs").child(user_id);
                blogRef.setValue(blog, (error2, ref2) -> {
                    if (error2 != null) {
                        // Handle error
                        Log.e(TAG, "Error saving blog to Firebase: " + error2.getMessage());
                        Toast.makeText(RegistrationActivity.this, error2.getMessage(), Toast.LENGTH_SHORT).show();
                        // Rollback the user and profile writes
                        userRef.removeValue();
                        profileRef.removeValue();
                        return;
                    }

                    // All data saved successfully
                    Toast.makeText(RegistrationActivity.this, "Firebase has registered your user", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User saved to Firebase!");
                });
            });
        });

        // Attach a listener to check if the user has permission to write to the database
        DatabaseReference permissionRef = databaseReference.child(".info/authenticated");
        permissionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && Boolean.TRUE.equals(snapshot.getValue(Boolean.class))) {
                    // User has permission to write to the database
                    Toast.makeText(RegistrationActivity.this, "User has permission to write to the database", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User has permission to write to the database");
                } else {
                    // User does not have permission to write to the database
                    Toast.makeText(RegistrationActivity.this, "User does not have permission to write to the database", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User does not have permission to write to the database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e(TAG, "Error checking database permission: " + error.getMessage());
                Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
        } else if (password.length() < 6) {
            edPassword.setError("Password must be at least 6 characters long");
            return false;
        } else {
            edPassword.setError(null);
            edConfirm.setError(null);
            return true;
        }

    }


}