package com.example.tiendacoches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.tiendacoches.Interfaz.IListenerProducto;
import com.example.tiendacoches.Model.Leche;
import com.example.tiendacoches.Vista.Fragmentos.FragmentoDetalle;
import com.example.tiendacoches.Vista.Fragmentos.FragmentoListado;
import com.example.tiendacoches.Vista.Fragmentos.FragmentoVenta;
import com.example.tiendacoches.Vista.Parser.Parser;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IListenerProducto, FragmentoListado.IOnAttachListener, FragmentoDetalle.IOnAtachListener, FragmentoVenta.IOnAtachListener {


    private static final String KEY_PRODUCTOS = "com.examplefragment.contactos";
    private static final String KEY_PRODUCTO= "com.examplefragment.contactos";

    //EN EL MAIN VAMOS A CONTROLAR CUANDO LLAMAMOS A NUESTRO FRAGMENTOS;
    private Toolbar toolbar;
    private FragmentoListado fragmentoListado;
    private FragmentManager fragmentManager;
    private FragmentoDetalle fragmentDetalle;
    private FragmentoVenta fragmentoVenta;
    private boolean isTablet;
    private boolean isVenta;
    private Leche leche;

    private List<Leche>listadoLeche;

    public MainActivity() {
        super(R.layout.fragmento_container);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_PRODUCTOS, (Serializable) listadoLeche);
        outState.putSerializable(KEY_PRODUCTO,leche);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null){
            Leche[] lista;
            lista = (Leche[]) savedInstanceState.getSerializable(KEY_PRODUCTOS);
            listadoLeche.toArray(lista);
            leche = (Leche) savedInstanceState.getSerializable(KEY_PRODUCTO);
        }

        fragmentManager = getSupportFragmentManager();
        isTablet = findViewById(R.id.FragmentoDetalle) != null;
        isVenta = findViewById(R.id.FragmentoVenta) != null;
        if (isTablet){
            fragmentDetalle = (FragmentoDetalle) fragmentManager.findFragmentById(R.id.FragmentoDetalle);
        }
        if(isVenta){
            fragmentoVenta = (FragmentoVenta) fragmentManager.findFragmentById(R.id.FragmentoVenta);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public List<Leche> getListado() {
        if (listadoLeche == null){
            cargarDatos();
        }
        return listadoLeche;
    }



    public void cargarDatos(){
        Parser parser = new Parser(getApplicationContext());
        if (parser.startParser()){
            listadoLeche = parser.getListaLeches();
        }else{
            throw new NullPointerException();
        }
    }


    /**
     *
     * @param bindingAdapterPosition
     */
    @Override
    public void onProductoSeleccionado(int bindingAdapterPosition) {
        leche = listadoLeche.get(bindingAdapterPosition);
        if (isTablet){
            setTitle(leche.getMarca());
            fragmentDetalle.cargarDatos(leche);
        }else{
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fragmentContainer,FragmentoDetalle.class,null).commit();
        }

        if (isVenta){
            setTitle(leche.getMarca());
            fragmentoVenta.cargarDatos(leche);
        }else{
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fragmentContainer,FragmentoVenta.class,null).commit();
        }


    }

    /**
     *
     * @param item insertamos los items que hemos creado en nuestro menu.xml
     * @return los items creados
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        String backStackName = FragmentoListado.class.getName();
        int id = item.getItemId();
        if (id == R.id.itemJuego1){
            //            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack()
            Toast.makeText(this,"Consultar Stock", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack("replacement").replace(R.id.fragmentContainer, new FragmentoListado(),null).commit();
            return true;
        }else if(id == R.id.itemJuego2){
            Toast.makeText(this,"Venta de leche", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack("replacement").replace(R.id.fragmentContainer, FragmentoListado.class,null).commit();
            return  true;
        }

        return false;
    }

    @Override
    public Leche getLeche() {
        if (listadoLeche == null){
            cargarDatos();
        }
        if (leche == null){
            leche = listadoLeche.get(0);
        }

        return leche;
    }
}