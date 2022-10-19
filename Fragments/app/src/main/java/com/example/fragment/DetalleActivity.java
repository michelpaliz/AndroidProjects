package com.example.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.fragment.Fragment.FragmentDetalle;

public class DetalleActivity extends AppCompatActivity {

    public static final String EXTRA_TEXTO="EXTRA_TEXTO";

    public DetalleActivity(){
        super(R.layout.activity_detalle);
    }


    protected void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            String textoCorreo = getIntent().getStringExtra(EXTRA_TEXTO);
            Bundle bundle = new Bundle();
            bundle.putString(FragmentDetalle.TEXTO_CORREO, textoCorreo);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgDetalle, FragmentDetalle.class, bundle).commit();
        }
    }

}
