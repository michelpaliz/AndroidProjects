package com.example.examenciclistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.examenciclistas.Interfaz.IListenerCiclistas;
import com.example.examenciclistas.Modelo.Competition;
import com.example.examenciclistas.Modelo.Cyclist;
import com.example.examenciclistas.Modelo.Parser.CompetitionParser;
import com.example.examenciclistas.Vista.FragmentoDetalle;
import com.example.examenciclistas.Vista.FragmentoLista;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IListenerCiclistas, FragmentoLista.IOnAttachListener, FragmentoDetalle.IOnAttachListener {

    private static final String COMPETITION = "com.examplefragment.contactos";
    private static final String KEY_CICLISTA = "com.examplefragment.contacto";

    private Competition competicion;
    private Cyclist ciclista;
    private FragmentManager fragmentManager;
    private FragmentoDetalle fragmentoDetalle;
    private boolean isDetalle;


    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(COMPETITION, competicion);
        outState.putSerializable(KEY_CICLISTA,ciclista);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            competicion = (Competition) savedInstanceState.getSerializable(COMPETITION);
            ciclista = (Cyclist) savedInstanceState.getSerializable(KEY_CICLISTA);
        }
        fragmentManager = getSupportFragmentManager();
        isDetalle = findViewById(R.id.FragmentoDetalle) != null;
        if (isDetalle){
            fragmentoDetalle = (FragmentoDetalle) fragmentManager.findFragmentById(R.id.FragmentoDetalle);
        }

    }



    @Override
    public Competition getCompeticion() {
        if (competicion== null){
            cargarDatos();
        }
        return competicion;
    }

    public void cargarDatos(){
        CompetitionParser competitionParser = new CompetitionParser(this);
        if (competitionParser.parse()){
            competicion = competitionParser.getCompetition();
        }else{
            throw new NullPointerException();
        }
    }

    @Override
    public void onCiclistaSeleccionado(int posicion) {
        ciclista = competicion.getCyclists()[posicion];
        System.out.println("posiccion " + posicion);

        if (ciclista == null){
            throw new NullPointerException();
        }

        if (isDetalle){
            setTitle(ciclista.getName());
            fragmentoDetalle.cargarDatos(ciclista);
        }else{
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fragmentContainerView,FragmentoDetalle.class,null).commit();
        }



    }

    @Override
    public Cyclist getCiclista() {
        if (competicion == null){
            cargarDatos();
        }
        if (ciclista == null){
            ciclista = competicion.getCyclists()[0];
        }
        return ciclista;
    }
}