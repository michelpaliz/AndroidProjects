package com.example.headfirstbookandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView brands;
    private Spinner spColor;
    private Button btnFindBeer;
    private String beerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFindBeer  =  (Button) findViewById(R.id.buttonFindBeer);
        btnFindBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brands = (TextView) findViewById(R.id.buttonFindBeer);
                spColor = (Spinner) findViewById(R.id.color);
                beerType = String.valueOf(spColor.getSelectedItemId());
            }
        });
    }



}