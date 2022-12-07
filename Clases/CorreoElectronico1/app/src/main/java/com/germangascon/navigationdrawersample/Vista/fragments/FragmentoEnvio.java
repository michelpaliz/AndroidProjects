package com.germangascon.navigationdrawersample.Vista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.germangascon.navigationdrawersample.Interfaz.IOnCorreoSeleccionado;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.R;
import com.germangascon.navigationdrawersample.Vista.Adaptadores.AdaptadorEliminados;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentoEnvio extends Fragment {


    private TextView tvAsunto;
    private TextView tvDescripcion;
    private TextView tvCorreoDestino;
    private Button button;

    public FragmentoEnvio() {
        super(R.layout.fragmento_envio);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_envio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAsunto = view.findViewById(R.id.tvAsuntoEnvio);
        tvCorreoDestino = view.findViewById(R.id.tvCorreoDestinoEnvio);
        tvDescripcion = view.findViewById(R.id.tvTextoEnviado);
        button = view.findViewById(R.id.btnEnviar);

        button.setOnClickListener(v -> {
            if (tvCorreoDestino.getText().length() > 0 & tvCorreoDestino.getText().toString().contains("@")){
                Toast.makeText(getContext(),"Correo enviado" +
                        " ", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Introduce al menos el correo de destino", Toast.LENGTH_SHORT).show();

            }

        });


    }


}
