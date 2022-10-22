package com.example.alumnosfragment.Contoller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.alumnosfragment.Interface.IAlumno;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.Model.Asignatura;
import com.example.alumnosfragment.Model.Nota;
import com.example.alumnosfragment.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IAlumno, FragmentOnAttachListener {

    private List<Alumno> alumnos;
    private List<Asignatura> asignaturas;
    private FragmentListarAlumno frgListarAlumno;
    private FragmentDetalle getFrgDetalle;
    private boolean isTablet;
    private FragmentDetalle frgDetalle;

    public MainActivity(){
        super(R.layout.activity_main);
        isTablet = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTablet = findViewById(R.id.FrgDetalle) !=null;
        //Parsemos nuestros elementos
        ParserAlumno parserAlumno = new ParserAlumno(getApplicationContext());
        ParserAsignatura parserAsignatura = new ParserAsignatura(getApplicationContext());

        if(parserAlumno.startParser() && parserAsignatura.startParser()){
            alumnos = parserAlumno.getAlumnos();
            asignaturas = parserAsignatura.getAsignaturas();
            Log.d("asignaturasMain", String.valueOf(asignaturas.size()));
            Log.d("alumnosMain", String.valueOf(alumnos.size()));
        }else{
            Log.d("salto","no se ha podido parsear");
        }

        if (savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgListado, FragmentListarAlumno.class, null).commit();
            fragmentManager.addFragmentOnAttachListener(this);
            //            fragmentManager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgListado,new FragmentListarAlumno(alumnos,this), null).commit();
        }


    }


    @Override
    public void onAlumnoSeleccionado(int position) {
        Alumno alumnoRecogido = alumnos.get(position);
        Asignatura asignaturaRecogida = asignaturas.get(position);
//        String notaRecogida = alumnos.get(position).getNotas().get(position).getCalificacion();
        if (isTablet){
            frgDetalle.mostrarDetalle(alumnoRecogido, asignaturaRecogida);
        }else{
//            DetalleActivity detalleActivity = new DetalleActivity(asignaturas);
            Intent intent = new Intent(this,DetalleActivity.class);
//            intent.putExtra(DetalleActivity.EXTRA_NAME, asignaturaRecogida);
            intent.putExtra(DetalleActivity.EXTRA_NAME,alumnoRecogido);
            startActivity(intent);
        }

    }


    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.FrgListado) {
            frgListarAlumno = (FragmentListarAlumno) fragment;
            frgListarAlumno.setContextListener(alumnos, this);
            if(isTablet){
                Bundle bundle = new Bundle();
//                frgDetalle.setAsignaturas(asignaturas);
                bundle.putSerializable(FragmentDetalle.EXTRA_DETALLE, alumnos.get(0));
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgDetalle,FragmentDetalle.class, bundle).commit();
            }
        }
        if (fragment.getId() == R.id.FrgDetalle){
            frgDetalle = (FragmentDetalle) fragment;
        }

    }

}