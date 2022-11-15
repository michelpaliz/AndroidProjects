package com.example.juegos;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Ahorcado extends Thread implements Runnable {
    private final String TAG = "message";
    private int NUMERO_INTENTOS = 7;
    private final List<String> palabras;
    private String palabra;
    ///////////////

    boolean salida = false;
    boolean acierto = false;
    boolean palabraEquivocada = false;
    boolean partidaPerdida = false;
    boolean palabraAcertada = false;
    boolean mostrarLista = false;

    //////////////////

    private String mensaje, mensajeFinal;
    private String printarLista;
    private List<Character>lista;
    private String letra;
    private int idImg;
    private final Context context;

    //JUEGO DEL AHORCADO
    //1- Tenemos que meter los datos en una lista
    //2- Reglas del juego; 6 intentos para adivinar la palabra.


    public Ahorcado(Context context) {
        palabras = new ArrayList<>();
        this.context = context;
//        cargarDatos();
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

    public String getMensaje() {

        return mensaje;
    }

    public String getPrintarLista() {
        return printarLista;
    }

    public String getPalabra() {
        return palabra;
    }

    public int getIdImg() {
        return idImg;
    }


    public String eliminarAcentos(String palabraAcentuada){
        return StringUtils.stripAccents(palabraAcentuada);
    }


    public void cargarDatos() {
        String linea;
        InputStream inputStream = context.getResources().openRawResource(R.raw.words_es);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            if (inputStream != null){
                while ((linea = bufferedReader.readLine()) != null){
                    palabras.add(linea);
                }
            }
        }catch ( IOException io ){
            io.printStackTrace();
        }


        if (palabra == null ){
            try{
                int indicePalabra = randBetween(0,palabras.size()-1);
                palabra = palabras.get(indicePalabra);
            }catch (NullPointerException io){
                io.printStackTrace();
            }
            palabra = eliminarAcentos(palabra);
        }

        System.out.println(palabra);

    }

    public int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }


    public String printarIndices(){
        cargarDatos();
        String dibujo = palabra;
        String guiones = dibujo.replaceAll("[a-zA-Z]","_");
        return guiones.replaceAll(""," ");
    }


    public void jugar(){
        cargarDatos();
        do {
            mensajeFinal = "Introduce una letra";
            for (int i = 0; i < palabra.length(); i++) {
                if (palabra.contains(letra)) {
                    rellenarPalabra(letra.charAt(0));
                    acierto = true;
                }
                if (!palabra.contains(letra)){
                    palabraEquivocada = true;
                }
                salida = true;
            }

        } while (!salida);



        if (palabraEquivocada){
            NUMERO_INTENTOS = NUMERO_INTENTOS-1;
            mensaje = "Palabara no encontrada , tienes " + NUMERO_INTENTOS + " intentos" ;
            String nombre = "hangman_"+NUMERO_INTENTOS;
            idImg = context.getResources().getIdentifier(nombre,"drawable",context.getPackageName());
            if (lista == null){
                printarLista = printarIndices();
            }
            palabraEquivocada = false;
        }

        partidaPerdida = NUMERO_INTENTOS ==1;
        if(partidaPerdida ){
            mensaje="Has perdido la partida";
            mensajeFinal = "Dale a jugar otra vez para comenzar una nueva partida";
            Thread thread = new Thread(this);
            thread.start();
        }


        //**********************CONVIERTO LA LISTA EN UN STRING *********************************//

        if (acierto){

            String palabraGanada1 = lista.toString().replaceAll(",","") ;
            String palabraGanada2 = palabraGanada1.replaceAll("]","") ;
            String palabraGanada3 = palabraGanada2.replace("[","") ;
            String palabraGanada4 = palabraGanada3.replaceAll("\\s","") ;

            palabraAcertada = palabra.equalsIgnoreCase(palabraGanada4);
            if (palabraAcertada){
                mensaje= "Has ganado la partida";
            }
            Log.d(TAG, "palabraGanada: " + palabraGanada4);
            Log.d(TAG, "palabraAcertada: " + palabraAcertada);
            Log.d(TAG, "palabra: " + palabra);


        }


    }



    public void rellenarPalabra(char elemento) {
        mensaje = "Palabra encontrada, tienes " + NUMERO_INTENTOS + " intentos";
        int longitud = palabra.length();
        mostrarLista = true;

        if (lista == null) {
            lista = new ArrayList<>();
            for (int i = 0; i < longitud; i++) {
                lista.add('_');
            }

        }

        printarLista = lista.toString();

        for (int i = 0; i < longitud; i++) {
            if ( lista.get(i) == '_' && palabra.charAt(i) == elemento) {
                lista.set(i, elemento);
            }

        }

        printarLista = lista.toString();

    }





    public void preguntar(String letraIntroducida){
        String input;
        boolean correcto = false;

        do {
            input = (letraIntroducida);
            if (input.length() == 1) {
                correcto = true;
            }

        } while (!correcto);


        letra = input;

        System.out.println(letra);
        jugar();

    }


    @Override
    public void run() {
        try {
            sleep(3000L);
            Intent intent = new Intent(context,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getResources().finishPreloading();
            context.getApplicationContext().startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
