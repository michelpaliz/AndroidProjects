package com.example.caminoalba.ui.menuItems;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Objects;

public class FragmentSettings extends Fragment {

    private SharedPreferences sharedPreferences;
    private TextView tvName, tvEmail;
    private EditText etOldPassword, etNewPassword;
    private Button btnChangePassword;
    private Spinner spinnerLanguage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String userStr = sharedPreferences.getString("profile", "");
        Gson gson = new Gson();
        Profile profile = gson.fromJson(userStr, Profile.class);
        tvName = view.findViewById(R.id.userNameEditText);
        tvEmail = view.findViewById(R.id.userEmailEditText);
        etOldPassword = view.findViewById(R.id.oldPasswordEditText);
        etNewPassword = view.findViewById(R.id.passwordEditText);
        btnChangePassword = view.findViewById(R.id.changePasswordButton);
        spinnerLanguage = view.findViewById(R.id.languageSpinner);
        // ======================================== //
        tvEmail.setText(profile.getUser().getEmail());
        tvName.setText(profile.getFirstName().substring(0, 1).toUpperCase() + profile.getFirstName().substring(1) + " " + profile.getLastName().substring(0, 1).toUpperCase() + profile.getLastName().substring(1));

        // =========== CHANGE PASSWORD ============= //

        changePassword();

        // ============ SELECT THE LANGUAGE ======== //

        // Populate the language spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Set the spinner to the saved language if available
        String savedLanguage = sharedPreferences.getString("language", "");
        if (!savedLanguage.isEmpty()) {
            int spinnerPosition = adapter.getPosition(savedLanguage);
            spinnerLanguage.setSelection(spinnerPosition);
        }

        // Save the selected language when a new language is selected
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("language", selectedLanguage);
                editor.apply();
                String langCode;// Set the new language
                if (selectedLanguage.equalsIgnoreCase("spanish")) {
                    langCode = "es";
                } else {
                    langCode = "en";
                }
                setLocale(langCode); // Set the new language
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }


    private void changePassword() {
        FirebaseAuth mAuth;
        FirebaseUser mUser;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        // Prompt the user to re-provide their sign-in credentials
        btnChangePassword.setOnClickListener(v -> {

            // =========== CHANGE PASSWORD ============= //
            String oldPassword = etOldPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();

            // Check if old password is correct
            assert mUser != null;
            if (TextUtils.isEmpty(oldPassword)) {
                Toast.makeText(requireContext(), "Please enter your old password", Toast.LENGTH_SHORT).show();
                return;
            }
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(mUser.getEmail()), oldPassword);
            mUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Check if new password is at least 6 characters long
                    if (newPassword.length() >= 6) {
                        // Update the user's password
                        mUser.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Error updating password: " + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(requireContext(), "New password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Incorrect old password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}