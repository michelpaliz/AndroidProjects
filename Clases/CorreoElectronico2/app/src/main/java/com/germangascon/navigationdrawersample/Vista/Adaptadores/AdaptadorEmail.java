package com.germangascon.navigationdrawersample.Vista.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
    private final CorreoLogica correoLogica;
    private FragmentoListado.TipoFragmento tipoFragmento;
    private HashMap<Email, Contacto> listaGeneral;
    private HashMap<Email, Email> listaSpam;
    private final IOnCorreoSeleccionado iOnCorreoSeleccionado;
    private Cuenta cuenta;
    public static int email;


    /**
     * @param context       el contexto que le pasamos de nuestra actividad
     * @param cuenta        nuestro objt donde se encuentra 1 matriz de contactos y otra de correos
     * @param tipoFragmento el tipo de fragmento
     */
    public AdaptadorEmail(Context context, Cuenta cuenta, FragmentoListado.TipoFragmento tipoFragmento, IOnCorreoSeleccionado iOnCorreoSeleccionado) {
        this.context = context;
        this.tipoFragmento = tipoFragmento;
        this.cuenta = cuenta;
        this.correoLogica = new CorreoLogica(cuenta);
        this.iOnCorreoSeleccionado = iOnCorreoSeleccionado;
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

        return stream.collect(Collectors.toList());
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

        return stream.collect(Collectors.toList());
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


    public class HolderCorreoRecibidos extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final TextView tvNombre;
        private final TextView tvTema;
        private final TextView tvDescripcion;
        private final TextView tvFecha;
        List<Map.Entry<Email, Email>> listaEmail;

        public HolderCorreoRecibidos(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imagenEnviado);
            this.tvNombre = itemView.findViewById(R.id.tvNombreEnviado);
            this.tvTema = itemView.findViewById(R.id.tvAsuntoEnviado);
            this.tvDescripcion = itemView.findViewById(R.id.tvTextoEnviado);
            this.tvFecha = itemView.findViewById(R.id.tvFechaEnviado);
            itemView.setOnClickListener(this);
        }

        /**
         * Funcion para cargar los datos generales (CORREOS ENVIADOS, CORREOS ELIMINADOS, CORREOS RECIBIDOS)
         *
         * @param listaEmail
         * @param posicion
         */
        public void cargarDatosGeneral(List<Map.Entry<Email, Contacto>> listaEmail, int posicion) {
            String nombre = "c" + listaEmail.get(posicion).getValue().getFoto();
            System.out.println(nombre);
            int id = context.getResources().getIdentifier(nombre, "drawable", context.getPackageName());
            imageView.setImageResource(id);
            String texto = listaEmail.get(posicion).getKey().getTexto();
            tvDescripcion.setText(texto.substring(0, 15));
            tvNombre.setText(listaEmail.get(posicion).getValue().getNombre());
            tvTema.setText(listaEmail.get(posicion).getKey().getTema());

            if (!listaEmail.get(posicion).getKey().isLeido()) {
                tvFecha.setTextColor(Color.parseColor("#000000"));
            } else {
                tvFecha.setTextColor(Color.parseColor("#00BCD4"));
            }
            tvFecha.setText(listaEmail.get(posicion).getKey().getFecha());

        }

        /**
         * Funcion para los correos recibidos pero que no son reconocidos en nuestros contactos
         *
         * @param listaEmail
         * @param posicion
         */
        public void cargarDatosSpam(List<Map.Entry<Email, Email>> listaEmail, int posicion) {
            this.listaEmail = listaEmail;
            String nombre = "d";
            System.out.println(nombre);
            @SuppressLint("DiscouragedApi") int id = context.getResources().getIdentifier(nombre, "drawable", context.getPackageName());
            imageView.setImageResource(id);
            String texto = listaEmail.get(posicion).getKey().getTexto();
            tvDescripcion.setText(texto.substring(0, 15));
            tvNombre.setText(listaEmail.get(posicion).getValue().getCorreoOrigen());
            tvTema.setText(listaEmail.get(posicion).getKey().getTema());
            if (!listaEmail.get(posicion).getKey().isLeido()) {
                tvFecha.setTextColor(Color.parseColor("#000000"));
            } else {
                tvFecha.setTextColor(Color.parseColor("#00BCD4"));
            }
            tvFecha.setText(listaEmail.get(posicion).getKey().getFecha());
        }


        @Override
        public void onClick(View v) {
            if (cuenta == null) {
                throw new NullPointerException();
            }
            email = getAdapterPosition();
            iOnCorreoSeleccionado.onCorreoSeleccionado(getAdapterPosition());
        }
    }


}
