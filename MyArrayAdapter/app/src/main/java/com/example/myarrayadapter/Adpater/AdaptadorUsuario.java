package com.example.myarrayadapter.Adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myarrayadapter.Model.Usuario;
import com.example.myarrayadapter.R;

import java.util.ArrayList;

public class AdaptadorUsuario extends ArrayAdapter <Usuario>{


    public AdaptadorUsuario(@NonNull Context context, ArrayList<Usuario> listaUsuarios) {
        super(context, 0,listaUsuarios);
    }

    // este metodo describe la traduccion de los datos de mi obj
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //obten la informacion  del item en este posicion
        Usuario usuario = getItem(position);
        //Confirma si esa vista esta siendo reutilizada, de otra manera anyade la vista
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user,parent,false);
        }
        //Investiga la vista para los datos
        TextView tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);
        TextView tvDireccion = (TextView) convertView.findViewById(R.id.tvDireccion);
        //Ocupa la informacion de datos en la nueva vista usando nuestro objeto
        tvNombre.setText(usuario.getNombre());
        tvDireccion.setText(usuario.getDireccion());
        //Devuelveme la vista
        return convertView;
    }

}
