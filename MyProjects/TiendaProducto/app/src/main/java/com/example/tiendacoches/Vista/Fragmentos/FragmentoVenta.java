package com.example.tiendacoches.Vista.Fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tiendacoches.Model.Leche;
import com.example.tiendacoches.Model.Tienda;
import com.example.tiendacoches.R;


public class FragmentoVenta extends Fragment {

    private Leche leche;


    //En el fragmentoDetalle cargamos directamente la vista desde el fragmento
    public interface IOnAtachListener {
        Leche getLeche();
    }

    private TextView tvMarca;
    private TextView tvStock;
    private TextView tvPrecioVenta;
    private TextView tvComunicacion;
    private TextView tvResumenCompra;
    private EditText etCantidad;
    private Button btnComprar;
    private Tienda tienda;
    private final StringBuilder sb;

    public FragmentoVenta() {
        super(R.layout.item_detalle_venta);
        sb = new StringBuilder();
        tienda = new Tienda();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAtachListener iOnAtachListener = (IOnAtachListener) context;
        leche = iOnAtachListener.getLeche();

    }


    @Override
    public void onViewCreated(@NonNull View itemView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);
        tvMarca = itemView.findViewById(R.id.tvNombre);
        tvStock = itemView.findViewById(R.id.tvStock);
        tvPrecioVenta = itemView.findViewById(R.id.tvPrecioVenta);
        tvComunicacion = itemView.findViewById(R.id.tvComunicacion);
        tvResumenCompra = itemView.findViewById(R.id.tvResumenCompra);
        etCantidad = itemView.findViewById(R.id.editTextNumber);
        btnComprar = itemView.findViewById(R.id.button);
        if (leche != null) {
            cargarDatos(leche);
            vender(leche);
        }

    }


    public void cargarDatos(Leche leche) {
        this.leche = leche;
        sb.setLength(0);
        sb.append("Nombre del Producto ").append(leche.getMarca());
        tvMarca.setText(sb);
        sb.setLength(0);
        sb.append("Numero de Stock ").append((leche.getNumeroStock()));
        tvStock.setText(sb);
        sb.setLength(0);
        sb.append("Precio de venta ").append(leche.redondear(leche.getPrecioVenta())).append("$");
        tvPrecioVenta.setText(sb);
    }

    public void vender(Leche leche) {


        String numberStr = "0";

        etCantidad.setText(numberStr);

        btnComprar.setOnClickListener(v -> {
            etCantidad.setText(etCantidad.getText());
            int cantidad = Integer.parseInt(etCantidad.getText().toString());
            System.out.println("cantidad cogida " + cantidad);
            tienda.ventaLeche(leche, cantidad);
            tvComunicacion.setText("SE HA ANAYADIDO A LA CESTA");
            tienda.resumenCompra();
            tvResumenCompra.setText(tienda.getMensaje());
        });

        tvComunicacion.setText(tienda.getMensaje());


    }


}
