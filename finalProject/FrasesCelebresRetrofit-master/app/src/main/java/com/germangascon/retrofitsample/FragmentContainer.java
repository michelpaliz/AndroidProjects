package com.germangascon.retrofitsample;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.germangascon.retrofitsample.activities.login.ProfileActivity;


public class FragmentContainer extends Fragment {



    private Button btnVolver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FragmentPreferencias())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnVolver = view.findViewById(R.id.btnVolver_container);

        btnVolver.setOnClickListener(v -> {
            Intent goToMainPage = new Intent(getContext(), ProfileActivity.class);
            startActivity(goToMainPage);
        });

//        btnSaveUser.setOnClickListener(v -> {
//            SAVEUSER = true;
//            Toast.makeText(getContext(), "User Saved for next session", Toast.LENGTH_SHORT).show();
//        });


    }
}