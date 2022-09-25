package com.example.piedrapapeltijera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MAX_PUNTOS = 3;
    private int empate;
    private int puntosUsuario = 0;
    private int puntosMaquina = 0;
    private Button btnPiedra;
    private Button btnPapel;
    private Button btnTijera;
    private TextView resultado;
    private ImageView imgMaquina;
    private ImageView imgUsuario;
    private int eleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPiedra = (Button) findViewById(R.id.btnPiedra);
        btnPapel = (Button) findViewById(R.id.btnPapel);
        btnTijera = (Button) findViewById(R.id.btnTijera);
        imgMaquina  =  (ImageView)  findViewById(R.id.imgMaquina);
        imgUsuario  =  (ImageView)  findViewById(R.id.imgUsuario);
        resultado = (TextView) findViewById(R.id.resultado);


    }

    @Override
    public void onClick(View view) {

        //Dar significado a las variables que el usuario escoje
        switch (view.getId()) {
            case R.id.btnPiedra:
                imgUsuario.setImageResource(R.drawable.piedra);
                eleccion = 1;
                break;
            case R.id.btnPapel:
                imgUsuario.setImageResource(R.drawable.papel);
                eleccion = 2;
                break;
            case R.id.btnTijera:
                imgUsuario.setImageResource(R.drawable.tijera);
                eleccion = 3;
                break;

        }

        int resultadoMaquina = maquina();
        String combinacion = eleccion + "" + resultadoMaquina;

        //mostramos los imagenes
        if (resultadoMaquina == 1){
            imgMaquina.setImageResource(R.drawable.piedra);
        }else if(resultadoMaquina == 2){
            imgMaquina.setImageResource(R.drawable.papel);
        }else if(resultadoMaquina == 3){
            imgMaquina.setImageResource(R.drawable.tijera);
        }


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

        if(puntosUsuario == 3){
            Toast.makeText(getApplicationContext(),"El usuario ha ganado", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }else if(puntosMaquina == 3){
            Toast.makeText(getApplicationContext(),"La maquina ha ganado", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }

        resultado.setText("Empates " + empate + " Puntos usuario " + puntosUsuario +" " + "Puntos Maquina " + puntosMaquina);
    }



    public int maquina() {
        int resultado = Lib.Util.randBetween(1, 3);
        return resultado;
    }


}