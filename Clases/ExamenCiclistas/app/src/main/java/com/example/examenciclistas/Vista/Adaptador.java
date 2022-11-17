package com.example.examenciclistas.Vista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenciclistas.Interfaz.IListenerCiclistas;
import com.example.examenciclistas.Modelo.Competition;
import com.example.examenciclistas.Modelo.Cyclist;
import com.example.examenciclistas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.HolderAdaptador> {

    private final Context context;
    private Cyclist ciclista;
    private final Competition competition;
    private final IListenerCiclistas iListenerCiclistas;

    public Adaptador(Context context, Competition competition, IListenerCiclistas iListenerCiclistas) {
        this.context = context;
        this.competition = competition;
        this.iListenerCiclistas = iListenerCiclistas;
    }

    @NonNull
    @Override
    public HolderAdaptador onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new HolderAdaptador(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAdaptador holder, int position) {
//        ciclista = ciclistas.get(position);
        List<Cyclist> cyclists;
        cyclists = Arrays.asList(competition.getCyclists());
        Cyclist  cyclist =  cyclists.get(position);
        holder.cargarDatos(cyclist);

    }

    @Override
    public int getItemCount() {
        return competition.getCyclists().length;
    }

    public class HolderAdaptador extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvId;
        private ImageView ivFoto;
        private TextView tvNombre;
        private TextView tvApellido;
        private TextView tvGrupo;
        private TextView tvTiempo;
        private StringBuilder sb;


        public HolderAdaptador(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
//            tvApellido = itemView.findViewById(R.id.tvApellido);
            tvGrupo = itemView.findViewById(R.id.tvGrupo);
            tvTiempo = itemView.findViewById(R.id.tvTiempo);
            itemView.setOnClickListener(this);
            sb = new StringBuilder();
        }

        public void cargarDatos(Cyclist ciclista){
            String nombre = "cyclist_"+ciclista.getCyclistId();
            int id = context.getResources().getIdentifier(nombre,"drawable",context.getPackageName());

            tvId.setText(String.valueOf(getBindingAdapterPosition()+1));
            ivFoto.setImageResource(id);
            sb.setLength(0);
            sb.append(ciclista.getName()).append(" ").append(ciclista.getSurname());
            tvNombre.setText(ciclista.getName());
//            tvApellido.setText(ciclista.getSurname());
            tvGrupo.setText(ciclista.getTeam());
            tvTiempo.setText(ciclista.getTiempo());
        }


        @Override
        public void onClick(View view) {
            if (view != null){
                iListenerCiclistas.onCiclistaSeleccionado(getBindingAdapterPosition());
            }
        }
    }
}
