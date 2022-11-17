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
;
import com.example.alumnosfragment.Model.Asignatura;
import com.example.alumnosfragment.R;

public class FragmentAlumnoDetalle extends Fragment {

    public static final String EXTRA_DETALLE = "com.example.alumnosfragment.EXTRA_DETALLE";
    private Alumno alumno;
    private Asignatura asignatura;
    private RecyclerView recyclerView;
    private AdaptadorAsignatura adaptadorAsignatura;


    public interface IOnAttachListener{
        Alumno getAsignaturaDetalle();
    }


    public FragmentAlumnoDetalle() {
      super(R.layout.recyclerview_lista_notas);
      alumno = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null){
             alumno = (Alumno) getArguments().getSerializable(EXTRA_DETALLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Esto es para el layout del detalle
        recyclerView = requireView().findViewById(R.id.rvListarDetalle);
        adaptadorAsignatura = new AdaptadorAsignatura(alumno.getNotas());
        recyclerView.setAdapter(adaptadorAsignatura);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        if (alumno!= null) {
            mostrarDetalle(alumno);
        }


    }


    public void mostrarDetalle(Alumno alumno){
        if (adaptadorAsignatura != null){
            adaptadorAsignatura.setNotas(alumno.getNotas());
            requireActivity().setTitle("Notas de " + alumno.getNombre() + " " + alumno.getApellido() + " " + alumno.getApellido2());
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        alumno = attachListener.getAsignaturaDetalle();
    }



}
