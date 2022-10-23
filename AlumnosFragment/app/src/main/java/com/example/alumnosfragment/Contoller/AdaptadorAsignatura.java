package com.example.alumnosfragment.Contoller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alumnosfragment.Model.Asignatura;
import com.example.alumnosfragment.Model.Nota;
import com.example.alumnosfragment.R;

import java.util.List;

public class AdaptadorAsignatura extends RecyclerView.Adapter<AdaptadorAsignatura.HolderAlumno> {

    private static List<Nota> notas = null;


    public AdaptadorAsignatura( List<Nota>notas) {
        AdaptadorAsignatura.notas = notas;
    }

    @NonNull
    @Override
    public HolderAlumno onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notas_recyclerview,parent, false);
        return new AdaptadorAsignatura.HolderAlumno(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAlumno holder, int position) {
        Nota nota = notas.get(position);
//        asignaturas = HolderAlumno.parsearAsignatura(HolderAlumno.tvNota.getContext());
//        Asignatura asignatura = asignaturas.get(position);
        holder.bindContacto(nota);
    }

    @Override
    public int getItemCount() {
        if (notas == null){
            return  0;
        }else{
            return notas.size();
        }
    }



    public static class HolderAlumno extends RecyclerView.ViewHolder {

        private final TextView tvCodigo;
        private final TextView tvNombre;
        private TextView tvNota ;
        private List <Asignatura> asignaturas;


        public HolderAlumno(@NonNull View itemView) {
            super(itemView);
            this.tvCodigo = itemView.findViewById(R.id.tvCodAsig);
            this.tvNombre = itemView.findViewById(R.id.tvNombreAsignatura);
            tvNota = itemView.findViewById(R.id.tvCalifacion);
        }


        //RELLENAMOS NUESTRO VIEW
        public void bindContacto(Nota nota){
            asignaturas = parsearAsignatura(tvNota.getContext());
            tvCodigo.setText(nota.getCodAsig());
            tvNota.setText(nota.getCalificacion());
            if (asignaturas == null){
                Log.d("esnull","es null");
            }

//            Log.d("notas",String.valueOf(AdaptadorAsignatura.notas.size()));
            Log.d("notas",(notas.toString()));
            Log.d("asignaturas",(this.asignaturas.toString()));
            Log.d("notas",(asignaturas.toString()));
            Log.d("notassize",(String.valueOf(notas.size())));
            Log.d("asignaturassize",(String.valueOf(asignaturas.size())));
            for (int i = 0; i < asignaturas.size(); i++) {
                if (asignaturas.get(i).getCodigoAsignatura().equalsIgnoreCase(nota.getCodAsig())) {
                    tvNombre.setText(asignaturas.get(i).getNombreAsignatura());
                }
            }
        }

        //COJEMOS LA LISTA ASIGNATURA JSON PARA PARSEAR NUESTRA LISTA ASIGNATURAS
        private static List<Asignatura> parsearAsignatura(Context context){
            List<Asignatura> asignaturas;
            ParserAsignatura parserAsignaturas= new ParserAsignatura(context);
            parserAsignaturas.startParser();
            asignaturas = parserAsignaturas.getAsignaturas();
            return asignaturas;
        }

        @Override
        public String toString() {
            return "HolderAlumno{" +
                    "tvCodigo=" + tvCodigo +
                    ", tvNombre=" + tvNombre +
                    ", tvNota=" + tvNota +
                    ", asignaturas=" + asignaturas +
                    '}';
        }
    }
}



