package com.germangascon.retrofitsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;

import com.germangascon.retrofitsample.activities.login.ProfileActivity;

public class FragmentConsultas extends Fragment {

    private int opcion;

    public FragmentConsultas() {
        super(R.layout.fragemento_consultas);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnAutores = view.findViewById(R.id.btnFrasesPorAutor);
        Button btnCategorias = view.findViewById(R.id.btnFrasesPorCategoria);
        Button btnVolver = view.findViewById(R.id.btnVolver);

//        FragmentManager manager = requireActivity().getSupportFragmentManager();
//        manager.beginTransaction()
//                .setReorderingAllowed(true)
//                .addToBackStack(null)
//                .replace(R.id.frgConsultas,FragmentList.class, null)
//                .commit();


        btnVolver.setOnClickListener(v -> {
            Intent goToMainPage = new Intent(getContext(), ProfileActivity.class);
            startActivity(goToMainPage);
        });

        //2 TIPOS DE BUSQUEDAS

        btnAutores.setOnClickListener(v -> {
            Fragment fragmentList = new FragmentList();
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
            opcion = 2;
            Fragment fragmentList = new FragmentList();
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