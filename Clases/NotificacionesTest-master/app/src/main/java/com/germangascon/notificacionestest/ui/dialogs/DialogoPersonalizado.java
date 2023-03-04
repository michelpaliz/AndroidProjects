package com.germangascon.notificacionestest.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.germangascon.notificacionestest.R;

import java.util.Objects;

/**
 * DialogoPersonalizado
 *
 * @author Germán Gascón
 * @version 0.1, 2021-03-04
 * @since 0.1, 2019-03-04
 **/
public class DialogoPersonalizado extends DialogFragment implements DialogInterface.OnClickListener {
    //Claves para el Bundle
    public static final String TEXTO_NOMBRE = "name";
    public static final String TEXTO_APELLIDOS = "surname";
    public static final String TEXTO_BOTON = "button";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_personalizado, null);
        builder.setView(layout);
        TextView tvNombre = layout.findViewById(R.id.tvNombre);
        TextView tvApellidos = layout.findViewById(R.id.tvApellidos);
        String nombre = "Nombre";
        String apellidos = "Apellidos";
        String boton = "Aceptar";
        if(arguments != null) {
            nombre = arguments.getString(TEXTO_NOMBRE, "Nombre");
            apellidos = arguments.getString(TEXTO_APELLIDOS, "Apellidos");
            boton = arguments.getString(TEXTO_BOTON, "Aceptar");
        }
        builder.setTitle("Perfil de " + nombre);
        tvNombre.setText(nombre);
        tvApellidos.setText(apellidos);
        builder.setPositiveButton(boton, this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.i("Diálogos", "Aceptar");
        dialogInterface.dismiss();
    }
}
