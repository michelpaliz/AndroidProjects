package com.example.gridcountry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gridcountry.Adaptador.ListViewAdapter;
import com.example.gridcountry.Model.Pais;
import com.example.gridcountry.Parser.ParserPais;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewStub stubGrid;
    private ViewStub stubList;
    private GridView gridView;
    private int currentViewMode = 0;
    private final int VIEW_MODE_LISTVIEW=0;
    private final int VIEW_MODE_GRIDVIEW=1;
    private View container;
    //
    private ListViewAdapter adaptador;
    private ArrayList<Pais> listaPaises;
    private ParserPais parser;
    private ListView listarVista;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializamos
        stubGrid = (ViewStub) findViewById(R.id.stub_grid);
        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubList.inflate();
        stubGrid.inflate();
        //
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        container = inflater.inflate(R.layout.listitem_country, null);
        listarVista = (ListView) container.findViewById(R.id.listview);
        gridView = (GridView) container.findViewById(R.id.gridView);
        listaPaises = new ArrayList<>();
        parser = new ParserPais(this);
        //Parseamos la lista con nuestro XML
        if (parser.parserPaisesXML()){
            listaPaises = (ArrayList<Pais>) parser.getPaises();
            adaptador = new ListViewAdapter(this, listaPaises);
            listarVista.setAdapter(adaptador);
        }else{
            Toast.makeText(this, "No se pudieron obtener los datos de los países", Toast.LENGTH_SHORT).show();
        }
        Log.e("Lista", listaPaises.toString());
    }
}