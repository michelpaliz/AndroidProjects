package com.germangascon.retrofitsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Button;

import com.germangascon.retrofitsample.activities.login.ProfileActivity;

import java.util.Objects;

public class FragmentoConsultas extends Fragment {

    private int opcion;
    private FragmentList fragmentList;

    public FragmentoConsultas() {
        super(R.layout.fragemento_consultas);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnAutores = view.findViewById(R.id.btnFrasesPorAutor);
        Button btnCategorias = view.findViewById(R.id.btnFrasesPorCategoria);
        Button btnVolver = view.findViewById(R.id.btnVolver);
        Button btnFrases = view.findViewById(R.id.btnFrases);


        btnVolver.setOnClickListener(v -> {
            Intent goToMainPage = new Intent(getContext(), ProfileActivity.class);
            startActivity(goToMainPage);
        });

        //2 TIPOS DE BUSQUEDAS

        btnFrases.setOnClickListener(v -> {
            fragmentList = new FragmentList();
            opcion = 3;
            Bundle args = new Bundle();
            args.putInt("option", 3);
            fragmentList.setArguments(args);
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.frgConsultas, fragmentList, null)
                    .commit();

        });

        btnAutores.setOnClickListener(v -> {
            fragmentList = new FragmentList();
            opcion = 1;
            Bundle args = new Bundle();
            args.putInt("option", 1);
            fragmentList.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.frgConsultas, fragmentList, null)
                    .commit();
        });

        btnCategorias.setOnClickListener(v -> {
            fragmentList = new FragmentList();
            opcion = 2;
            Bundle args = new Bundle();
            args.putInt("option", 2);
            fragmentList.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.frgConsultas, fragmentList, null)
                    .commit();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public int getOpcion() {
        return opcion;
    }
}