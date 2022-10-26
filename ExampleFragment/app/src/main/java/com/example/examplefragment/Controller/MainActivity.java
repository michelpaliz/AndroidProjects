package com.example.examplefragment.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.example.examplefragment.Interface.IContactoListener;
import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements IContactoListener, FragmentOnAttachListener {


    private static final String KEY_CONTACTOS = "com.examplefragment.contactos";

    private List<Contacto> contactos;
    private boolean isTablet;
    private FragmentLista frgListado;


    public MainActivity() {
        super(R.layout.activity_main);
        frgListado = null;
        isTablet = false;
    }

    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTablet = findViewById(R.id.FrgDetalle)!= null;

        if (savedInstanceState == null) {
            Parser parser = new Parser(this) ;
            if (parser.startParser()){
                this.contactos = parser.getContactos();
            }
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgListado, FragmentLista.class, null).commit();
            manager.addFragmentOnAttachListener(this);
        }else{
            contactos = (List<Contacto>) savedInstanceState.getSerializable(KEY_CONTACTOS);
            frgListado = (FragmentLista) getSupportFragmentManager().findFragmentById(R.id.FrgListado);
            if (frgListado != null){
                frgListado.setContextListener(contactos, this);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_CONTACTOS, (Serializable) contactos);
    }

    @Override
    public void onContactoSeleccionado(int posicion) {
        Contacto contactoRecogido = contactos.get(posicion);
        if (isTablet){
//            frgDetalle.mostrarDetalle(contactoRecogido);
            ((FragmentDetalle) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.FrgDetalle))).mostrarDetalle(contactos.get(posicion));
        }else{
            Intent i = new Intent(this, DetalleActivity.class);
            i.putExtra(DetalleActivity.EXTRA_NAME, contactoRecogido);
            startActivity(i);
        }

    }

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        Log.d("fragmentid ", String.valueOf(fragment.getId()));
//        2131230726
        Log.d("frglistadoid ", String.valueOf(R.id.FrgListado));
//        2131230726
        if(fragment.getId() == R.id.FrgListado){
            frgListado = (FragmentLista) fragment;
            //aqui recibe la accion(listener)
            frgListado.setContextListener(contactos, this);
            if (isTablet) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(FragmentDetalle.EXTRA_CONTACTO, contactos.get(0));
                FragmentManager manager = getSupportFragmentManager();
                //Si usamos el replace no seria necesario utilizar el codigo de abajo
                manager.beginTransaction().setReorderingAllowed(true).replace(R.id.FrgDetalle, FragmentDetalle.class, bundle).commit();
            }

        }


//        if (fragment.getId() == R.id.FrgDetalle){
//            frgDetalle = (FragmentDetalle) fragment;
//        }

    }


}