package com.example.alumnosfragment.Contoller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alumnosfragment.Interface.IAlumno;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.Model.Asignatura;;
import com.example.alumnosfragment.R;

import java.util.List;

public class FragmentDetalle extends Fragment {

    public static final String EXTRA_DETALLE = "com.example.alumnosfragment.EXTRA_DETALLE";
    private TextView tvCodigoAsignatura;
    private TextView tvNombreAsignatura;
    private TextView tvCalificacion;
    private Alumno alumno;
    private Asignatura asignatura;
    private List<Asignatura> asignaturas;
    private IAlumno listener;

    public FragmentDetalle() {
      super(R.layout.recyclerview_lista_notas);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null){
             alumno = (Alumno) getArguments().getSerializable(EXTRA_DETALLE);
//             asignatura = (Asignatura) getArguments().getSerializable(EXTRA_DETALLE);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Esto es para el layout del detalle
        RecyclerView recyclerView = view.findViewById(R.id.rvListarDetalle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new AdaptadorAsignatura(asignaturas, listener));

        if (alumno != null && asignatura != null){
            mostrarDetalle(alumno,asignatura);
        }

    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.recyclerview_lista_notas, container,false );
//        return  view;
////        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    public void setContextListener(List<Asignatura>asignaturas, IAlumno listener){
        this.asignaturas = asignaturas;
        this.listener = listener;
    }

    public void mostrarDetalle(Alumno alumno, Asignatura asignatura){
        tvNombreAsignatura.setText(asignatura.getNombreAsignatura());
        tvCodigoAsignatura.setText(asignatura.getCodigoAsignatura());
        tvCalificacion.setText(String.valueOf(alumno.getNotas()));
//        tvCalificacion.setText(alumno.getNotas().get(posicion).getCalificacion());

    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
