package com.example.guess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyActivity";
    private static final int  MAX_FALLOS = 3;
    private static int intentos = 0;
    private static int luckyNumber = Lib.Util.randBetween(1, 10);;
    private static int numero = 0;
    //prueba
    private Button button = (Button) findViewById(R.id.button1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void trampa(View view){
        TextView tvLuckyNumber = findViewById(R.id.luckyNumber);
        tvLuckyNumber.setText(String.valueOf(luckyNumber));
        if(view.getId() == R.id.button){
            Toast.makeText(this, "EL numero secreto es " + luckyNumber, Toast.LENGTH_LONG).show();
        }
    }


    public void game(int eleccion) {
        if (eleccion != luckyNumber && intentos < MAX_FALLOS) {
            ++intentos;
            Toast.makeText(getApplicationContext(), "Intentalo otra vez", Toast.LENGTH_SHORT).show();
        }else if(eleccion != luckyNumber&& intentos == MAX_FALLOS){
            Toast.makeText(getApplicationContext(), "Perdistes", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }else if(eleccion == luckyNumber && intentos < MAX_FALLOS) {
            Toast.makeText(getApplicationContext(), "Ganastes", Toast.LENGTH_LONG).show();
            finishAffinity();
        }
    }


    public void onClick2(View view) {
        trampa(view);
    }


    @Override
    public void onClick(View view) {

        TextView informacion = (TextView) findViewById(R.id.informacion);
        informacion.setText("numero de intentos " + intentos);

//        Log.d("luckyNumber es ","" + luckyNumber);
//        System.out.println("luckynumber" + luckyNumber);
        int eleccion = 0;
        switch (view.getId()) {
            case R.id.button1:
                eleccion = 1;
//                numero = Integer.parseInt(((Button)view).getText().toString());
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

//        Log.d("Eleccion es", "" + eleccion);
//        System.out.println("eleccion" + eleccion);
        game(eleccion);

    }



}