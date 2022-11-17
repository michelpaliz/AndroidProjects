package com.example.tiendacoches.Vista.Fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tiendacoches.Model.Leche;
import com.example.tiendacoches.R;


public class FragmentoDetalle extends Fragment {

    private Leche leche;


    //En el fragmentoDetalle cargamos directamente la vista desde el fragmento
    public interface IOnAtachListener {
        Leche getLeche();
    }

    private TextView tvMarca;
    private TextView tvStock;
    private TextView tvPrecioVenta;
    private TextView tvPrecioCompra;
    private TextView tvFechaCompra;
    private TextView tvFechaCaducacion;
    private TextView tvCaducacion;
    private final StringBuilder sb;

    public FragmentoDetalle() {
        super(R.layout.item_detalle_producto);
        sb = new StringBuilder();
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
        tvPrecioCompra = itemView.findViewById(R.id.tvPrecioCompra);
        tvFechaCompra = itemView.findViewById(R.id.tvFechaCompra);
        tvPrecioVenta = itemView.findViewById(R.id.tvPrecioVenta);
        tvFechaCaducacion = itemView.findViewById(R.id.tvFechaCaducacion);
        tvCaducacion = itemView.findViewById(R.id.tvCaducacion);
        if (leche != null) {
            cargarDatos(leche);
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
        sb.append("Precio de compra").append(leche.redondear(leche.getPrecioUnidad())).append("$");
        tvPrecioCompra.setText(sb);
        sb.setLength(0);
        sb.append("Fecha de compra ").append(leche.getFechaCompraStock());
        tvFechaCompra.setText(sb);
        sb.setLength(0);
        sb.append("Fecha de caducidad").append(leche.getFechaCaducidad());
        tvFechaCaducacion.setText(sb);
        sb.setLength(0);
        sb.append("Precio de venta ").append(leche.redondear(leche.getPrecioVenta())).append("$");
        tvPrecioVenta.setText(sb);
        sb.setLength(0);
        sb.append("Caducidad").append("\n").append(leche.getIsCaducado());
        tvCaducacion.setText(sb);

    }




}
