package com.example.caminoalba.ui.menuItems;

import android.content.Intent;
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

import com.example.caminoalba.LoginActivity;
import com.example.caminoalba.MainActivity;
import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Objects;


public class FragmentSettings extends Fragment  {

    private SharedPreferences sharedPreferences;
    private TextView tvName, tvEmail;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private Spinner spinnerLanguage;
    private OnLanguageChangeListener languageChangeListener;



    public interface OnLanguageChangeListener {
        void onLanguageChanged();
    }


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
        etConfirmPassword = view.findViewById(R.id.passwordEditTextConfirm);

        // Set the initial text of menu items
        updateMenuItems();

        // Change password button click listener
        changePassword();

        // Language spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);



        // Set the spinner to the saved language if available
        String savedLanguage = sharedPreferences.getString("language", "");
        if (!savedLanguage.isEmpty()) {
            int spinnerPosition = adapter.getPosition(savedLanguage);
            spinnerLanguage.setSelection(spinnerPosition, false); // Set default language without triggering onItemSelected
        }

// Language spinner selection listener
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isFirstSelection = true; // Flag to track initial selection

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                } else {
                    String selectedLanguage = parent.getItemAtPosition(position).toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("language", selectedLanguage);
                    editor.apply();
                    String langCode;
                    if (selectedLanguage.equalsIgnoreCase("spanish")) {
                        langCode = "es";
                    } else {
                        langCode = "en";
                    }
                    setLocale(langCode);
                    updateMenuItems(); // Update menu items after language change
                    // Notify the hosting activity about the language change
                    if (languageChangeListener != null) {
                        languageChangeListener.onLanguageChanged();
                    }
                }
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
            String confirmPassword = etConfirmPassword.getText().toString();

            // Check if old password is correct
            assert mUser != null;
            if (TextUtils.isEmpty(oldPassword)) {
                Toast.makeText(requireContext(), R.string.password_updated_successfully, Toast.LENGTH_SHORT).show();
                return;
            }
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(mUser.getEmail()), oldPassword);
            mUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Check if new password is at least 6 characters long
                    if (newPassword.length() >= 6) {
                        // Check if new password and confirm password match
                        if (newPassword.equals(confirmPassword)) {
                            // Update the user's password
                            mUser.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(requireContext(), R.string.password_updated_successfully, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), R.string.password_updated_unsuccessfully + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(requireContext(), R.string.password_dont_match, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), R.string.password_lenght, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.incorrect_password, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



    private void updateMenuItems() {
        // Update the text of menu items based on the current language
        String userStr = sharedPreferences.getString("profile", "");
        Gson gson = new Gson();
        Profile profile = gson.fromJson(userStr, Profile.class);
        tvEmail.setText(profile.getUser().getEmail());
        tvName.setText(profile.getFirstName() + " " + profile.getLastName());
        // Update other menu items here
        // ...
    }


    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        // Update the configuration for the current resources
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Save the selected language in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.apply();

        // Restart the activity to apply the language change
        // Restart the activity to apply the language change
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }


}