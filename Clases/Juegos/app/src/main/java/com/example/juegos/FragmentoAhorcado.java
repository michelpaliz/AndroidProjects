package com.example.juegos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class FragmentoAhorcado extends Fragment  {


    private Ahorcado ahorcado;
    private Toolbar toolbar;
    private Button btnJugar;
    private EditText etLetra;
    private TextView tvNumeroIntentos;
    private TextView tvPalabra;
    private TextView tvPalabraEscondida;
    private TextView tvInformacionGeneral;
    private ImageView imageView;


    public FragmentoAhorcado() {
        super(R.layout.ahorcado);

    }


    //Para inflar el layout del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ahorcado = new Ahorcado(getContext());
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        //RELLENAMOS LOS VIEW
        btnJugar = view.findViewById(R.id.button);
        etLetra = view.findViewById(R.id.etNumero);
        tvNumeroIntentos = view.findViewById(R.id.tvInformacionIntentos);
        tvPalabra = view.findViewById(R.id.tvPalabra);
        tvPalabraEscondida = view.findViewById(R.id.tvPalabraEscondida);
        tvInformacionGeneral = view.findViewById(R.id.tvInformacionGeneral);
        imageView = view.findViewById(R.id.imageView);

    }



    //Para cuando el fragmento esta apunto de ser visible
    @Override
    public void onStart() {
        super.onStart();

        tvPalabra.setText(ahorcado.printarIndices());

        btnJugar.setOnClickListener(view -> {

            boolean palabraEquivocada = ahorcado.palabraEquivocada;
            boolean partidaPerdida = ahorcado.partidaPerdida;
            boolean acierto = ahorcado.acierto;
            boolean palabraAcertada = ahorcado.palabraAcertada;

            tvInformacionGeneral.setText(ahorcado.getMensajeFinal());

            if (view != null) {


                ahorcado.preguntar(String.valueOf(etLetra.getText()));


                tvNumeroIntentos.setText(ahorcado.getMensaje());


                tvPalabra.setText(ahorcado.getPrintarLista());


                if (!palabraEquivocada){
                    tvNumeroIntentos.setText(ahorcado.getMensaje());
                    imageView.setImageResource(ahorcado.getIdImg());
                    tvPalabra.setText(ahorcado.getPrintarLista());
                }



                if (partidaPerdida) {
                    tvNumeroIntentos.setText(ahorcado.getMensaje());
                    tvInformacionGeneral.setText(ahorcado.getMensajeFinal());
                    tvPalabraEscondida.setVisibility(View.VISIBLE);
                    tvPalabraEscondida.setText(ahorcado.getPalabra().toUpperCase());

                }

                if (acierto) {
                    tvNumeroIntentos.setText(ahorcado.getMensaje());
                    if (palabraAcertada) {
                        tvNumeroIntentos.setText(ahorcado.getMensaje());
                    }
                }
            }
        });


    }





    //Para cuando el fragmento es asociado con una actividad
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




}
