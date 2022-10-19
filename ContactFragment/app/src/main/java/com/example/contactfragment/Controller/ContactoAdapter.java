package com.example.contactfragment.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactfragment.Interface.IContactoListener;
import com.example.contactfragment.Model.Contacto;
import com.example.contactfragment.R;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private Context context;
    private static List<Contacto> contactos;
    private IContactoListener listener;
    private LayoutInflater mInflater;

    public ContactoAdapter(@NonNull Context context, List<Contacto> contactos, IContactoListener listener) {
//        this.mInflater = LayoutInflater.from(context);
        this.context = context;
      ContactoAdapter.contactos = contactos;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = mInflater.inflate(R.layout.list_item_detalle_recyclerview, null);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_detalle_recyclerview,parent,false);
        return new ContactoAdapter.ContactoViewHolder(itemView,context,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        Contacto contacto = contactos.get(position);
        holder.bindContacto(contacto);
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }


    public static class ContactoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Para el item del recycler view
        private final TextView tvNombre;
        private final TextView tvApellidos;
        private final TextView tvNacimiento;
        private final TextView tvDireccion;
        private final TextView tvEmpresa;
        private final TextView tvTelefono1;
        private final TextView tvTelefono2;
        //Para el constructor
        private final Context context;
        private final IContactoListener listener;


        public ContactoViewHolder(View itemView, Context context, IContactoListener listener) {
            super(itemView);
            this.tvNombre = itemView.findViewById(R.id.tvNombre);
            this.tvApellidos = itemView.findViewById(R.id.tvApellido);
            this.tvNacimiento = itemView.findViewById(R.id.tvNacimiento);
            this.tvDireccion = itemView.findViewById(R.id.tvDireccion);
            this.tvEmpresa = itemView.findViewById(R.id.tvEmpresa);
            this.tvTelefono1 = itemView.findViewById(R.id.tvTelf1);
            this.tvTelefono2 = itemView.findViewById(R.id.tvTelf2);
            this.context = context;
            this.listener = listener;
            //
            itemView.setOnClickListener(this);
        }


        public void bindContacto(Contacto contacto){
            tvNombre.setText(contacto.getNombre());
            tvApellidos.setText(contacto.getApellidos());
            tvNacimiento.setText(contacto.getNacimiento());
            tvDireccion.setText(contacto.getDireccion());
            tvTelefono1.setText(contacto.getTelefono1());
            tvTelefono2.setText(contacto.getTelefono2());
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.onContactoSeleccionado(contactos.get(getBindingAdapterPosition()));
            }
        }
    }

}
