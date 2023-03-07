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

public class ListApdapterGeneric<T> extends RecyclerView.Adapter<ListApdapterGeneric.HolderAdapter> {

    private final IListenerList iListenerList;
    private final List<Frase> frases;
    private final List<T> list;
    private final int option;
    private final boolean isDetalle;

    public ListApdapterGeneric(List<T> list, List<Frase> frases, IListenerList iListenerList, int option, boolean isDetalle) {
        this.iListenerList = iListenerList;
        this.list = list;
        this.frases = frases;
        this.option = option;
        this.isDetalle = isDetalle;
    }

    @NonNull
    @Override
    public ListApdapterGeneric.HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent,
                false);
        return new HolderAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListApdapterGeneric.HolderAdapter holder, int position) {

        switch (option) {
            case 0:
                if (isDetalle) {
                    Frase frase = frases.get(position);
                    holder.cargarDetalles(frase);
                }
                break;
            case 1:
                Object tipo = list.get(position);
                holder.cargarDatosAutores((Autor) tipo);
                break;
            case 2:
                tipo = list.get(position);
                holder.cargarDatosCategoria((Categoria) tipo);
                break;

            case 3:
                Frase frase = frases.get(position);
                holder.cargarDetalles(frase);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (isDetalle) {
            return frases.size();
        }
        return list.size();
    }

    public class HolderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvTexto;

        public HolderAdapter(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.tvTipo_item);
            if (!isDetalle) {
                itemView.setOnClickListener(this);
            }
        }

        public void cargarDatosAutores(Autor autor) {
            tvTexto.setText(autor.getNombre());
        }

        public void cargarDatosCategoria(Categoria categoria) {
            tvTexto.setText(categoria.getNombre());
        }

        public void cargarDetalles(Frase frase) {
            tvTexto.setText(frase.getTexto());
        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                iListenerList.onItemSelected(getAdapterPosition());
                notifyDataSetChanged();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(itemView.getContext(), tvTexto.getText().toString(), duration);
                toast.show();
            }
        }
    }
}
