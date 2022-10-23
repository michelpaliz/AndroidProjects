package com.example.alumnosfragment.Contoller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.alumnosfragment.Interface.IAlumno;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IAlumno, FragmentOnAttachListener {

    private List<Alumno> alumnos;
    private FragmentListarAlumno frgListarAlumno;
    private FragmentDetalle frgDetalle;
    private boolean isTablet;

    public MainActivity(){
        super(R.layout.activity_main);
        isTablet = false;
        frgDetalle = null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Parseamos
        ParserAlumno parserAlumno = new ParserAlumno(getApplicationContext());
        if(parserAlumno.startParser()){
            alumnos = parserAlumno.getAlumnos();
            Log.d("alumnosMain", String.valueOf(alumnos.size()));
            Log.d("mostraralumnos", alumnos.toString());
        }else{
            Log.d("salto","no se ha podido parsear");
        }
        isTablet = findViewById(R.id.FrgDetalle) !=null;

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgListado, FragmentListarAlumno.class, null).commit();
            fragmentManager.addFragmentOnAttachListener(this);


    }


    //

    @Override
    public void onAlumnoSeleccionado(int position) {
        Alumno alumnoRecogido = alumnos.get(position);
        if (isTablet){
            frgDetalle.mostrarDetalle(alumnoRecogido);
        }else{
            Intent intent = new Intent(this,DetalleActivity.class);
            intent.putExtra(DetalleActivity.EXTRA_NAME,alumnoRecogido);
            startActivity(intent);
        }

    }


    //

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.FrgListado) {
            frgListarAlumno = (FragmentListarAlumno) fragment;
            frgListarAlumno.setContextListener(alumnos, this);
            if(isTablet){
                Bundle bundle = new Bundle();
                bundle.putSerializable(FragmentDetalle.EXTRA_DETALLE, alumnos.get(0));
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgDetalle,FragmentDetalle.class, bundle).commit();
            }
        }
    }

}