package com.germangascon.navigationdrawersample.Vista.Adaptadores;

import android.content.Context;
import android.graphics.Color;
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

public class AdaptadorSpam extends RecyclerView.Adapter<AdaptadorSpam.HolderCorreoRecibidos> {

    private final Context context;
    private final IOnCorreoSeleccionado iOnCorreoSeleccionado;
    private final HashMap<Email, Email> hashMap;

    public AdaptadorSpam(Context context, Cuenta cuenta, IOnCorreoSeleccionado iOnCorreoSeleccionado) {
        this.context = context;
        this.iOnCorreoSeleccionado = iOnCorreoSeleccionado;
        CorreoLogica correoLogica = new CorreoLogica(cuenta);
        hashMap = correoLogica.getCorreosSpam();
    }

    @NonNull
    @Override
    public HolderCorreoRecibidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragmento_spam,parent,false);
        return new HolderCorreoRecibidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCorreoRecibidos holder, int position) {
        Stream<Map.Entry<Email, Email>> stream = hashMap.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));

        List<Map.Entry<Email, Email>> test = stream.collect(Collectors.toList());
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
            this.imageView = itemView.findViewById(R.id.imageSpam);
            this.tvNombre = itemView.findViewById(R.id.tvNombreSpam);
            this.tvTema = itemView.findViewById(R.id.tvAsuntoSpam);
            this.tvDescripcion = itemView.findViewById(R.id.tvTextoSpam);
            this.tvFecha = itemView.findViewById(R.id.tvFechaSpam);
            itemView.setOnClickListener(this);
        }

        public void cargarDatos(List<Map.Entry<Email,Email>> email, int posicion){
            String nombre = "d";
            System.out.println( nombre);
            int id = context.getResources().getIdentifier(nombre,"drawable",context.getPackageName());
            imageView.setImageResource(id);
//            tvDescripcion.setText(contacto.get(posicion).getKey().getTexto());
            String texto =email.get(posicion).getKey().getTexto();
            tvDescripcion.setText(texto.substring(0,15));
            tvNombre.setText(email.get(posicion).getValue().getCorreoOrigen());
            tvTema.setText(email.get(posicion).getKey().getTema());


            if (!email.get(posicion).getKey().isLeido()){
                tvFecha.setTextColor(Color.parseColor("#000000"));
            }else{
                tvFecha.setTextColor(Color.parseColor("#00BCD4"));
            }
            tvFecha.setText(email.get(posicion).getKey().getFecha());

        }


        @Override
        public void onClick(View v) {
            iOnCorreoSeleccionado.onCorreoSeleccionado(getAdapterPosition());
        }
    }





}
