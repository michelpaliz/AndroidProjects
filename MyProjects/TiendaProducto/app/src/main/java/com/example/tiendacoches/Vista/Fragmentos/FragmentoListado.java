package com.example.tiendacoches.Vista.Fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendacoches.Interfaz.IListenerProducto;
import com.example.tiendacoches.Model.Leche;
import com.example.tiendacoches.R;
import com.example.tiendacoches.Vista.Adaptadores.AdapatadorProducto;

import java.util.ArrayList;
import java.util.List;

public class FragmentoListado extends Fragment {


    //************ UTILIZAMOS ESTE FRAGMENTO PARA INSERTAR EL RECYCLERVIEW ****************//

    //ATRIBUTOS
    private IListenerProducto listenerProducto;
    private List<Leche> listaLeches;

    //Interfaz para cargar los productos en nuestra lista desde otra clase;
    public interface IOnAttachListener{
        List<Leche> getListado();
    }

    public FragmentoListado() {
        super(R.layout.recyclerview_producto);
        listaLeches = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //INSERTAMOS EN ADPATADOR NUESTRO RECYCLERVIEW
        RecyclerView recyclerView = view.findViewById(R.id.rcListaProducto);

        if (listaLeches != null && listenerProducto !=null){
            recyclerView.setAdapter(new AdapatadorProducto(listaLeches, listenerProducto)) ;
        }else{
            throw new NullPointerException();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    /**
     *
     * @param context lo utilizaremos para referenciar a nuestra interfaz y listener
     *                ya que si no lo hacer saltaria un null pointer exeption en nuestro
     *                contexto ya que no lo contraria.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerProducto = (IListenerProducto) context;
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        listaLeches = iOnAttachListener.getListado();

    }








}
