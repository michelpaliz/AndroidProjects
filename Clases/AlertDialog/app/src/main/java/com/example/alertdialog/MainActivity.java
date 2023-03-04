package com.example.alertdialog;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = findViewById(R.id.btnInfo);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Info").setPositiveButton("ok", (dialogInterface, i) -> {
            Toast.makeText(MainActivity.this,"Hola", Toast.LENGTH_SHORT).show();
            button.setOnClickListener(this);
            dialogInterface.cancel();
        }).create();
    }



    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(Dialog.INFO,"TITUTLO");
        bundle.putString(Dialog.POSITVE_BUTTON,"Positive");
        bundle.putString(Dialog.TOAST,"TOAST");
        Dialog dialog = new Dialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "TAG");
    }
}