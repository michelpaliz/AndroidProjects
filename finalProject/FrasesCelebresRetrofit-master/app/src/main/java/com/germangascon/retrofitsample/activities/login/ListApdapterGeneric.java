package com.germangascon.retrofitsample.activities.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.germangascon.retrofitsample.R;
import com.germangascon.retrofitsample.interfaces.IListenerList;
import com.germangascon.retrofitsample.models.Autor;
import com.germangascon.retrofitsample.models.Categoria;
import com.germangascon.retrofitsample.models.Frase;

import java.util.List;

public class ApdapterGeneric<T> extends RecyclerView.Adapter<ApdapterGeneric.HolderAdapter> {

    private final IListenerList iListenerList;
    private final List<Frase> frases;
    private final List<T> list;
    private final int option;
    private final boolean isDetalle;

    public ApdapterGeneric(List<T> list, List<Frase> frases, IListenerList iListenerList, int option, boolean isDetalle) {
        this.iListenerList = iListenerList;
        this.list = list;
        this.frases = frases;
        this.option = option;
        this.isDetalle = isDetalle;
    }

    @NonNull
    @Override
    public ApdapterGeneric.HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent,
                false);
        return new HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApdapterGeneric.HolderAdapter holder, int position) {
        System.out.println("esto es fraselist" + list);
        T tipo = list.get(position);
        if (isDetalle){
            switch (option){
                case 1:
                    holder.cargarDetalleAutores((Autor) tipo);
                    break;
                case 2:
                    holder.cargarDatosCategoria((Categoria) tipo);
                    break;
            }
        }
        switch (option){
            case 1:
                holder.cargarDatosAutores((Autor) tipo);
                break;
            case 2:
                holder.cargarDatosCategoria((Categoria) tipo);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvAutor;
        private final TextView tvCategoria;
        private final TextView tvTexto;

        public HolderAdapter(@NonNull View itemView) {
            super(itemView);
            tvAutor = itemView.findViewById(R.id.tvAutor_item);
            tvCategoria = itemView.findViewById(R.id.tvCategoria_item);
            tvTexto = itemView.findViewById(R.id.tvTexto_item);
            itemView.setOnClickListener(this);
        }

        public void cargarDatosAutores(Autor autor) {
            tvAutor.setText(autor.getNombre());
        }

        public void cargarDatosCategoria(Categoria categoria) {
            tvAutor.setText(categoria.getNombre());
        }

        // para detalle


        public void cargarDetalleAutores(Autor autor){
            tvAutor.setText(autor.getNombre());
            this.tvFrase = itemView.findViewById(R.id.tvFraseCateg);
        }

        public void cargarDetalleCategorias(){
            this.tvCategoria = itemView.findViewById(R.id.tvCategoria);
            this.tvFrase = itemView.findViewById(R.id.tvFraseCateg);
        }



        public void cargarDatosFrases(Frase frase) {
            tvAutor.setText(frase.getAutor().getNombre());
            tvCategoria.setText(frase.getCategoria().getNombre());
            tvTexto.setText(frase.getTexto());
        }

        @Override
        public void onClick(View v) {
            if (v != null){
                iListenerList.onItemSelected(getAdapterPosition());
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(itemView.getContext(), tvAutor.getText().toString(), duration);
                toast.show();
            }
        }
    }
}
