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

import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;
import com.germangascon.navigationdrawersample.R;
import com.germangascon.navigationdrawersample.Vista.Logica.CorreoLogica;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoEmail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdaptadorEmail extends RecyclerView.Adapter<AdaptadorEmail.HolderCorreoRecibidos> implements View.OnClickListener {


    //    private final IOnCorreoSeleccionado iOnCorreoSeleccionado;
    private final Context context;
    private final FragmentoEmail.TipoFragmento tipoFragmento;
    private View.OnClickListener listener;
    private HashMap<Email, Contacto> listaGeneral;
    private HashMap<Email, Email> listaSpam;

    public AdaptadorEmail(Context context, Cuenta cuenta,  FragmentoEmail.TipoFragmento tipoFragmento) {
        this.context = context;
        this.tipoFragmento = tipoFragmento;
        CorreoLogica correoLogica = new CorreoLogica(cuenta);

        switch (tipoFragmento) {
            case RECEIVED:
                listaGeneral = correoLogica.getCorreosRecibidos();
                break;
            case SENT:
                listaGeneral = correoLogica.getCorreosEnviados();
                break;
            case UNREADED:
                listaGeneral = correoLogica.getCorreosNoLeidos();
                break;
            case SPAM:
                listaSpam = correoLogica.getCorreosSpam();
                break;
            case DELETED:
                listaGeneral = correoLogica.getCorreosEliminados();
                break;
        }

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    //TODO intentar investigar porque va aqui el listener
    @NonNull
    @Override
    public HolderCorreoRecibidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragmento_enviados, parent, false);
        view.setOnClickListener(this);
        return new HolderCorreoRecibidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCorreoRecibidos holder, int position) {
        List<Map.Entry<Email, Contacto>> test1 = null;
        List<Map.Entry<Email, Email>> test2 = null;

        switch (tipoFragmento) {
            case RECEIVED:
            case SPAM:
            case UNREADED:
            case DELETED:
                test1 = gestionGeneral();
            case SENT:
                test2 = gestionSpam();
                break;
        }

        if (test1 != null) {
            holder.cargarDatosGeneral(test1, position);
        } else {
            holder.cargarDatosSpam(test2, position);
        }


    }

    public List<Map.Entry<Email, Contacto>> gestionGeneral() {
        Stream<Map.Entry<Email, Contacto>> stream = listaGeneral.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));

        List<Map.Entry<Email, Contacto>> test = stream.collect(Collectors.toList());
        return test;
    }


    public List<Map.Entry<Email, Email>> gestionSpam() {
        Stream<Map.Entry<Email, Email>> stream = listaSpam.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));

        List<Map.Entry<Email, Email>> test = stream.collect(Collectors.toList());
        return test;
    }


    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    @Override
    public int getItemCount() {
        return listaGeneral.size();
    }


    public class HolderCorreoRecibidos extends RecyclerView.ViewHolder {

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
        }

        public void cargarDatosGeneral(List<Map.Entry<Email, Contacto>> contacto, int posicion) {
            String nombre = "c" + contacto.get(posicion).getValue().getFoto();
            System.out.println(nombre);
            int id = context.getResources().getIdentifier(nombre, "drawable", context.getPackageName());
            imageView.setImageResource(id);
            String texto = contacto.get(posicion).getKey().getTexto();
            tvDescripcion.setText(texto.substring(0, 15));
            tvNombre.setText(contacto.get(posicion).getValue().getNombre());
            tvTema.setText(contacto.get(posicion).getKey().getTema());

            if (!contacto.get(posicion).getKey().isLeido()) {
                tvFecha.setTextColor(Color.parseColor("#000000"));
            } else {
                tvFecha.setTextColor(Color.parseColor("#00BCD4"));
            }
            tvFecha.setText(contacto.get(posicion).getKey().getFecha());

        }

        public void cargarDatosSpam(List<Map.Entry<Email, Email>> email, int posicion) {
            String nombre = "d";
            System.out.println(nombre);
            int id = context.getResources().getIdentifier(nombre, "drawable", context.getPackageName());
            imageView.setImageResource(id);
            String texto = email.get(posicion).getKey().getTexto();
            tvDescripcion.setText(texto.substring(0, 15));
            tvNombre.setText(email.get(posicion).getValue().getCorreoOrigen());
            tvTema.setText(email.get(posicion).getKey().getTema());


            if (!email.get(posicion).getKey().isLeido()) {
                tvFecha.setTextColor(Color.parseColor("#000000"));
            } else {
                tvFecha.setTextColor(Color.parseColor("#00BCD4"));
            }
            tvFecha.setText(email.get(posicion).getKey().getFecha());
        }


    }


}
