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

public class FragmentAddAuthor extends Fragment {


    private Button btnaddAuthor, btnEditAuthor, btnAddPhrase, btnEditPhrase, btnAddCategory, btnEditCategory ;

    public FragmentAddAuthor() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnaddAuthor = view.findViewById(R.id.btnAnyadirAutor);
        btnAddCategory = view.findViewById(R.id.btnAnyadirCategoria);
        btnAddPhrase = view.findViewById(R.id.btnAnyadirFrase);

        btnEditAuthor = view.findViewById(R.id.btnModificarAutor);
        btnEditCategory = view.findViewById(R.id.btnModificarCategoria);
        btnEditPhrase = view.findViewById(R.id.btnModificarFrase);


        //Add functions

        btnaddAuthor.setOnClickListener( v -> {
            Intent goToAdminPage = new Intent(getContext(), Admin.class);
            goToAdminPage.putExtra("option",1);
            startActivity(goToAdminPage);
        });

        btnAddCategory.setOnClickListener(v -> {
            Intent goToAdminPage = new Intent(getContext(), Admin.class);
            goToAdminPage.putExtra("option",2);
            startActivity(goToAdminPage);
        });

        btnAddPhrase.setOnClickListener(v -> {
            Intent goToAdminPage = new Intent(getContext(), Admin.class);
            goToAdminPage.putExtra("option",3);
            startActivity(goToAdminPage);

        });

        //Edit functions

        btnEditPhrase.setOnClickListener(v -> {

        });

        btnEditCategory.setOnClickListener(v -> {

        });

        btnEditAuthor.setOnClickListener(v -> {

        });



    }




}