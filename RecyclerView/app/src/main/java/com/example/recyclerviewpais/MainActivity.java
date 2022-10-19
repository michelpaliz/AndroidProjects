package com.example.recyclerviewpais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.recyclerviewpais.Adaptador.AdaptadorPais;
import com.example.recyclerviewpais.Interfaz.IPaisListener;
import com.example.recyclerviewpais.Model.Pais;
import com.example.recyclerviewpais.Parser.ParserPais;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPaisListener {

    private List<Pais> listaPaises;
    private RecyclerView rvPaises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarDatos();

        rvPaises = findViewById(R.id.myRecyclerView);
        rvPaises.setHasFixedSize(true);
        rvPaises.setAdapter(new AdaptadorPais((ArrayList<Pais>) listaPaises, this));
        rvPaises.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

    }


    public void  iniciarDatos(){
        listaPaises = new ArrayList<>();
        ParserPais parser = new ParserPais(this);
        if ((parser.parserPaisesXML())){
            listaPaises =  parser.getPaises();
        }else{
            Toast.makeText(this,"Err", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(int posicion) {
        Toast.makeText(this, listaPaises.get(posicion).getNombre(), Toast.LENGTH_SHORT).show();
    }
}