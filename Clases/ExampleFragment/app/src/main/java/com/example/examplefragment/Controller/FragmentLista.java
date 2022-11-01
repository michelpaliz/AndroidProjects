package com.example.examplefragment.Controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examplefragment.Interface.IContactoListener;
import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;

import java.util.List;

public class FragmentLista extends Fragment {

    private List<Contacto> contactos;
    private IContactoListener listener;


    public FragmentLista(){
        super(R.layout.fragment_recyclerview);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState){
        super.onViewCreated(view, savedInstaceState);
        assert contactos != null && listener !=null;
        RecyclerView recyclerView = view.findViewById(R.id.rvLista);
        Log.d("contactos", String.valueOf(contactos.size()));
        recyclerView.setAdapter(new ContactoAdapter(contactos, listener));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }

    public void setContextListener(List<Contacto> contactos, IContactoListener listener){
        this.contactos = contactos;
        this.listener = listener;
    }




}
