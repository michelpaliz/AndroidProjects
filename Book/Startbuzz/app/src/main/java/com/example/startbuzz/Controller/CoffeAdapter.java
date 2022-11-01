package com.example.startbuzz.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startbuzz.Interface.IElement;
import com.example.startbuzz.Model.Coffe;
import com.example.startbuzz.R;

import java.util.List;


public class CoffeAdapter extends RecyclerView.Adapter<CoffeAdapter.HolderCoffe> {

    private List<Coffe> coffees;
    private IElement iElement;

    public CoffeAdapter( List<Coffe> coffees, IElement iElement) {
        this.coffees = coffees;
        this.iElement = iElement;
    }

    @NonNull
    @Override
    public HolderCoffe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_item, parent, false);
        return new HolderCoffe(view,iElement);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCoffe holder, int position) {
        Coffe coffe = coffees.get(position);
        holder.bindCoffe(coffe);
    }

    @Override
    public int getItemCount() {
        return coffees.size();
    }

    public static class HolderCoffe extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvNombre;
        private final TextView tvDescripcion;
        private final TextView tvPrecio;
        private final ImageView imagen;
        private final Context contexto;
        private IElement iElemento;

        public HolderCoffe(@NonNull View itemView, IElement iElement) {
            super(itemView);
            imagen = itemView.findViewById(R.id.ivImagen);
            tvNombre = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            this.iElemento = iElement;
            this.contexto = itemView.getContext();
        }

        public void bindCoffe(Coffe coffe){
            String nombreImg = String.valueOf(coffe.getImg());
            int resId = contexto.getResources().getIdentifier(nombreImg,"drawable", contexto.getPackageName());
            if (resId != 0){
                imagen.setImageResource(resId);
            }else{
                nombreImg = "milk_coffee";
                resId = contexto.getResources().getIdentifier(nombreImg,"drawable", contexto.getPackageName());
                imagen.setImageResource(resId);
            }
            tvNombre.setText(coffe.getNombre());
            tvPrecio.setText(String.valueOf(coffe.getPrecio()+"$"));
            tvDescripcion.setText(coffe.getDescripcion());

        }


        @Override
        public void onClick(View view) {
            if (iElemento != null){
                iElemento.onSelectedElement(getAdapterPosition());
            }
        }
    }
}
