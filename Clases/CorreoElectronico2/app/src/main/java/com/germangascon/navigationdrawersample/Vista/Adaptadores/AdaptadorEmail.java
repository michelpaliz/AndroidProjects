package com.germangascon.navigationdrawersample.Vista.Adaptadores;

import android.annotation.SuppressLint;
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
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoListado;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdaptadorEmail extends RecyclerView.Adapter<AdaptadorEmail.HolderCorreoRecibidos> {

    private final Context context;
    private final IOnCorreoSeleccionado onCorreoSeleccionado;
    private final CorreoLogica correoLogica;
    private FragmentoListado.TipoFragmento tipoFragmento;
    private HashMap<Email, Contacto> listaGeneral;
    private HashMap<Email, Email> listaSpam;


    /**
     * @param context              el contexto que le pasamos de nuestra actividad
     * @param cuenta               nuestro objt donde se encuentra 1 matriz de contactos y otra de correos
     * @param tipoFragmento        el tipo de fragmento
     * @param onCorreoSeleccionado  TODO investigar para que sirve este listener
     */
    public AdaptadorEmail(Context context, Cuenta cuenta, FragmentoListado.TipoFragmento tipoFragmento, IOnCorreoSeleccionado onCorreoSeleccionado) {
        this.context = context;
        this.tipoFragmento = tipoFragmento;
        this.correoLogica = new CorreoLogica(cuenta);
        this.onCorreoSeleccionado = onCorreoSeleccionado;
        cargarDatos();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void actualizarLista(FragmentoListado.TipoFragmento tipoFragmento) {
        this.tipoFragmento = tipoFragmento;
        cargarDatos();
        notifyDataSetChanged();
    }


    private void cargarDatos() {
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
            case BIN:
                listaGeneral = correoLogica.getCorreosEliminados();
            case SPAM:
                listaSpam = correoLogica.getCorreosSpam();
                break;

        }

    }


    /**
     * Esta funcion cargamos nuestra layout del adaptador
     *
     * @param parent   es la clase padre nuestros view
     * @param viewType TODO investigar esto
     * @return la vista cargada con el listener
     */
    @NonNull
    @Override
    public HolderCorreoRecibidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragmento_enviados, parent, false);
        return new HolderCorreoRecibidos(view);
    }

    /**
     * El onBindViewHolder creamos una instancia de nuestro Holder personalizado donde cargaremos los datos
     *
     * @param holder   instancia de nuestro holder personalizado
     * @param position la position de nuestro elemento que recogeremos de cada item.
     */
    @Override
    public void onBindViewHolder(@NonNull HolderCorreoRecibidos holder, int position) {
        List<Map.Entry<Email, Contacto>> test1 = null;
        List<Map.Entry<Email, Email>> test2 = null;
        cargarDatos();
        switch (tipoFragmento) {
            case RECEIVED:
            case SENT:
            case UNREADED:
            case BIN:
                test1 = gestionGeneral();
                break;
            case SPAM:
                test2 = gestionSpam();
                break;
        }

        if (test1 != null) {
            holder.cargarDatosGeneral(test1, position);
        }
        if (test2 != null) {
            holder.cargarDatosSpam(test2, position);
        }


    }

    /**
     * Creamos una funcion para ordenar nuestra listas con los contactos y los emails
     *
     * @return la lista ordenada de forma descendente
     */
    public List<Map.Entry<Email, Contacto>> gestionGeneral() {
        Stream<Map.Entry<Email, Contacto>> stream = listaGeneral.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));

        List<Map.Entry<Email, Contacto>> test1 = stream.collect(Collectors.toList());
        return test1;
    }

    /**
     * Creanis una funcion para la lista de los correos de spam
     *
     * @return la lista de spam ordenada
     */
    public List<Map.Entry<Email, Email>> gestionSpam() {
        Stream<Map.Entry<Email, Email>> stream = listaSpam.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));

        List<Map.Entry<Email, Email>> test2 = stream.collect(Collectors.toList());
        return test2;
    }


    @Override
    public int getItemCount() {
        int size = 0;
        if (listaGeneral != null) {
            size = listaGeneral.size();
        }
        if (listaSpam != null)
            size = listaSpam.size();

        return size;

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

        /**
         * Funcion para cargar los datos generales (CORREOS ENVIADOS, CORREOS ELIMINADOS, CORREOS RECIBIDOS)
         *
         * @param contacto
         * @param posicion
         */
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

        /**
         * Funcion para los correos recibidos pero que no son reconocidos en nuestros contactos
         *
         * @param email
         * @param posicion
         */
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
