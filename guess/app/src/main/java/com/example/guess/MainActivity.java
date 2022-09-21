package com.example.guess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyActivity";
    public static final int MAX_FALLOS = 3;

    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;
    // Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creamos la app
        // button1.findViewById(R.id.button1);
        // button1 = (Button) findViewById(R.id.button1);
        // button1.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View view) {
        // Toast.makeText(getApplicationContext(), "Desde aqui",
        // Toast.LENGTH_LONG).show();
        // }
        // });
    }

    // Vamos a crear primero los numero aleatorios;

    @Override
    public void onClick(View view) {
        int luckyNumber = Lib.Util.randBetween(1, 10);
        TextView tvLuckyNumber = findViewById(R.id.luckyNumber);
        tvLuckyNumber.setText(String.valueOf(luckyNumber));


        int eleccion = 0;
        int contador = 0;
        boolean correct = false;
        boolean pierde = false;
        do {
            // APP

            switch (view.getId()) {
                case R.id.button1:
                    eleccion = 1;
                    break;
                case R.id.button2:
                    eleccion = 2;
                    break;
                case R.id.button3:
                    eleccion = 3;
                    break;
                case R.id.button4:
                    eleccion = 4;
                    break;
                case R.id.button5:
                    eleccion = 5;
                    break;
                case R.id.button6:
                    eleccion = 6;
                    break;
                case R.id.button7:
                    eleccion = 7;
                    break;
                case R.id.button8:
                    eleccion = 8;
                    break;
                case R.id.button9:
                    eleccion = 9;
                    break;
                case R.id.button10:
                    eleccion = 10;
                    break;
            }
            // si el usuario sobrepasa las oportunidades pierde la partida

            if (contador == MAX_FALLOS) {
                 Toast.makeText(getApplicationContext(), "Has superado los intentospermitidos", Toast.LENGTH_LONG)
                 .show();
                pierde = true;
                correct = true;
            }

            // si falla el usuario el contador aumenta
            if (luckyNumber != eleccion) {
                contador++;
                 Toast.makeText(getApplicationContext(), "Numero incorrecto intentalo denuevo", Toast.LENGTH_LONG)
                 .show();
                correct = false;
            } else {
                // has ganado
                 Toast.makeText(getApplicationContext(), "Ganastes",
                 Toast.LENGTH_LONG).show();
                correct = true;
            }

        } while (!correct);

        if (pierde == true) {
             Toast.makeText(getApplicationContext(), "Perdistes",
             Toast.LENGTH_LONG).show();
        }

    }

}