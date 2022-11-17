package com.example.tiendacoches.Vista.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendacoches.Interfaz.IListenerProducto;
import com.example.tiendacoches.Model.Leche;
import com.example.tiendacoches.R;

import java.util.List;

public class AdapatadorProducto extends RecyclerView.Adapter<AdapatadorProducto.HolderProducto>{

    //NECESITAMOS EL PUNTERO PARA INTRODUCIR LOS DATOS;
    private final List<Leche>listaLeches;
    private final IListenerProducto iListenerProducto;

    public AdapatadorProducto(List<Leche> listaLeches, IListenerProducto iListenerProducto) {
        this.listaLeches = listaLeches;
        this.iListenerProducto = iListenerProducto;
    }

    //IMPLEMENTAMOS LOS 3 METODOS DE RECYCLERVIEW

    @NonNull
    @Override
    public HolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_producto,parent,false);
        return new HolderProducto(itemView);
    }

    /**
     *
     * @param holder nos proporciona el mismo holder que hemos creado para llamar a nuestras vistas
     * @param position la posicion del elemento de nuestra lista
     */
    @Override
    public void onBindViewHolder(@NonNull HolderProducto holder, int position) {

        Leche leche = listaLeches.get(position);

        if (leche==null){
           throw new NullPointerException();
        }

        holder.cargarDatos(listaLeches.get(position));
    }

    @Override
    public int getItemCount() {
        return listaLeches.size();
    }


    public class HolderProducto extends RecyclerView.ViewHolder implements View.OnClickListener{

        //METEMOS NUESTRAS VISTAS AQUI PARA RELLENARLO
        private TextView tvMarca;
        private TextView tvStock;
        private TextView tvPrecioVenta;
//        private TextView tvPrecioCompra;
//        private TextView tvFechaCompra;
//        private TextView tvFechaCaducacion;
//        private TextView tvCaducacion;


        /**
         * INICIALIZAMOS NUESTRAS VISTAS EN EL METODO
         * @param itemView vista ya cargada para hacerlo
         */
        public HolderProducto(@NonNull View itemView) {
            super(itemView);
            tvMarca = itemView.findViewById(R.id.tvNombre);
            tvStock =  itemView.findViewById(R.id.tvStock);
//            tvPrecioCompra = itemView.findViewById(R.id.tvPrecioCompra);
//            tvFechaCompra = itemView.findViewById(R.id.tvFechaCompra);
            tvPrecioVenta = itemView.findViewById(R.id.tvPrecioVenta);
//            tvFechaCaducacion = itemView.findViewById(R.id.tvFechaCaducacion);
//            tvCaducacion = itemView.findViewById(R.id.tvCaducacion);
            //Llamamos a nuestro listener cuando creemos nuestro holder
            itemView.setOnClickListener(this);
        }


        /**
         *
         * @param leche el elemento que obtenemos en nuestra lista
         */
        public void cargarDatos(Leche leche){
            Log.d("mess", "cargarDatos: " + leche.toString());
            tvMarca.setText("Nombre del Producto " + leche.getMarca());
            tvStock.setText("Numero de Stock " + String.valueOf(leche.getNumeroStock()));
//            tvPrecioCompra.setText(String.valueOf(leche.getPrecioUnidad()));
            tvPrecioVenta.setText("Precio de venta "  + String.valueOf(leche.redondear(leche.getPrecioVenta()) + "$"));
//            tvFechaCaducacion.setText(leche.getFechaCaducidad());
//            tvCaducacion.setText(leche.getFechaCaducidad());
        }


        /**
         *
         * @param v vista para ejecutar y cargar el indice del mismo en nuestra interfaz
         */
        @Override
        public void onClick(View v) {
            if (v != null){
                iListenerProducto.onProductoSeleccionado(getBindingAdapterPosition());
            }
        }
    }










}
