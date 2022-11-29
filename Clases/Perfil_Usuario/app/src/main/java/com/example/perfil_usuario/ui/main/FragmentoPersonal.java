package com.example.perfil_usuario.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.perfil_usuario.Modelo.Persona;
import com.example.perfil_usuario.R;

public class FragmentoPersonal extends Fragment {

    private Persona persona;

    private ImageView imageView;
    private TextView tvNombre;
    private TextView tvApellido;
    private TextView tvFechaNacimiento;
    private TextView tvDireccion;
    private TextView tvDNI;


    public interface IOnAttachListener{
        Persona getDatosPersonales();
    }

    public FragmentoPersonal() {
        super(R.layout.fragment_personal);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageView);
        tvNombre = view.findViewById(R.id.tvCif);
        tvApellido = view.findViewById(R.id.tvApellido);
        tvFechaNacimiento = view.findViewById(R.id.tvFechaNacimiento);
        tvDireccion = view.findViewById(R.id.tvDireccion);
        tvDNI = view.findViewById(R.id.tvId);
        if (persona != null){
            cargarDatos(persona);
        }
    }


    public void cargarDatos(Persona persona){
        imageView.setImageResource(R.drawable.man);
        tvNombre.setText(persona.getNombre());
        tvApellido.setText(persona.getApellido());
        tvFechaNacimiento.setText(persona.getFechaNacimiento());
        tvDireccion.setText(persona.getDireccion());
        tvDNI.setText(persona.getDni());
    }

    /**
     *
     * @param context es la actividad donde esta el fragmento.
     * el requisto es el que la actividad que implemente el framento tiene que implementar el Onattachlistener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        persona = iOnAttachListener.getDatosPersonales();
    }
}
