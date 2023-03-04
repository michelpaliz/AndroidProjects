package com.germangascon.notificacionestest.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

/**
 * AlertDialogFrament
 *
 * @author Germán Gascón
 * @version 0.1, 2019-03-04
 * @since 0.1
 **/

public class AlertDialogFragment extends DialogFragment {
    //Claves para el Bundle
    public static final String TEXTO_TITULO = "titulo";
    public static final String TEXTO_MENSAJE = "mensaje";
    public static final String TEXTO_BOTON = "button";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        if(arguments != null) {
            builder.setPositiveButton(arguments.getString(TEXTO_BOTON, "OK"), null);
            builder.setTitle(arguments.getString(TEXTO_TITULO, "Título"));
            builder.setMessage(arguments.getString(TEXTO_MENSAJE, "Mensaje"));
        }
        else {
            builder.setPositiveButton("OK", null);
            builder.setTitle("Título");
            builder.setMessage("Mensaje");
        }

        return builder.create();
    }
}
