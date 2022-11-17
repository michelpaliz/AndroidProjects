package com.example.alumnosfragment.Contoller;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.alumnosfragment.Interface.IAlumnoSeleccionado;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.R;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentListarAlumno.IOnAttachListener, IAlumnoSeleccionado, FragmentAlumnoDetalle.IOnAttachListener {

    private static final String KEY_ALUMNO = "ALUMNO";
    private static final String KEY_ALUMNOS = "ALUMNOS";

    ///
    private List<Alumno> alumnos;
    private Alumno alumno;

    private  FragmentAlumnoDetalle frgDetalle;
    private boolean isTablet;

    public MainActivity() {
        super(R.layout.activity_main);    ///
        isTablet = false;
        frgDetalle = null;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        //GUARDAMOS NUESTROS OBJETOS ANTES DE QUE SEAN DESTRUIDOS
            outState.putSerializable(KEY_ALUMNOS, (Serializable) alumnos);
            outState.putSerializable(KEY_ALUMNO, alumno);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //SEGUNDA VEZ QUE ENTRAMOS A LA APP
            if (savedInstanceState != null) {
                alumnos = (List<Alumno>) savedInstanceState.getSerializable(KEY_ALUMNOS);
                alumno = (Alumno) savedInstanceState.getSerializable(KEY_ALUMNO);
            }

        //COMENZAMOS LA APP
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //SI ES TABLET LLAMOS DIRECTAMENTE AL FRAGMENT MEDIANTE EL FRAGMENT MANAGER
            isTablet = findViewById(R.id.FrgDetalle) != null;
            if (isTablet) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                frgDetalle = (FragmentAlumnoDetalle) fragmentManager.findFragmentById(R.id.FrgDetalle);

            }


    }

    public void cargarDatos() {
        ParserAlumno parserAlumno = new ParserAlumno(getApplicationContext());
        if (parserAlumno.startParser()) {
            alumnos = parserAlumno.getAlumnos();
        } else {
            Toast.makeText(this, "No se ha pasado los datos correctamente", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *
     * @param position que obtenemos desde el AdaptadorAlumno
     */
    @Override
    public void onAlumnoSeleccionado(int position) {
        Alumno alumnoRecogido = alumnos.get(position);
        if (isTablet) {
            setTitle(alumnoRecogido.getNombre());
            frgDetalle.mostrarDetalle(alumnoRecogido);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.FrgListado, FragmentAlumnoDetalle.class,null).commit();
        }

    }

    @Override
    public Alumno getAsignaturaDetalle() {
        if (alumnos == null){
            cargarDatos();
        }

        if (alumno == null){
            alumno = alumnos.get(0);
        }

        return alumno;
    }

    @Override
    public List<Alumno> getListaAsignaturas() {
        if (alumnos == null){
            cargarDatos();
        }
        return alumnos;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle(R.string.alumnos);
    }
}