package com.example.headfirstbookandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BeerExpert expert = new BeerExpert();

    private TextView tvBrands;
    private TextView tvBeerSeleccionado;
    private Spinner spColor;
    private Button btnFindBeer;
    private String beerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBeerSeleccionado = findViewById(R.id.tvBeerSeleccionado);
        btnFindBeer  =  findViewById(R.id.buttonFindBeer);
        btnFindBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvBrands =  findViewById(R.id.buttonFindBeer);
                spColor =  findViewById(R.id.color);
                //Cojer el producto seleccionador por el usuario
                tvBeerSeleccionado.setText(beerType);
                //Esto nos devuelve a generic Java obj, xq los Spinners pueden almacenar img.
                //Utilizaremos el valueOf para convertirlo el obj a String
                beerType = String.valueOf(spColor.getSelectedItem());
                //Ahora necesitamos pasar el color de la cerveza para que nuestro metodo nos devuelva
                //las disponibles marcas.
                List<String> brandList = expert.getMarcas(beerType);
                StringBuilder brandsFormatted = new StringBuilder();
                for (String brand : brandList) {
                    brandsFormatted.append(brand).append("\n");
                }
                //Mostrar la lista de cervezas;
                tvBeerSeleccionado.setText(brandsFormatted);
            }
        });
    }



}