package com.germangascon.retrofitsample;



import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


public class FragmentPreferencias extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    EditTextPreference ip, port;
    Preference username, password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.options, rootKey);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
        ip = findPreference("ip");
        port = findPreference("port");
        username = findPreference("iUsernamePref");
        password = findPreference("iPasswordPref");
        assert ip != null;
        ip.setText(prefs.getString("ip", ""));

        assert port != null;
        port.setText(prefs.getString("port", ""));

//        assert username != null;
//        username.setSummary(prefs.getString("usernamePref", ""));
//
//        assert password != null;
//        password.setSummary(prefs.getString("passwordPref", ""));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        ip.setText(sharedPreferences.getString("ip", ""));
        port.setText(sharedPreferences.getString("port", ""));
        username.setSummary(sharedPreferences.getString("usernamePref", ""));
        password.setSummary(sharedPreferences.getString("passwordPref", ""));
    }
}