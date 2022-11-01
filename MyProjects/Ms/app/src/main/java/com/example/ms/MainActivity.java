package com.example.ms;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ms.Conf.Conf;
import com.example.ms.Lib.Util;
import com.example.ms.Obj.Jugador;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn;
    private ImageView ivDado;
    private TextView tvPregunta;
    private TextView tvNombre;
    private EditText etRespuestaInput;
    private ArrayList<String> opciones;
    private Spinner apuestasJ1;
    private Spinner apuestasJ2;
    private Jugador jugador1;
    private Jugador jugador2;
    private Adapter adaptador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creamos los objetos del jugador
        jugador1 = new Jugador();
        //TextView
        tvPregunta = (TextView) findViewById(R.id.tvPregunta);
        tvNombre = (TextView) findViewById(R.id.tvNombreJ1);
        //Image
        ivDado = (ImageView) findViewById(R.id.imgDado);
        //Spinner
         apuestasJ1 = (Spinner) findViewById(R.id.opcionesJ1);
        //EditText
        etRespuestaInput = (EditText) findViewById(R.id.respuesta);
        //Button
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        //Variables locales


        //Primeramente el j1 tendria que insertar el nombre y despues tendria que meter las opciones de juego que el quiere
        setNombreJugador();
        setRetosJugador();
        Log.e("mensaje",jugador1.toString());



    }


    @SuppressLint("SetTextI18n")
    public void setNombreJugador(){
        String strRespuesta;
        strRespuesta = etRespuestaInput.getText().toString();

        if(jugador1.getNombre() == null){
            jugador1.setNombre(strRespuesta);
            tvNombre.setText("JUGADOR " + jugador1.getNombre().toUpperCase());
        }
    }

    public void setRetosJugador(){
        String mensaje= "Ahora introduce otro reto mas";
        String mensajeError = "Introduce una opcion valida";
        String reto;

        boolean correcto = false;
        int contadorOpciones = 0;


        tvPregunta.setText("Ahora introduce las opciones que quieres apostar a tu rival\n (tienes 5 opciones)");
        do{
            reto =  etRespuestaInput.getText().toString();

                reto = Util.validateStringAndroid(reto,mensajeError);
                if (reto.equalsIgnoreCase(mensajeError)){
                    jugador1.getRetos().add("error no valida opcion");
                }
                jugador1.getRetos().add(reto);
                contadorOpciones++;
                tvPregunta.setText(mensaje);

        }while (contadorOpciones == 5);
        
        
    }



}

