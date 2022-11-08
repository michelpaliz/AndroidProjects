package com.example.examplefragment.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.examplefragment.Interface.IContactoListener;
import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private final List<Contacto> contactos;
    private final IContactoListener listener;
//    private LayoutInflater mInflater;

    public ContactoAdapter(List<Contacto> contactos, IContactoListener listener ) {
      this.contactos = contactos;
      this.listener = listener;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lista_recyclerview,parent,false);
        return new ContactoViewHolder(itemView,listener);
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
        private final TextView tvTelefono1;
        private final IContactoListener listener;
        private final StringBuilder sb;


        public ContactoViewHolder(View itemView, IContactoListener listener) {
            super(itemView);
            this.tvNombre = itemView.findViewById(R.id.tvNombrePhone);
            this.tvTelefono1 = itemView.findViewById(R.id.tvNumeroTelefonoPhone);
            this.listener = listener;
            this.sb = new StringBuilder();
            itemView.setOnClickListener(this);
        }


        public void bindContacto(Contacto contacto){
            sb.setLength(0);
            sb.append(contacto.getNombre()).append(" ").append(contacto.getApellido1()).append(" ").append(contacto.getApellido2());
            tvNombre.setText(sb.toString());
            tvTelefono1.setText(contacto.getTelefono1());
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
//                getAdapterPosition()
//                listener.onContactoSeleccionado(contactos.get(getAdapterPosition()));
//                listener.onContactoSeleccionado(getAdapterPosition());
                listener.onContactoSeleccionado(getBindingAdapterPosition());
            }
        }
    }

}
