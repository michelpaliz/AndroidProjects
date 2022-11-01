package com.example.gridpais;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gridpais.Adaptador.ListViewAdapter;
import com.example.gridpais.Model.Pais;
import com.example.gridpais.Parser.ParserPais;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //
    private GridView gridView;
    private ListViewAdapter adaptador;
    private ArrayList<Pais> listaPaises;
    private ParserPais parser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializamos
        gridView = (GridView) findViewById(R.id.idGrid);
        listaPaises = new ArrayList<>();
        parser = new ParserPais(this);
        //Parseamos la lista con nuestro XML
        if (parser.parserPaisesXML()){
            listaPaises = (ArrayList<Pais>) parser.getPaises();
            adaptador = new ListViewAdapter(this, listaPaises);
            gridView.setAdapter(adaptador);
        }else{
            Toast.makeText(this, "No se pudieron obtener los datos de los países", Toast.LENGTH_SHORT).show();
        }
        Log.e("Lista", listaPaises.toString());
    }
}