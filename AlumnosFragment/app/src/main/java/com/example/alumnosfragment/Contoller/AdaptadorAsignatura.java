package com.example.alumnosfragment.Contoller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alumnosfragment.Interface.IAlumno;
import com.example.alumnosfragment.Model.Asignatura;
import com.example.alumnosfragment.R;

import java.util.List;

public class AdaptadorAsignatura extends RecyclerView.Adapter<AdaptadorAsignatura.HolderAlumno> {

    private final List<Asignatura> asignaturas;
    private final IAlumno listener;

    public AdaptadorAsignatura(List<Asignatura> asignaturas, IAlumno listener) {
        this.asignaturas = asignaturas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderAlumno onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notas_recyclerview,parent, false);
        return new HolderAlumno(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAlumno holder, int position) {
        Asignatura asignatura = asignaturas.get(position);
        holder.bindContacto(asignatura);
    }

    @Override
    public int getItemCount() {
        return asignaturas.size();
    }

    public class HolderAlumno extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView tvCodigo;
        private final TextView tvNombre;
        private final IAlumno listener;


        public HolderAlumno(@NonNull View itemView, IAlumno listener) {
            super(itemView);
            this.listener = listener;
            this.tvCodigo = itemView.findViewById(R.id.tvCodAsig);
            this.tvNombre = itemView.findViewById(R.id.tvNombreAsignatura);
            //IMPORTANTISIMO SI NO PONEMOS ESTA LINEA NUESTRA INTERFAZ DE ONCLICK NO FUNCIONARA!!
            itemView.setOnClickListener(this);
        }


        public void bindContacto(Asignatura asignatura){
            tvCodigo.setText(asignatura.getCodigoAsignatura());
            tvNombre.setText(asignatura.getNombreAsignatura());
        }


        @Override
        public void onClick(View view) {
            if (listener!= null){
                listener.onAlumnoSeleccionado(getAdapterPosition());
            }

        }


    }


}
