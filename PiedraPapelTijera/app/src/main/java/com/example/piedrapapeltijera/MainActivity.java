package com.example.piedrapapeltijera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MAX_PUNTOS = 3;
    private int empate = 0;
    private int puntosUsuario = 0;
    private int puntosMaquina = 0;
    private Button btnPiedra;
    private Button btnPapel;
    private Button btnTijera;
    private TextView resultado;
    private int eleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPiedra = (Button) findViewById(R.id.btnPiedra);
        btnPapel = (Button) findViewById(R.id.btnPapel);
        btnTijera = (Button) findViewById(R.id.btnTijera);


    }

    @Override
    public void onClick(View view) {


        //Dar significado a las variables que el usuario escoje
        switch (view.getId()) {
            case R.id.btnPiedra:
                eleccion = 1;
//                eleccion = btnPiedra.getText().toString();
                break;
            case R.id.btnPapel:
                eleccion = 2;
//                eleccion = btnPapel.getText().toString();
                break;
            case R.id.btnTijera:
                eleccion = 3;
//                eleccion = btnTijera.getText().toString();
                break;

        }

        int resultadoMaquina = maquina();
        String combinacion = eleccion + "" + resultadoMaquina;
//        resultado.setText(combinacion);
        if (combinacion.equalsIgnoreCase("13") || combinacion.equalsIgnoreCase("31")) {
            if ((eleccion == 1 && resultadoMaquina == 1) || (eleccion == 3 && resultadoMaquina == 3)) {
               empate++;
            } else if (resultadoMaquina == 1) {
                puntosMaquina++;
            } else if (eleccion == 1) {
                puntosUsuario++;
            }
        } else if (combinacion.equalsIgnoreCase("21") || combinacion.equalsIgnoreCase("12")) {
            if ((eleccion == 2 && resultadoMaquina == 2) || (eleccion == 1 && resultadoMaquina == 1)) {
                empate++;
            } else if (resultadoMaquina == 2) {
                puntosMaquina++;
            } else if(eleccion == 2 ){
                puntosUsuario++;
            }
        } else if (combinacion.equalsIgnoreCase("32") || combinacion.equalsIgnoreCase("23")) {

            if ((eleccion == 3 && resultadoMaquina == 3) || (eleccion == 2 && resultadoMaquina == 2))  {
                empate++;
            } else if (resultadoMaquina == 3) {
                puntosMaquina++;
            } else if(eleccion == 3 ){
                puntosUsuario++;
            }

        }

        Toast.makeText(getApplicationContext(),"Empate " + empate + " Puntos usuario" + puntosUsuario + " " + "Puntos Maquina " + puntosMaquina, Toast.LENGTH_SHORT).show();
//        resultado.setText("Puntos Usuario =" + puntosUsuario +" " + "Puntos Maquina" + puntosMaquina);
    }
    public int maquina() {
        int resultado = Lib.Util.randBetween(0, 3);
        return resultado;
    }


}