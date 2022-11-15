package com.example.tresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//importante importar este paquete no import android.widget.gridLayout
import androidx.gridlayout.widget.GridLayout;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    //JUEGO DE 3 EN RAYA
        //CREAMOS LA MATRIZ
        //HACEMOS QUE LOS INDICES DE LA MATRIZ SEAN BOTONES
        //LA CPU NOS DARA UN NUMERO AL AZAR DEL 0-2 PARA CADA INDICE

    //VALORES ESTATICOS PARA CADA JUGADOR

    private static final String TAG = "Message";


    private char [][] gameBoard;
    private List<Integer> jugadorPosicion;
    private List<Integer> computadoraPosicion;
    private static final String jugadorValor = "o";
    private static final String computadoraValor = "x";


    //PARA PONER LOS INDICES DE LOS JUGADORES
    private boolean isJugador = true;
    private boolean isComputadora = false;
    private boolean partidaTerminada = false;
    private boolean  partidaEmpatada ;

    //VARIABLES
    private Button btReset;
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
    private Set<Integer> numerosPrevios;

    public MainActivity() {
        super(R.layout.activity_main);
        jugadorPosicion = new ArrayList<>();
        computadoraPosicion = new ArrayList<>();
        gameBoard = new char[3][3];
        numerosPrevios = new HashSet<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInformacion = findViewById(R.id.tvInformacion);
        tvSize = findViewById(R.id.tvSize);
        btReset = findViewById(R.id.btnReset);
        ib00 = findViewById(R.id.ib00);
        ib01 = findViewById(R.id.ib01);
        ib02 = findViewById(R.id.ib02);
        ib10 = findViewById(R.id.ib10);
        ib11= findViewById(R.id.ib11);
        ib12 = findViewById(R.id.ib12);
        ib20 = findViewById(R.id.ib20);
        ib21 = findViewById(R.id.ib21);
        ib22 = findViewById(R.id.ib22);
        //EMPEZAR EL JUEGO
        jugar();

    }


    public void jugar(){

        if (isJugador){
            GridLayout gridLayout = findViewById(R.id.gridLayout);
            setSingleEvent(gridLayout);
        }

    }




    public void setSingleEvent(GridLayout gridLayout){

                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        ImageView button = (ImageView) gridLayout.getChildAt(i);
                        int finalIterator = i;

                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                if (isJugador){
                                    tvInformacion.setText("TU TURNO");
                                    Toast.makeText(getApplicationContext(),"clicked at i " + finalIterator, Toast.LENGTH_SHORT).show();
                                    numerosPrevios.add(finalIterator);
                                    cargarDatos(finalIterator);
                                    isJugador = false;
                                    isComputadora = true;
                                }
                                tvSize.setText(String.valueOf(numerosPrevios.size()));

                                if (numerosPrevios.size()<8)
                                    if (isComputadora){
                                        int numeroComputadora ;
                                        boolean noRepetido = false;

                                        do {
                                             numeroComputadora = randBetween(0, 7);
                                            if (numerosPrevios.add(numeroComputadora)){
                                                    noRepetido = true;
                                            }

                                        }while(!noRepetido);

                                        numerosPrevios.add(numeroComputadora);
                                        tvInformacion.setText("TURNO DE LA COMPUTADORA " + numerosPrevios.toString());
                                        cargarDatos(numeroComputadora);
                                        Toast.makeText(getApplicationContext(),"Computer clicked at i " + numeroComputadora, Toast.LENGTH_SHORT).show();
                                        isJugador = true;
                                        isComputadora = false;
                                    }


                            }
                        });

                }

    }

    public void cargarDatos(int indice){

        //CARGAMOS LA IMAGEN
        int id = 0;
        char valor = 'c';
        if (isJugador){
            valor = jugadorValor.charAt(0);
            id = this.getResources().getIdentifier(jugadorValor,"drawable",this.getPackageName());
            jugadorPosicion.add(indice);
        }
        if (isComputadora){
            valor= computadoraValor.charAt(0);
            id = this.getResources().getIdentifier(computadoraValor,"drawable",this.getPackageName());
            computadoraPosicion.add(indice);
        }
        //CARGAMOS LOS DATOS
        switch (indice){
            case 0:
                    ib00.setImageResource(id);
                    gameBoard[0][0] = valor;
                break;
            case 1:
                    ib01.setImageResource(id);
                    gameBoard[0][1] = valor;
                break;
            case 2:
                    ib02.setImageResource(id);
                    gameBoard[0][2] = valor;
                break;
            case 3:
                    ib10.setImageResource(id);
                    gameBoard[1][0] = valor;
                    break;
            case 4:
                    ib11.setImageResource(id);
                    gameBoard[1][1] = valor;
                    break;
            case 5:
                    ib12.setImageResource(id);
                    gameBoard[1][2] = valor;
                    break;
            case 6:
                   ib20.setImageResource(id);
                    gameBoard[2][0] = valor;
                    break;
            case 7:
                    ib21.setImageResource(id);
                    gameBoard[2][1] = valor;
                    break;
            case 8:
                    ib22.setImageResource(id);
                    gameBoard[2][2] = valor;
                    break;
        }


        printGameBoard();
        checkwinner();

        System.out.println("numerosprevios " + numerosPrevios.size());
        System.out.println("boolean " + partidaTerminada);

        partidaEmpatada = numerosPrevios.size() == 9;
        if (partidaTerminada){
            tvSize.setText("Puedes comenzar otra partida, dale al boton !");
            botonReset();
        }else if (partidaEmpatada){
            tvSize.setText("EMPATE. Puedes comenzar otra partida, dale al boton !");
            botonReset();
        }else{
        tvSize.setText("Debes terminar la partida primero antes de comenzar otra");
        }

    }

    public void printGameBoard(){
        for (char[] row: gameBoard) {
            for (char column: row) {
                System.out.print(column);
            }
            System.out.println();
        }
    }

    public void checkwinner(){
        List<Integer> topRow = Arrays.asList(0,1,2);
        List<Integer> midRow = Arrays.asList(3,4,5);
        List<Integer> bottomRow = Arrays.asList(6,7,8);
        List<Integer> leftColumn = Arrays.asList(0,3,6);
        List<Integer> midColumn = Arrays.asList(1,4,7);
        List<Integer> rightColumn = Arrays.asList(2,5,8);
        List<Integer> leftCross = Arrays.asList(0,4,8);
        List<Integer> rightCross = Arrays.asList(2,4,6);

        List<List<Integer>>condicionesGanador = new ArrayList<>();
        condicionesGanador.add(topRow);
        condicionesGanador.add(midRow);
        condicionesGanador.add(bottomRow);
        condicionesGanador.add(leftColumn);
        condicionesGanador.add(rightColumn);
        condicionesGanador.add(midColumn);
        condicionesGanador.add(leftCross);
        condicionesGanador.add(rightCross);

        for (List<Integer> lista : condicionesGanador){
            if (jugadorPosicion.containsAll(lista)){
                partidaTerminada = true;
                tvInformacion.setText("PARTIDA HA TERMINADO HAS GANADO");
                System.out.println("HAS GANADO");
            }else if (computadoraPosicion.containsAll(lista)){
                partidaTerminada = true;
                tvInformacion.setText("PARTIDA HA TERMINADO LA COMPUTADORA HA GANADO");
                System.out.println("HA GANADO LA COMPUTADORA");
            }

        }



    }

    public void botonReset(){
        btReset.setOnClickListener(view -> {
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
        });
    }

    public int random(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    public int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }









}