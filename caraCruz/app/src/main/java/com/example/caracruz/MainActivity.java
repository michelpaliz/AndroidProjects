package com.example.caracruz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Lib.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btCara , btCruz;
    private ImageView img;
    private TextView information;
    private String eleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btCara = (Button) findViewById(R.id.button1);
        btCruz = (Button) findViewById(R.id.button2);
        img = (ImageView) findViewById(R.id.imageView);
        information = (TextView) findViewById(R.id.resultado);
        btCara.setOnClickListener(this);
        btCruz.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Tendriamos que comparar los botones para poder dar significado;
        //Despues por cada boton que surga la image correspondiente.
        String resultado = suerte();

        //Dar significado a eleccion segun lo que el usuario ha escogido
        if (view.getId() == btCara.getId()){
            eleccion =  btCara.getText().toString();
        }else{
            eleccion =  btCruz.getText().toString();
        }

        //Printame la imagen que ha salido
        if (resultado.equalsIgnoreCase(btCara.getText().toString())){
            img.setImageResource(R.drawable.euro_cara);
        }else{
            img.setImageResource(R.drawable.euro_cruz);
        }
        //Mensaje
        if (eleccion.equalsIgnoreCase(resultado)){
            information.setText("Has perdido");
            Toast.makeText(getApplicationContext(),"Has perdido", Toast.LENGTH_SHORT);
        }else{
            information.setText("Has ganado");
            Toast.makeText(getApplicationContext(),"Has ganado", Toast.LENGTH_SHORT);
        }

        
    }

    public String suerte() {
        String resultado;
        int random = Util.randBetween(0, 10);

        if (entero(random) == true) {
            resultado = ("cara");
        } else {
            resultado = ("cruz");
        }
        return resultado;
    }

    public boolean entero(int numero) {
        boolean correcto = false;
        if (numero % 2 == 0) {
            return correcto = true;
        }
        return correcto;
    }

}