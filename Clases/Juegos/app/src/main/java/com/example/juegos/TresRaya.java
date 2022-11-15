package com.example.juegos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TresRaya  {

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
    public  boolean isJugador = true;
    public  boolean isComputadora = false;
    public  boolean partidaTerminada = false;
    public boolean  partidaEmpatada ;
    boolean cargarImagenes = false;

    private int indiceImg;

    private String mensaje;

    //VARIABLES
    private Set<Integer> numerosPrevios;

    public int getIndice() {
        return indiceImg;
    }




    public TresRaya() {
        jugadorPosicion = new ArrayList<>();
        computadoraPosicion = new ArrayList<>();
        gameBoard = new char[3][3];
        numerosPrevios = new HashSet<>();
    }



    public String getMensaje() {
        return mensaje;
    }



    public void logica(int finalIterator ){

        if (isJugador){
//                        tvInformacion.setText("TU TURNO");
            numerosPrevios.add(finalIterator);
            cargarDatos(finalIterator);
            isJugador = false;
            isComputadora = true;
        }
//                    tvSize.setText(String.valueOf(numerosPrevios.size()));

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
//                            tvInformacion.setText("TURNO DE LA COMPUTADORA " + numerosPrevios.toString());
                mensaje = "Turno de la computadora";

                cargarDatos(numeroComputadora);
//                Toast.makeText(context,"Computer clicked at i " + numeroComputadora, Toast.LENGTH_SHORT).show();
                isJugador = true;
                isComputadora = false;
            }


    }

    public void cargarDatos(int indice){

        //CARGAMOS LA IMAGEN
        indiceImg = indice;

//        int id = 0;
        char valor = 'c';
        cargarImagenes = true;
        if (isJugador) {
            valor = jugadorValor.charAt(0);
            //            id = context.getResources().getIdentifier(jugadorValor,"drawable",context.getPackageName());
            jugadorPosicion.add(indice);
        }
        if (isComputadora) {
            valor = computadoraValor.charAt(0);
            //            id = context.getResources().getIdentifier(computadoraValor,"drawable",context.getPackageName());
            computadoraPosicion.add(indice);
        }


        //CARGAMOS LOS DATOS
        switch (indice){
            case 0:
//                ib00.setImageResource(id);
                gameBoard[0][0] = valor;
                break;
            case 1:
//                ib01.setImageResource(id);
                gameBoard[0][1] = valor;
                break;
            case 2:
//                ib02.setImageResource(id);
                gameBoard[0][2] = valor;
                break;
            case 3:
//                ib10.setImageResource(id);
                gameBoard[1][0] = valor;
                break;
            case 4:
//                ib11.setImageResource(id);
                gameBoard[1][1] = valor;
                break;
            case 5:
//                ib12.setImageResource(id);
                gameBoard[1][2] = valor;
                break;
            case 6:
//                ib20.setImageResource(id);
                gameBoard[2][0] = valor;
                break;
            case 7:
//                ib21.setImageResource(id);
                gameBoard[2][1] = valor;
                break;
            case 8:
//                ib22.setImageResource(id);
                gameBoard[2][2] = valor;
                break;
        }


        printGameBoard();
        checkwinner();

        System.out.println("numerosprevios " + numerosPrevios.size());
        System.out.println("boolean " + partidaTerminada);

        partidaEmpatada = numerosPrevios.size() == 9;
        if (partidaTerminada){
            mensaje = "Puedes comenzar otra partida, dale al boton !";
//            tvSize.setText("Puedes comenzar otra partida, dale al boton !");
//            botonReset();
        }else if (partidaEmpatada){
//            tvSize.setText("EMPATE. Puedes comenzar otra partida, dale al boton !");
            mensaje = "EMPATE. Puedes comenzar otra partida, dale al boton !";
//            botonReset();
        }else{
            mensaje = "Debes terminar la partida primero antes de comenzar otra";
//            tvSize.setText("Debes terminar la partida primero antes de comenzar otra");
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
                mensaje = "PARTIDA HA TERMINADO HAS GANADO";
//                tvInformacion.setText("PARTIDA HA TERMINADO HAS GANADO");
                System.out.println("HAS GANADO");
            }else if (computadoraPosicion.containsAll(lista)){
                partidaTerminada = true;
//                tvInformacion.setText("PARTIDA HA TERMINADO LA COMPUTADORA HA GANADO");
                mensaje = "PARTIDA HA TERMINADO LA COMPUTADORA HA GANADO";
                System.out.println("HA GANADO LA COMPUTADORA");
            }

        }



    }



    public int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }










}
