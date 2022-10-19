package com.example.examplefragment.Controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;

import java.util.Objects;

public class DetalleActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.examplefragment.EXTRA_NAME";

    public DetalleActivity(){
        super(R.layout.activity_detalle);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            Contacto contacto = (Contacto) Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_NAME));
            Bundle bundle = new Bundle();
            bundle.putSerializable(FragmentDetalle.EXTRA_CONTACTO, contacto);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgDetalle, FragmentDetalle.class,bundle).commit();
        }

    }



}
