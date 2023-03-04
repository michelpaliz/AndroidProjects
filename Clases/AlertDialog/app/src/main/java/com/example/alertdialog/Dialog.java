package com.example.alertdialog;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {

    public static final String INFO = "info";
    public static final String POSITVE_BUTTON = "Positive";
    public static final String TOAST = "Positive";

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        assert bundle != null;
        String tittle = bundle.getString(INFO);
        String positiveButton = bundle.getString(POSITVE_BUTTON);
        String toast = bundle.getString(TOAST);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
        alertDialog.setTitle("Tittle").setPositiveButton("ok", (dialogInterface, i) -> {
            Toast.makeText(requireContext(), "HOLA", Toast.LENGTH_SHORT).show();
            dialogInterface.cancel();
        }).create().show();

        return alertDialog.create();

//        return super.onCreateDialog(savedInstanceState);

    }


}


