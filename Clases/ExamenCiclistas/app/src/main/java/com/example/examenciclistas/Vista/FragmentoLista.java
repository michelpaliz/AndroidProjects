package com.example.examenciclistas.Vista;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenciclistas.Interfaz.IListenerCiclistas;
import com.example.examenciclistas.Modelo.Competition;
import com.example.examenciclistas.Modelo.Cyclist;
import com.example.examenciclistas.Modelo.Logica;
import com.example.examenciclistas.R;

import java.util.List;

public class FragmentoLista extends Fragment {

    //VAMOS A RELLENAR CICLISTAS PARA LA LISTA
    private IListenerCiclistas listener;
    private Competition competicion;

    public interface IOnAttachListener{
        Competition getCompeticion();
    }

    public FragmentoLista() {
        super(R.layout.recycler_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        Logica logica = new Logica(competicion);

        if (competicion != null && listener !=null){
            recyclerView.setAdapter(new Adaptador(getContext(), competicion, listener)) ;
        }else{
            throw new NullPointerException();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (IListenerCiclistas) context;
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        competicion = iOnAttachListener.getCompeticion();
    }



}
