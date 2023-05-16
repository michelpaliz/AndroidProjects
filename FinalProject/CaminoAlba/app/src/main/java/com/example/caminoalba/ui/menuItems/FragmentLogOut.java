package com.example.caminoalba.ui.menuItems;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.caminoalba.LoginActivity;
import com.example.caminoalba.MainActivity;
import com.example.caminoalba.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class FragmentLogOut extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button confirmButton = view.findViewById(R.id.btnConfirm);
        confirmButton.setOnClickListener(v -> {
            // Call the logOut() method to log the user out
            logOut();
        });
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();

        // Clear stored user data or authentication tokens
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("user");
        editor.remove("email");
        editor.apply();

        // Redirect the user to the login screen
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

}