package com.example.saludo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    private TextView titulo;
    private EditText nombre;
    private EditText apellido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titulo = (TextView) findViewById(R.id.textView);
        nombre = (EditText) findViewById(R.id.nombre);
        apellido = (EditText) findViewById(R.id.apellido);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String nmStr = nombre.getText().toString();
        String apStr = apellido.getText().toString();
        titulo.setText("Hola " + nmStr + " " + apStr );
        Toast.makeText(getApplicationContext(),"Hola " + nmStr + " " + apStr,Toast.LENGTH_SHORT).show();
    }
}