package com.example.alumnosfragment.Contoller;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alumnosfragment.Interface.IAlumnoSeleccionado;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.R;

import java.util.List;

public class FragmentListarAlumno extends Fragment {

    private List<Alumno> alumnos;
    private IAlumnoSeleccionado listener;

    public interface IOnAttachListener{
        List<Alumno> getListaAsignaturas();
    }


    public FragmentListarAlumno() {
        //Lo puedo indicar el layout en el constructor o lo puedo indicar en el metodo onCreateView que es lo mismo.
        super(R.layout.recyclerview_lista_alumnos);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
//        assert alumnos != null && listener != null;
        RecyclerView recyclerView = view.findViewById(R.id.rvListarAlumnos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new AdaptadorAlumno(alumnos,listener));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()  , LinearLayoutManager.VERTICAL,false));
    ;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Le inyectamos el contexto de la actividad
            listener = (IAlumnoSeleccionado) context;
        //Referenciamos el contexto a nuestra interfaz
            IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        //Recargamos los datos de nuestra lista
            alumnos = iOnAttachListener.getListaAsignaturas();
    }
}
