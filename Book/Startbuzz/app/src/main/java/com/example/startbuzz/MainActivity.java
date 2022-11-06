package com.example.startbuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.startbuzz.Controller.CoffeAdapter;
import com.example.startbuzz.Controller.OpcionesAdapter;
import com.example.startbuzz.Interface.IElement;
import com.example.startbuzz.Model.Coffe;
import com.example.startbuzz.Model.Opcion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements IElement{

    private List<Opcion>opciones;
    private List<Coffe> coffees;

    //CREAR APLICACION TIENDA DE COFFEE
    //CICLO DE VIDA DE LA APP PARA VISIBILIDAD (onCreate(), onStop(), onRestart())

    public MainActivity(){
        super(R.layout.activity_main);
        this.coffees = new ArrayList<>();
        this.opciones = new ArrayList<>();
    }

    public List<Coffe> getCoffees() {
        return coffees;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //Creamos los datos
        crearDatosOpciones();
        //Creamos el recyclerview
        if (savedInstanceState == null){
            RecyclerView recyclerView = findViewById(R.id.rcOpciones);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new OpcionesAdapter(this,opciones, this));
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//            FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgListado, FragmentLista.class, null).commit();
//            manager.addFragmentOnAttachListener(this);
        }

    }

    private void crearDatosOpciones(){
        Opcion opcion;
        opcion = new Opcion("Drinks","Consigue tus bebidas preferidas");
        opciones.add(opcion);
        opcion = new Opcion("Food","Consigue tus platos preferidos");
        opciones.add(opcion);
        opcion = new Opcion("Tiendas","Encuentranos en estos lugares");
        opciones.add(opcion);
    }

    //Creamos los datos para la tienda
    public void crearDatosDrink(){
        Coffe coffe;
        coffe = new Coffe("Nombre " + 1, 5, "black_coffe", "Descripcion " + 1, "descripcionLarga", R.drawable.black_coffee);
        coffees.add(coffe);
        coffe = new Coffe("Nombre " + 2 , 7, "capuccino", "Descripcion " + 2, "Descripcion Larga", R.drawable.capuccino);
        coffees.add(coffe);
        coffe = new Coffe("Nombre " + 3 , 5, "milk_coffe", "Descripcion " + 3, "Descripcion Larga", R.drawable.milk_coffee);
        coffees.add(coffe);
    }


    @Override
    public void onSelectedElement(int position) {
        Coffe coffe = coffees.get(position);
        Opcion opcion = opciones.get(position);
    }
//
//    @Override
//    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
//        if (fragment.getId() == R.id.fragmentContainerView){
//
//        }
//    }
}