package com.example.examplefragment.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.example.examplefragment.Interface.IContactoListener;
import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;

import java.util.List;


public class MainActivity extends AppCompatActivity implements IContactoListener, FragmentOnAttachListener {


    //FrgListado === Activity_main.xml
    //FrgDetalle === Detalle_activity.xml
    //fragmentrecyclerView.xml { // item_recyclerview_lista.xml // item_recyclerview_detalle.xml }


    private List<Contacto> contactos;
    private boolean isTablet;
    private FragmentDetalle frgDetalle;
    private FragmentLista frgListado;


    public MainActivity() {
        super(R.layout.activity_main);
        frgDetalle = null;
        frgListado = null;
        isTablet = false;
    }

    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTablet = findViewById(R.id.FrgDetalle)!= null;

        Log.d("Tablet", "es 1"+ isTablet);

        Parser parser = new Parser(this) ;
        if (parser.startParser()){
            this.contactos = parser.getContactos();
        }

        if (savedInstanceState == null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgListado, FragmentLista.class, null).commit();
            manager.addFragmentOnAttachListener(this);
        }

    }

    @Override
    public void onContactoSeleccionado(int posicion) {
        Contacto contactoRecogido = contactos.get(posicion);
        if (isTablet){
            frgDetalle.mostrarDetalle(contactoRecogido);
        }else{
            Log.d("contexto", getApplicationContext().toString());
            Intent i = new Intent(this, DetalleActivity.class);
            Log.d("Tablet", "es 2 "+ isTablet);
            i.putExtra(DetalleActivity.EXTRA_NAME, contactoRecogido);
            Log.d("intentpesonalizado", i.toString());
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
                manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgDetalle, FragmentDetalle.class, bundle).commit();

            }

        }

        if (fragment.getId() == R.id.FrgDetalle){
            frgDetalle = (FragmentDetalle) fragment;
        }

    }


}