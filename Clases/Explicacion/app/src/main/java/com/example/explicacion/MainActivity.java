package com.example.explicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    private TextView textView;
    private Spinner listarPaises;
    private ArrayAdapter<CharSequence> adaptador; // si es por medio de un xml utilizaremos xml


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializamos
        textView = (TextView) findViewById(R.id.textView);
        listarPaises = (Spinner) findViewById(R.id.spinner);
        //Adaptador
        adaptador = ArrayAdapter.createFromResource(this,R.array.countries_array,android.R.layout.simple_spinner_dropdown_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listarPaises.setAdapter(adaptador);
        //anyadimos el listener para nuestra lista
        listarPaises.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        textView.setText("Seleccionado: " + adapterView.getItemAtPosition(position));
        Toast.makeText(this,"Seleccionado: " + adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        textView.setText("");
    }
}