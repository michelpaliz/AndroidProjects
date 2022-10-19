package com.example.gridcountry.Adaptador;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.gridcountry.Model.Pais;
import com.example.gridcountry.R;

import java.util.ArrayList;


public class ListViewAdapter extends ArrayAdapter<Pais> {

    private final ArrayList<Pais> listaPaises;

    public ListViewAdapter(Context context, ArrayList<Pais> listaPaises) {
        super(context, R.layout.listitem_country, listaPaises);
        this.listaPaises = listaPaises;

    }

    static class Holder {
        ImageView ivFlag;
        TextView nombrePais;
        TextView capital;
        TextView poblacion;
    }

    @Override
    //**Mediante los datos que tenemos en nuestra lista el Adaptador se encargara de distribuirlos a nuestro View
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Holder holder = null;
        Pais pais = null;
        //INTRODUCIMOS NUESTRO LAYOUT (QUE YA ESTABA INTRODUCIDO EN NUESTRO CONSTRUCTOR)
        //EL LAYOUTINFLATER TIENE QUE IR ANTES
//       LayoutInflater inflater = LayoutInflater.from(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (convertView == null) {
            //INICIALIZAMOS NUESTRAS VARIABLES
            convertView = inflater.inflate(R.layout.listitem_country, null);
            holder = new Holder();
            pais = new Pais();
            holder.ivFlag = (ImageView) convertView.findViewById(R.id.ivImagenPais);
            holder.nombrePais = (TextView) convertView.findViewById(R.id.tvNombre);
            holder.capital = (TextView) convertView.findViewById(R.id.tvCapital);
            holder.poblacion = (TextView) convertView.findViewById(R.id.tvPoblacion);
            pais = getItem(position);
            convertView.setTag(holder);

        } else {
            /* Si item no es null, significa que se ha creado previamente, por tanto, podemos obtener
             * la referencia a nuestro ViewHolder mediante la propiedad Tag
             */
            holder = (Holder) convertView.getTag();
        }

        //PARA LA IMAGEN
        try {
//            String flagName = "_"+listaPaises.get(position).getCode().;
            /* Obtenemos el ID del drawable (imagen de la bandera) a partir del flagName */
            int resID = getContext().getResources().getIdentifier(pais.getImagen(), "drawable", getContext().getPackageName());
            /* Si hemos conseguido obtener el ID del drawable asociado, se lo asignamos al ImageView */
            if (resID != 0) {
                holder.ivFlag.setImageResource(resID);
            } else {
                String flagName = "_onu";
                resID = getContext().getResources().getIdentifier(flagName, "drawable", getContext().getPackageName());
                /* Si hemos conseguido obtener el ID del drawable asociado, se lo asignamos al ImageView */
                holder.ivFlag.setImageResource(resID);
            }
        } catch (Exception e) {
            /*
             * Si falla la obtención del ID del drawable no hacemos nada.
             */
        }

        /* Asignamos el nombre del país a partir de nuestro vector de países y el parámetro position */
        holder.nombrePais.setText(listaPaises.get(position).getNombre());

        /* Asignamos el nombre de la capital a partir de nuestro vector de países y el parámetro position */
        holder.capital.setText(listaPaises.get(position).getCapital());

        /* Asignamos el número de habitantes a partir de nuestro vector de países y el parámetro position */
        holder.poblacion.setText(String.valueOf(listaPaises.get(position).getPoblacion()));

        return  convertView;
    }


}
