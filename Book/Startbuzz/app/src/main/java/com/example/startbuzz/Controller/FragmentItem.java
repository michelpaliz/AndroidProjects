package com.example.startbuzz.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startbuzz.Model.Coffe;
import com.example.startbuzz.R;

public class FragmentItem extends Fragment {

    public static final String EXTRA_MESANJE =  "MENSAJE";
    private Coffe coffe;

    public FragmentItem() {
        super(R.layout.fragment_item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            coffe = (Coffe) getArguments().getSerializable(EXTRA_MESANJE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (coffe !=null){
            mostrarFragmento(coffe);
        }

    }


    public void mostrarFragmento(Coffe coffe){
        final ImageView ivImagen = requireView().findViewById(R.id.ivImagenFragment);
        final TextView tvTitulo = requireView().findViewById(R.id.tvTituloFragment);
        final TextView tvPrecio = requireView().findViewById(R.id.tvPrecioFragment);
        final TextView tvDescripcion = requireView().findViewById(R.id.tvDescripcionLargaFragment);
        ivImagen.setImageResource(coffe.getImg());
        tvTitulo.setText(coffe.getNombre());
        tvPrecio.setText(coffe.getPrecio());
        tvDescripcion.setText(coffe.getDescripcionLarga());
    }
}
