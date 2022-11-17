package com.example.examenciclistas.Vista;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.examenciclistas.Modelo.Cyclist;
import com.example.examenciclistas.Modelo.Logica;
import com.example.examenciclistas.R;

public class FragmentoDetalle extends Fragment {

    //VAMOS A RELLENAR CICLISTAS PARA LA LISTA
    private Cyclist ciclista;

    private ImageView ivFoto;
    private TextView tvNombre;
    private TextView tvApellido;
    private TextView tvGrupo;

    public interface IOnAttachListener{
        Cyclist getCiclista();
    }

    public FragmentoDetalle() {
        super(R.layout.fragmento_detalle_item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivFoto = view.findViewById(R.id.ivFotoFrgDetalle);
        tvNombre = view.findViewById(R.id.tvNombreFrgDetalle);
        tvApellido = view.findViewById(R.id.tvApellidoFrgDetalle);
        tvGrupo = view.findViewById(R.id.tvGrupoFrgDetalle);
        if (ciclista != null) {
            cargarDatos(ciclista);
        }

    }

    public void cargarDatos(Cyclist ciclista ){
        String nombre = "cyclist_"+ciclista.getCyclistId();
        int id = getContext().getResources().getIdentifier(nombre,"drawable",getContext().getPackageName());
        ivFoto.setImageResource(id);
        tvNombre.setText(ciclista.getName());
        tvApellido.setText(ciclista.getSurname());
        tvGrupo.setText(ciclista.getTeam());
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        ciclista = iOnAttachListener.getCiclista();
    }




}
