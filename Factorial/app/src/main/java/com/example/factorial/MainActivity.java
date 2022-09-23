package com.example.factorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {


    private TextView resultado;
    private EditText numero;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultado = (TextView) findViewById(R.id.resultado);
        numero = (EditText) findViewById(R.id.calcularFactorial) ;
        button = (Button) findViewById(R.id.button);

    }

    protected int calcularFactorial(int numero )
    {
        int factorial = 1;

        if (numero % 2 == 0) {
            for (int i = factorial; i <= numero; ++i) {
                factorial *= i;
            }
            System.out.println(factorial);
        } else {
            System.out.println("Error");
        }
        return factorial;
    }


    @Override
    public void onClick(View view) {
        int factorial = Integer.parseInt(numero.getText().toString());
        int importe = calcularFactorial(factorial);
        resultado.setText(importe + "");
        if (view.getId() == R.id.resultado)
            Toast.makeText(getApplicationContext(),"Resultado es " +""+ importe, Toast.LENGTH_SHORT ).show();
    }
}