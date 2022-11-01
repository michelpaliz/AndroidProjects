package com.example.selectcountry;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selectcountry.Adaptador.AdaptadorPais;
import com.example.selectcountry.Model.Pais;
import com.example.selectcountry.Parser.ParserPais;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private AdaptadorPais adaptador;
    private ArrayList<Pais> listaPaises;
    private ParserPais parser;
    private ListView listarVista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //inicializamos
        listarVista = (ListView) findViewById(R.id.listview);
        listaPaises = new ArrayList<>();
        parser = new ParserPais(this);
        //Parseamos la lista con nuestro XML
        if (parser.parserPaisesXML()){
            listaPaises = (ArrayList<Pais>) parser.getPaises();
            adaptador = new AdaptadorPais(this, listaPaises);
            listarVista.setAdapter(adaptador);
        }
        //Implementamos nuestro adaptador


        Log.e("Lista", listaPaises.toString());
    }




}