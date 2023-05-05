package com.example.caminoalba.ui.menuItems;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.google.gson.Gson;

import java.util.Locale;

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
                if (selectedLanguage.equalsIgnoreCase("spanish")) {
                    String langCode = "es";
                    setLocale(langCode); // Set the new language
                }else{
                    String langCode = "en";
                    setLocale(langCode); // Set the new language
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
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