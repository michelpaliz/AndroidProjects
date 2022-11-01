package com.example.recyclerviewpais.Adaptador;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewpais.Interfaz.IPaisListener;
import com.example.recyclerviewpais.Model.Pais;
import com.example.recyclerviewpais.R;

import java.util.ArrayList;


public class AdaptadorPais extends RecyclerView.Adapter<AdaptadorPais.PaisViewHolder> {

    private final ArrayList<Pais> listaPaises;
    private final IPaisListener listener;

    public AdaptadorPais( ArrayList<Pais> listaPaises, IPaisListener listener) {
        this.listaPaises = listaPaises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_country,parent,false);
        return  new PaisViewHolder(itemView,listener,itemView.getContext());

    }

    @Override
    public void onBindViewHolder(@NonNull PaisViewHolder holder, int position) {
        Pais pais = listaPaises.get(position);
        holder.bindPais(pais);
    }

    @Override
    public int getItemCount() {
        return listaPaises.size();
    }

    static public class PaisViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView ivImagen;
        private final TextView tvNombre;
        private final TextView tvCapital;
        private final TextView tvPopulacion;
        private final Context myContext;
        private final IPaisListener listener;

        public PaisViewHolder(@NonNull View view, IPaisListener listener, Context context) {
            super(view);
            this.ivImagen = view.findViewById(R.id.imagenPais);
            this.tvNombre = view.findViewById(R.id.tvNombre);
            this.tvCapital = view.findViewById(R.id.tvCapital);
            this.tvPopulacion = view.findViewById(R.id.tvPoblacion);
            this.myContext = context;
            this.listener = listener;
            view.setOnClickListener(this);

        }



        public void bindPais(Pais pais){
//            String flagName = "_" + pais.getImagen().toLowerCase();
            int resID = myContext.getResources().getIdentifier(pais.getImagen(),"drawable",myContext.getPackageName());
            if(resID != 0){
                ivImagen.setImageResource(resID);
            }else{
                ivImagen.setImageResource(R.drawable._onu);
            }

            tvNombre.setText(pais.getNombre());
            tvPopulacion.setText( String.valueOf(pais.getPoblacion()));
            tvCapital.setText(pais.getCapital());
        }


        @Override
        public void onClick(View view) {
                if (listener != null){
                    listener.onClick(getLayoutPosition());
                }
        }
    }


}
