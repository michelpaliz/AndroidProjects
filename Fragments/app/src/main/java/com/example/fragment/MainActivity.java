package com.example.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.content.Intent;
import android.os.Bundle;

import com.example.fragment.Fragment.FragmentDetalle;
import com.example.fragment.Fragment.FragmentListado;
import com.example.fragment.Interface.IListener;
import com.example.fragment.Model.Letter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IListener, FragmentOnAttachListener {
    private boolean tabletLayout;
    private FragmentDetalle frgDetalle;

    public MainActivity() {
      super(R.layout.activity_main);
      frgDetalle = null;
      tabletLayout = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabletLayout = findViewById(R.id.FrgDetalle) !=  null;
        //Esto es asincrono,
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgDetalle, FragmentListado.class,null).commit();
        //Anyadimos un listener para saber cuando un frg es cargado o descargado para que nos avise.
        manager.addFragmentOnAttachListener(this);


    }

    @Override
    public void letterSelected(Letter position) {
        if(tabletLayout){
            frgDetalle.mostrarDetalle(position.getDetalle());
        }else{
            //cargamos una nueva activity
            Intent intent = new Intent(this,FragmentDetalle.class);
            intent.putExtra(FragmentDetalle.TEXTO_CORREO, position.getDetalle());
            startActivity(intent);
        }
    }

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if(fragment.getId() == R.id.FrgListado){
            FragmentListado frgListado = (FragmentListado) fragment;
            frgListado.setListener(this);
            if(tabletLayout){
                Bundle bundle = new Bundle();
                bundle.putString(FragmentDetalle.TEXTO_CORREO,frgListado.getCartas().get(0).getDetalle());
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgListado,FragmentDetalle.class, bundle).commit();
            }
        }
        if(fragment.getId() ==  R.id.FrgDetalle){
            frgDetalle = (FragmentDetalle) fragment;
        }
    }


}