package com.example.examplefragment.Controller;

import android.content.Context;
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

    //Esta implementacion los coje desde el main que este nuestros contactos;
    public interface IOnAttachListener{
        List<Contacto> getContactosListado();
    }

    public FragmentLista(){
        super(R.layout.fragment_recyclerview);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState){
        super.onViewCreated(view, savedInstaceState);
        RecyclerView recyclerView = view.findViewById(R.id.rvLista);
        Log.d("LISTA_CONTACTOS", contactos.size()+"");
        recyclerView.setAdapter(new ContactoAdapter(contactos, listener));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Le asiganmos el contexto a nuestro contacto
        listener = (IContactoListener) context;
        IOnAttachListener attachListener = (IOnAttachListener) context;
        contactos = attachListener.getContactosListado();
    }






}
