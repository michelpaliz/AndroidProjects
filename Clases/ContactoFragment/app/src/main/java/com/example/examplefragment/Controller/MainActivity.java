package com.example.examplefragment.Controller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.examplefragment.Interface.IContactoListener;
import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;

import java.io.Serializable;
import java.util.List;


public class  MainActivity  extends AppCompatActivity implements FragmentLista.IOnAttachListener, IContactoListener, FragmentDetalle.IOnAttachListener{


    private static final String KEY_CONTACTOS = "com.examplefragment.contactos";
    private static final String KEY_CONTACTO = "com.examplefragment.contactos";

    private List<Contacto> contactos;
    private Contacto contacto;
    private boolean isTablet = false;
    private FragmentDetalle fragmentDetalle;


    //Antes de destruir la app llamamos a esta funcion para guardar las variables creadas previas.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_CONTACTOS, (Serializable) contactos);
        outState.putSerializable(KEY_CONTACTO,contacto);
    }

    //Creamos la app
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        //******* PARA USO NORMAL *********//
            //Comenzamos
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //Si no es primera vez que creamos la app entramos aqui
            if (savedInstanceState != null) {
                contactos = (List<Contacto>) savedInstanceState.getSerializable(KEY_CONTACTOS);
                contacto = (Contacto) savedInstanceState.getSerializable(KEY_CONTACTO);
            }

        //****** PARA TABLET ********* //
             //Si existe el layout nos da true
            isTablet = findViewById(R.id.FrgDetalle)!= null;
            if (isTablet){
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentDetalle = (FragmentDetalle) fragmentManager.findFragmentById(R.id.FrgDetalle);
            }

    }

    //El lisetener para los contactos seleccionados
    @Override
    public void onContactoSeleccionado(int posicion) {
        //Hay que utilizar el mismo contacto global para no utilizar el mismo contacto repetido.
        contacto = contactos.get(posicion);
        if (isTablet){
//            ((FragmentDetalle) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.FrgDetalle))).mostrarDetalle(contactos.get(posicion));
            setTitle(contacto.getNombre());
            fragmentDetalle.mostrarDetalle(contacto);

        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            //Reemplazamos el layout del listado con el del fragment que metimos en el constructor.
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.FrgListado,FragmentDetalle.class, null).commit();

//            Intent i = new Intent(this, DetalleActivity.class);
//            i.putExtra(DetalleActivity.EXTRA_NAME, contactoRecogido);
//            startActivity(i);
        }

    }

    public void cargarDatos() {
        Parser parser = new Parser(this) ;
        if (parser.startParser()){
            this.contactos = parser.getContactos();
        }else{
            throw new NullPointerException();
        }
    }


    @Override
    public Contacto getContactoDetalle() {
        if (contactos == null){
            cargarDatos();
        }
        if (contacto == null){
            //Tenemos que coger si o si el primero contacto para poder utilizarlo en nuestro fragmentoDetalle
            contacto = contactos.get(0);
        }
        return contacto;

    }

    @Override
    public List<Contacto> getContactosListado() {
        if (contactos == null){
            cargarDatos();
        }
        return contactos;
    }
}