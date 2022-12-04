package com.germangascon.navigationdrawersample.Vista.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.germangascon.navigationdrawersample.Interfaz.IOnCorreoSeleccionado;
import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;
import com.germangascon.navigationdrawersample.R;
import com.germangascon.navigationdrawersample.Vista.Logica.CorreoLogica;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdaptadorEnviados extends RecyclerView.Adapter<AdaptadorEnviados.HolderCorreoRecibidos> {

    private final Context context;
    private final IOnCorreoSeleccionado iOnCorreoSeleccionado;
    private final HashMap<Email, Contacto> hashMap;

    public AdaptadorEnviados(Context context, Cuenta cuenta, IOnCorreoSeleccionado iOnCorreoSeleccionado) {
        this.context = context;
        this.iOnCorreoSeleccionado = iOnCorreoSeleccionado;
        CorreoLogica correoLogica = new CorreoLogica(cuenta);
        hashMap = correoLogica.getCorreosEnviados();
    }

    @NonNull
    @Override
    public HolderCorreoRecibidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragmento_enviados,parent,false);
        return new HolderCorreoRecibidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCorreoRecibidos holder, int position) {
        Stream<Map.Entry<Email, Contacto>> stream = hashMap.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));

        List<Map.Entry<Email, Contacto>> test = stream.collect(Collectors.toList());
        holder.cargarDatos(test, position);

    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    public class HolderCorreoRecibidos extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView imageView;
        private final TextView tvNombre;
        private final TextView tvTema;
        private final TextView tvDescripcion;
        private final TextView tvFecha;


        public HolderCorreoRecibidos(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imagenEnviado);
            this.tvNombre = itemView.findViewById(R.id.tvNombreEnviado);
            this.tvTema = itemView.findViewById(R.id.tvAsuntoEnviado);
            this.tvDescripcion = itemView.findViewById(R.id.tvTextoEnviado);
            this.tvFecha = itemView.findViewById(R.id.tvFechaEnviado);
            itemView.setOnClickListener(this);
        }

        public void cargarDatos(List<Map.Entry<Email,Contacto>> contacto, int posicion){
            String nombre = "c"+contacto.get(posicion).getValue().getFoto();
            System.out.println( nombre);
            int id = context.getResources().getIdentifier(nombre,"drawable",context.getPackageName());
            imageView.setImageResource(id);
//            tvDescripcion.setText(contacto.get(posicion).getKey().getTexto());
            String texto =contacto.get(posicion).getKey().getTexto();
            tvDescripcion.setText(texto.substring(0,15));
            tvNombre.setText(contacto.get(posicion).getValue().getNombre());
            tvTema.setText(contacto.get(posicion).getKey().getTema());

            tvFecha.setText(contacto.get(posicion).getKey().getFecha());
        }


        @Override
        public void onClick(View v) {
            iOnCorreoSeleccionado.onCorreoSeleccionado(getAdapterPosition());
        }
    }





}
