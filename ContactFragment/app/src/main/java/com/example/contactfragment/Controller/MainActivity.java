package com.example.contactfragment.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.contactfragment.Interface.IContactoListener;
import com.example.contactfragment.Model.Contacto;
import com.example.contactfragment.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IContactoListener {

    private List<Contacto> contactos;


    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recyclerview);
        inicializador();
        Log.d("Lista", contactos.toString());
        ContactoAdapter adapter = new ContactoAdapter(this, contactos, this);
        RecyclerView recyclerView = findViewById(R.id.rvLista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }


    public void inicializador(){
        contactos = new ArrayList<>();
        Contacto contacto;
        for (int i = 0; i < 5; i++) {
            contacto = new Contacto("Nombre " + i , "Apellido " + i , "Nacimiento" + i ,"Direccion " + i , "Empresa "+ i, "telefono "  + i , "telefono " + i);
            contactos.add(contacto);
        }

    }


    @Override
    public void onContactoSeleccionado(Contacto contacto) {

    }
}