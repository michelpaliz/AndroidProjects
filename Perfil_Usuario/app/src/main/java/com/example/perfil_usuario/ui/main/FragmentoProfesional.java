package com.example.perfil_usuario.ui.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.perfil_usuario.Modelo.Persona;
import com.example.perfil_usuario.Modelo.PersonaDatosProfesionales;
import com.example.perfil_usuario.R;

public class FragmentoProfesional extends Fragment {

    private PersonaDatosProfesionales datosProfesionales;

    public interface IOnAttachListener{
        PersonaDatosProfesionales getDatosProfesionales();
    }

    private TextView tvNombreEmpresa;
    private TextView tvCif;
    private TextView tvDireccion;
    private TextView tvPaginaWeb;
    private TextView tvEmail;

    public FragmentoProfesional() {
        super(R.layout.fragment_profesional);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNombreEmpresa = view.findViewById(R.id.tvNombreEmpresa);
        tvCif = view.findViewById(R.id.tvCif);
        tvDireccion = view.findViewById(R.id.tvDireccionProfesional);
        tvPaginaWeb = view.findViewById(R.id.tvPaginaWeb);
        tvEmail = view.findViewById(R.id.tvEmail);
        if (datosProfesionales !=null){
            cargarDatos(datosProfesionales);
        }

    }


    public void cargarDatos(PersonaDatosProfesionales personaDatosProfesionales){
        tvNombreEmpresa.setText(personaDatosProfesionales.getNombreEmpresa());
        tvCif.setText(personaDatosProfesionales.getCif());
        tvEmail.setText(personaDatosProfesionales.getEmail());
        tvDireccion.setText(personaDatosProfesionales.getDireccion());
        tvPaginaWeb.setText(personaDatosProfesionales.getPaginaWeb());
    }


    /**
     *
     * @param context es la actividad donde este el fragmento.
     * el requisto es el que la actividad que implemente el framento tine que imp[lementar el Onattachlistener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        datosProfesionales = iOnAttachListener.getDatosProfesionales();

    }
}
