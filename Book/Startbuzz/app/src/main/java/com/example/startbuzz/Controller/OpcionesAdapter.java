package com.example.startbuzz.Controller;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startbuzz.Interface.IElement;
import com.example.startbuzz.Model.Opcion;
import com.example.startbuzz.R;
import com.example.startbuzz.SecondActivity;

import java.util.List;

public class OpcionesAdapter extends RecyclerView.Adapter<OpcionesAdapter.HolderOpciones> {

    private List<Opcion> opciones;
    private IElement iElement;
    private Context context;

    public OpcionesAdapter(Context context,List <Opcion> opciones, IElement iElement) {
        this.opciones = opciones;
        this.iElement = iElement;
        this.context  = context ;
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }

    @NonNull
    @Override
    public HolderOpciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_item,parent,false);
        return new HolderOpciones(context,view,iElement);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOpciones holder, int position) {
        Opcion opcion = opciones.get(position);
        holder.bind(opcion);

    }

    @Override
    public int getItemCount() {
        return opciones.size();
    }

    public static class HolderOpciones extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView titulo;
        private final TextView descripcion;
        private final IElement iElement;
        private final  Context context;
        public HolderOpciones(Context context, @NonNull View view, IElement iElement){
            super(view);
            titulo = view.findViewById(R.id.tvTituloFirstItem);
            descripcion = view.findViewById(R.id.tvDescripcionFirstItem);
            this.iElement = iElement;
            this.context = context   ;
            view.setOnClickListener(this);
        }



        public void bind(Opcion opcion){
            titulo.setText(opcion.getTitulo());
            descripcion.setText(opcion.getDescripcion());
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (opcion.getTitulo().equalsIgnoreCase("Drinks")) {
                        Intent intent = new Intent(context,SecondActivity.class);
                        intent.putExtra("type", opcion.getTitulo());
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"Esta vacia esta seccion",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        @Override
        public void onClick(View view) {
            if (view != null){
                iElement.onSelectedElement(getAdapterPosition());

            }
        }
    }
}
