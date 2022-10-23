package com.example.alumnosfragment.Contoller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alumnosfragment.Interface.IAlumno;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.Model.Nota;
import com.example.alumnosfragment.R;

import java.util.List;

public class AdaptadorAlumno extends RecyclerView.Adapter<AdaptadorAlumno.HolderAlumno> {

    private final List<Alumno> alumnos;
    private final IAlumno listener;
    private static String nota;

    public AdaptadorAlumno(List<Alumno> alumnos, IAlumno listener) {
        this.alumnos = alumnos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderAlumno onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_alumnos_recyclerview,parent, false);
        return new HolderAlumno(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAlumno holder, int position) {
        Alumno alumno = alumnos.get(position);
        holder.bindContacto(alumno);
    }

    @Override
    public int getItemCount() {
        if (alumnos == null){
            return  0;
        }
        return alumnos.size();
    }

    public class HolderAlumno extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView tvNombre;
        private final TextView tvEmail;
        private final TextView tvEdad;
        private final IAlumno listener;



        public HolderAlumno(@NonNull View itemView, IAlumno listener) {
            super(itemView);
            this.listener = listener;
            this.tvEdad = itemView.findViewById(R.id.tvEdadItemListarAlumno);
            this.tvNombre = itemView.findViewById(R.id.tvNombreItemListarAlumno);
            this.tvEmail = itemView.findViewById(R.id.tvEmailItemListarAlumno);
            //IMPORTANTISIMO SI NO PONEMOS ESTA LINEA NUESTRA INTERFAZ DE ONCLICK NO FUNCIONARA!!
            itemView.setOnClickListener(this);
        }


        public void bindContacto(Alumno alumno){
            tvNombre.setText(alumno.getNombre());
            tvEmail.setText(alumno.getEmail());
            tvEdad.setText(String.valueOf(alumno.getEdad()));

        }

        @Override
        public void onClick(View view) {
            if (listener!= null){
                listener.onAlumnoSeleccionado(getAdapterPosition());
            }

        }


    }


}
