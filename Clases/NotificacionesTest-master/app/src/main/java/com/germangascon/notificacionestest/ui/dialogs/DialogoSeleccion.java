package com.germangascon.notificacionestest.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;

/**
 * DialogoSeleccion
 *
 * @author Germán Gascón
 * @version 0.2, 2021-03-04
 * @since 0.1, 2019-03-04
 **/
public class DialogoSeleccion extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String[] items = {"Español", "Inglés", "Francés"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selección")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Diálogos", "Opción elegida: " + items[i]);
                    }
                });
        return builder.create();
    }
}
