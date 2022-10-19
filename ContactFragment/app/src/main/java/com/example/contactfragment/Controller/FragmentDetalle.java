package com.example.contactfragment.Controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactfragment.Model.Contacto;
import com.example.contactfragment.R;

public class FragmentDetalle extends Fragment {

    private TextView tvNombre;
    private TextView tvApellidos;
    private TextView tvNacimiento;
    private TextView tvDireccion;
    private TextView tvEmpresa;
    private TextView tvTelefono1;
    private TextView tvTelefono2;
    //String
    private String nombre;
    private String apellido;
    private String nacimiento;
    private String direccion;
    private String empresa;
    private String telefono1;
    private String telefono2;


    public FragmentDetalle(){
        super(R.layout.list_item_detalle_recyclerview);
    }

    public Contacto mos(Contacto contacto) {
        return contacto;
    }


    @Override
    public void OnCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            contactos = (Contacto)
        }


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rvLista);
//        recyclerView.setAdapter(new ContactoAdapter(this, ));


    }


}
