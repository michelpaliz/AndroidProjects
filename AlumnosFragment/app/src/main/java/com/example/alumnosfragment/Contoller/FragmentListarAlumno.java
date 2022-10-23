package com.example.alumnosfragment.Contoller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alumnosfragment.Interface.IAlumno;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.R;

import java.util.List;

public class FragmentListarAlumno extends Fragment {

    private List<Alumno> alumnos;
    private IAlumno listener;

    public FragmentListarAlumno() {
        //Lo puedo indicar el layout en el constructor o lo puedo indicar en el metodo onCreateView que es lo mismo.
        super(R.layout.recyclerview_lista_alumnos);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
//        assert alumnos != null && listener != null;
        RecyclerView recyclerView = view.findViewById(R.id.rvListarAlumnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new AdaptadorAlumno(alumnos,listener));
    }

    public void setContextListener(List<Alumno>alumnos, IAlumno listener){
        this.alumnos = alumnos;
        this.listener = listener;
    }

}
