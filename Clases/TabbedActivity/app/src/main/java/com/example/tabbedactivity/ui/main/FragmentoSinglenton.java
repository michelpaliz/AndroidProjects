package com.example.tabbedactivity.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tabbedactivity.R;

public class FragmentoSinglenton extends Fragment {

    //TODO necesitamos un obj que concecte a nuestra base de datos;
    //Crear unobj que cree la conexion para las proximas veces no recrear
    //Como hacemos que el obj solo tenga una instancia.
    //Que solo se puede crear un objeto
    //Necesito un metodo que me devuelva una instancia y si ya existe que me devuelve esa misma o que si es la primera vez me crea una nueva instancia.

    private static FragmentoSinglenton fragmentoSinglenton = null;

    public interface IOnAttachListener{
        String getDatosProfesionales();
    }

    //Desactivamos al constructor

    public FragmentoSinglenton() {
        super(R.layout.fragmento_singleton);
        fragmentoSinglenton = new FragmentoSinglenton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static FragmentoSinglenton getInstance(){
        if (fragmentoSinglenton != null){
//            return this; //no puedo meter el this porque no podria llamarlo.
            return fragmentoSinglenton;
        }
        return new FragmentoSinglenton();


    }

    /**
     *
     * @param context es la actividad donde este el fragmento.
     * el requisto es el que la actividad que implemente el framento tine que imp[lementar el Onattachlistener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Carga el contexto
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        datos = iOnAttachListener.getDatosProfesionales();
    }
}
