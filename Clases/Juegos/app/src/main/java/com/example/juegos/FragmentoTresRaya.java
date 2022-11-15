package com.example.juegos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import java.util.Set;

public class FragmentoTresRaya extends Fragment {

    private static final String jugadorValor = "o";
    private static final String computadoraValor = "x";

    private TresRaya tresRaya;
    private Button btReset;
    private Toolbar toolbar;
    private TextView tvSize;
    private TextView tvInformacion;
    private ImageView ib00;
    private  ImageView ib01;
    private  ImageView ib02;
    private ImageView ib10;
    private  ImageView ib11;
    private  ImageView ib12 ;
    private ImageView ib20 ;
    private  ImageView ib21 ;
    private ImageView ib22 ;
    private GridLayout gridLayout;
    private Context context;


    public FragmentoTresRaya() {
        super(R.layout.tres_raya);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tresRaya = new TresRaya();
        context = getContext();
        return super.onCreateView(inflater, container, savedInstanceState);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvInformacion = view.findViewById(R.id.tvInformacion);
        tvSize = view.findViewById(R.id.tvSize);
        btReset = view.findViewById(R.id.btnReset);
        ib00 = view.findViewById(R.id.ib00);
        ib01 = view.findViewById(R.id.ib01);
        ib02 = view.findViewById(R.id.ib02);
        ib10 = view.findViewById(R.id.ib10);
        ib11= view.findViewById(R.id.ib11);
        ib12 = view.findViewById(R.id.ib12);
        ib20 = view.findViewById(R.id.ib20);
        ib21 = view.findViewById(R.id.ib21);
        ib22 = view.findViewById(R.id.ib22);
        gridLayout = view.findViewById(R.id.gridLayout);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        setSingleEvent();

//        gridLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView ivButton = (ImageView) gridLayout.getChildAt(i);
            int finalIterator = i;

            ivButton.setOnClickListener(view1 -> {
                tresRaya.logica(finalIterator);
                setSingleEvent();


            });

        }



    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setSingleEvent(){


        boolean isJugador = tresRaya.isJugador;
        boolean isComputadora = tresRaya.isComputadora;
        boolean partidaTerminada = tresRaya.partidaTerminada;
        boolean  partidaEmpatada = tresRaya.partidaEmpatada;
        boolean cargarImagenes = tresRaya.cargarImagenes;


        tvInformacion.setText(tresRaya.getMensaje());


        if (partidaTerminada){
            tvInformacion.setText(tresRaya.getMensaje());
        }else if(partidaEmpatada){
            tvInformacion.setText(tresRaya.getMensaje());
        }else{
            tvInformacion.setText(tresRaya.getMensaje());
        }




        System.out.println(isJugador);
        System.out.println(isComputadora);


        if (cargarImagenes){

            int id = 0;

            if (isJugador){
                id = this.getResources().getIdentifier(jugadorValor,"drawable",context.getPackageName());
            }

            if (isComputadora){
                id = this.getResources().getIdentifier(computadoraValor,"drawable",context.getPackageName());
            }


            switch (tresRaya.getIndice()){
                case 0:
                    ib00.setImageResource(id);
                    break;
                case 1:
                    ib01.setImageResource(id);
                    break;
                case 2:
                    ib02.setImageResource(id);
                    break;
                case 3:
                    ib10.setImageResource(id);
                    break;
                case 4:
                    ib11.setImageResource(id);
                    break;
                case 5:
                    ib12.setImageResource(id);
                    break;
                case 6:
                    ib20.setImageResource(id);
                    break;
                case 7:
                    ib21.setImageResource(id);
                    break;
                case 8:
                    ib22.setImageResource(id);
                    break;
            }




        }



        if (partidaTerminada){
            botonReset();
        }else if (partidaEmpatada){
            botonReset();
        }



    }

    public void botonReset(){
        btReset.setOnClickListener(view -> {
            Intent intent = new Intent(context,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getResources().finishPreloading();
            context.startActivity(intent);
        });
    }



}
