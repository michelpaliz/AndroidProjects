package com.germangascon.retrofitsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.germangascon.retrofitsample.activities.login.ProfileActivity;

public class FragmentAdminFunctions extends Fragment {


    private Intent goToAdminPage;

    public FragmentAdminFunctions() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnaddAuthor = view.findViewById(R.id.btnAnyadirAutor);
        Button btnAddCategory = view.findViewById(R.id.btnAnyadirCategoria);
        Button btnAddPhrase = view.findViewById(R.id.btnAnyadirFrase);
        Button btnEdit = view.findViewById(R.id.btnModificarFrase);
        Button btnBack = view.findViewById(R.id.btnVolverAdmin);
        goToAdminPage = new Intent(getContext(), Admin.class);

        //Add functions
        btnaddAuthor.setOnClickListener(v -> {
            goToAdminPage.putExtra("option", 1);
            startActivity(goToAdminPage);
        });

        btnAddCategory.setOnClickListener(v -> {
            goToAdminPage.putExtra("option", 2);
            startActivity(goToAdminPage);
        });

        btnAddPhrase.setOnClickListener(v -> {
            goToAdminPage.putExtra("option", 3);
            startActivity(goToAdminPage);

        });

        //Edit functions

        btnEdit.setOnClickListener(v -> {
            goToAdminPage.putExtra("option", 4);
            startActivity(goToAdminPage);
        });

        btnBack.setOnClickListener(v -> {
            Intent goToMainPage = new Intent(getContext(), ProfileActivity.class);
            startActivity(goToMainPage);
        });


    }


}