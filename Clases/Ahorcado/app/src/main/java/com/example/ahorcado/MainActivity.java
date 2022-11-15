package com.example.ahorcado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "message";
    private int NUMERO_INTENTOS = 6;
    private List<String> palabras;
    private String palabra;
    ///////////////

    private Button btnJugar;
    private List<Character>lista;
    private EditText etLetra;
    private TextView tvPalabraEscondida;
    private TextView tvInformacionGeneral;
    private TextView tvNumeroIntentos;
    private TextView tvPalabra;
    private ImageView  imageView;
    private String letra;

    //JUEGO DEL AHORCADO
        //1- Tenemos que meter los datos en una lista
        //2- Reglas del juego; 6 intentos para adivinar la palabra.


    public MainActivity() {
        super(R.layout.activity_main);
        palabras = new ArrayList<>();
    }

    public String eliminarAcentos(String palabraAcentuada){
        return StringUtils.stripAccents(palabraAcentuada);
    }


    public void cargarDatos() {
        String linea;
        InputStream inputStream = this.getResources().openRawResource(R.raw.words_es);
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

        int indicePalabra = randBetween(0,palabras.size()-1);
        try{
            palabra = palabras.get(indicePalabra);
        }catch (NullPointerException io){
            io.printStackTrace();
        }
        palabra = eliminarAcentos(palabra);
    }

    public int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnJugar = findViewById(R.id.button);
        etLetra = findViewById(R.id.etNumero);
        tvNumeroIntentos = findViewById(R.id.tvInformacionIntentos);
        tvPalabra = findViewById(R.id.tvPalabra);
        tvPalabraEscondida = findViewById(R.id.tvPalabraEscondida);
        tvInformacionGeneral = findViewById(R.id.tvInformacionGeneral);
        imageView = findViewById(R.id.imageView);
        //CARGAMOS LOS DATOS
        cargarDatos();
        //EMPEZAMOS A JUGAR
        btnJugar.setOnClickListener(this);
    }


    public void jugar(){
        boolean salida = false;
        boolean acierto = false;
        boolean palabraEquivocada = false;
        boolean partidaPerdida = false;

        do {

            for (int i = 0; i < palabra.length(); i++) {
//                btnJugar.setOnClickListener(this);
                if (palabra.contains(letra)) {
                    tvNumeroIntentos.setText("Palabra encontrada");
                    rellenarPalabra(letra.charAt(0));
                    acierto = true;
                    salida = true;
                } else {
                    palabraEquivocada = true;
                    salida = true;
                }
                if (NUMERO_INTENTOS ==0) {
                    partidaPerdida = true;
//                    salida = true;
                }

            }

        } while (!salida);

        if (palabraEquivocada){
            tvNumeroIntentos.setText("Palabra no encontrada " + NUMERO_INTENTOS);
            --NUMERO_INTENTOS;
            String nombre = "hangman_"+NUMERO_INTENTOS;
            int id = this.getResources().getIdentifier(nombre,"drawable",this.getPackageName());
            imageView.setImageResource(id);
        }

        if (partidaPerdida){
            tvNumeroIntentos.setText("Lo siento has perdido");
            tvPalabraEscondida.setVisibility(View.VISIBLE);
            tvPalabraEscondida.setText(palabra);
        }

        //**********************CONVIERTO LA LISTA EN UN STRING *********************************//

        if (acierto){
            tvInformacionGeneral.setText("HAS ACERTADO");
            boolean palabraAcertada;
            String palabraGanada1 = lista.toString().replaceAll(",","") ;
            String palabraGanada2 = palabraGanada1.replaceAll("]","") ;
            String palabraGanada3 = palabraGanada2.replace("[","") ;
            String palabraGanada4 = palabraGanada3.replaceAll("\\s","") ;

            palabraAcertada = palabra.equalsIgnoreCase(palabraGanada4);

            Log.d(TAG, "palabraGanada: " + palabraGanada4);
            Log.d(TAG, "palabraAcertada: " + palabraAcertada);
            Log.d(TAG, "palabra: " + palabra);

            if (palabraAcertada){
                tvInformacionGeneral.setText("HAS GANADO LA PARTIDA");
            }
        }


    }


    public void rellenarPalabra(char elemento) {
        char ch;
        int longitud = palabra.length();

        if (lista == null) {
            lista = new ArrayList<>();
            for (int i = 0; i < longitud; i++) {
                lista.add('_');
            }
        }

        tvPalabra.setText(lista.toString());

        for (int i = 0; i < longitud; i++) {

            if (lista.get(i) == '_' && palabra.charAt(i) == elemento) {
                ch = elemento;
                lista.set(i, ch);
            }

        }
        tvPalabra.setText(lista.toString());

    }



    public void preguntar(){
        String input;
        boolean correcto = false;
        tvInformacionGeneral.setText("Introduce una letra");

        do {
            input = String.valueOf(etLetra.getText());
            if (input.length() == 1) {
                correcto = true;
            }

        } while (!correcto);

        letra = input;
        jugar();

    }


    @Override
    public void onClick(View view) {
        preguntar();
    }


}