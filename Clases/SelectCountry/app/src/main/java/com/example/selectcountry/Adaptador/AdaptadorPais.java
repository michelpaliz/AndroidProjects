package com.example.selectcountry.Adaptador;


import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.selectcountry.Model.Pais;
import com.example.selectcountry.R;

import java.io.File;import java.util.ArrayList;


public class AdaptadorPais extends ArrayAdapter<Pais> {

    private final ArrayList<Pais> listaPaises;

    public <Pais> AdaptadorPais( Context context, ArrayList<Pais> listaPaises) {
        super(context, 0,listaPaises);
        this.listaPaises = listaPaises;
    }

    public static class holder {
        static ImageView ivFlag;
        static TextView nombrePais;
        static TextView capital;
        static TextView poblacion;

    }

    //**Mediante los datos que tenemos en nuestra lista el Adaptador se encargara de distribuirlos a nuestro View

    public View getView(int position, View convertView, ViewGroup parent){
        //INTRODUCIMOS NUESTRO LAYOUT (QUE YA ESTABA INTRODUCIDO EN NUESTRO CONSTRUCTOR)
        //EL LAYOUTINFLATER TIENE QUE IR ANTES

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView= inflater.inflate(R.layout.listitem_country,null);

        //INICIALIZAMOS NUESTRAS VARIABLES
        holder.ivFlag = (ImageView)  convertView.findViewById(R.id.imagenPais);
        holder.nombrePais= (TextView) convertView.findViewById(R.id.tvNombre);
        holder.capital = (TextView) convertView.findViewById(R.id.tvCapital);
        holder.poblacion = (TextView) convertView.findViewById(R.id.tvPoblacion);
        Pais pais = getItem(position);
        //HACEMOS LOS SETTERS
//        if (convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_country,parent,false);
//        }
//        String flagName = "_"+listaPaises.get(position).getImagen();
        int id = getContext().getResources().getIdentifier(pais.getImagen(), "drawable", getContext().getPackageName());
        if (id != 0){
            holder.ivFlag.setImageResource(id);
        }else{
            String flagName = "_onu";
            id = getContext().getResources().getIdentifier(flagName,"drawable",getContext().getPackageName());
            holder.ivFlag.setImageResource(id);
        }

        holder.nombrePais.setText(listaPaises.get(position).getNombre());
        holder.capital.setText(listaPaises.get(position).getCapital());;
        holder.poblacion.setText(listaPaises.get(position).getPoblacion());


        return convertView;
    }

}
