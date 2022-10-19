package com.example.fragment.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fragment.Adapter.AdaptadorCarta;
import com.example.fragment.Interface.IListener;
import com.example.fragment.Model.Letter;
import com.example.fragment.R;

import java.util.List;

public class FragmentDetalle extends Fragment {
    //Cuando el usurio clickea el fragmentListado llama al listener pero el listener es la activity que va a recibir esa accion.
    //La tarea del activity es saber cual correo ha sido y saber cual detalle tengo que pasar al dato, lo hara atraves de un clave que se hace con un bundle pero el valor sera este texto que sera este.
    public static final String TEXTO_CORREO="TEXTO_CORREO";
    private TextView tvDetalle;
    private String textCorreo;

    public FragmentDetalle(){
        super(R.layout.fragment_detalle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Buscamos nuestro listView
       assert getArguments() != null;
       textCorreo = getArguments().getString(TEXTO_CORREO);

    }
    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        tvDetalle = view.findViewById(R.id.tvDetalle);
        mostrarDetalle(textCorreo);
    }

    public void mostrarDetalle(String texto){
        tvDetalle.setText(texto);
    }

}
