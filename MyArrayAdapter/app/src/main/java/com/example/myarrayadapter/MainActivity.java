package com.example.myarrayadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myarrayadapter.Adpater.AdaptadorUsuario;
import com.example.myarrayadapter.Model.Usuario;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //view
    private ListView verLista;
    //Construc the data source
    private ArrayList<Usuario> listaUsuarios;
    private AdaptadorUsuario adaptador;
    //add item to adapter
    private Usuario nuevoUsuario;
    private Usuario nuevoUsuario1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializamos las variables
        //Migra el adaptador a ListView
        verLista = (ListView) findViewById(R.id.listarAdaptadores);
        listaUsuarios = new ArrayList<>();
        nuevoUsuario = new Usuario("Michael", "Denia");
        nuevoUsuario1 = new Usuario("Jhoan","Denia");
        adaptador = new AdaptadorUsuario(this, listaUsuarios);
        verLista.setAdapter(adaptador);
        //Ocupa los datos en una lista
        //Anyade el usuario al adaptador
         adaptador.add(nuevoUsuario);
         adaptador.add(nuevoUsuario1);


    }

    @Override
    public void onClick(View view) {

    }
}