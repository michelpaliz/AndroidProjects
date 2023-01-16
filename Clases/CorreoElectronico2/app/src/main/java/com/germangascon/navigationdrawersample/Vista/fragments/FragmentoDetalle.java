package com.germangascon.navigationdrawersample.Vista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.germangascon.navigationdrawersample.Interfaz.IOnCorreoSeleccionado;
import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;
import com.germangascon.navigationdrawersample.R;
import com.germangascon.navigationdrawersample.Vista.Adaptadores.AdaptadorEmail;

public class FragmentoDetalle extends Fragment {


    //    PARA CARGAR LOS DATOS UTILIZAMOS UNA INSTACIA DE TIPO CUENTA
    private Email email;
    private Contacto contacto;
    //    PARA EL VIEW
    private TextView tvTextoEmail;
    private TextView tvTemaEmail;
    private TextView tvOrigenEmail;
    private TextView tvDestinoEmail;
    private TextView tvFechaEnvioEmail;
    private TextView tvNombreContacto;


    public interface IOnAttachListener {
        Email getEmail();
        Contacto getContactFromEmail(Email email);
    }

    public FragmentoDetalle() {
        super(R.layout.fragmento_detalle);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNombreContacto = view.findViewById(R.id.tvNombreContacto);
        tvTextoEmail = view.findViewById(R.id.tvTextoEmail);
        tvTemaEmail = view.findViewById(R.id.tvTemaEnvio);
        tvOrigenEmail = view.findViewById(R.id.tvOrigenEmail);
        tvDestinoEmail = view.findViewById(R.id.tvDestinoEmail);
        tvFechaEnvioEmail = view.findViewById(R.id.tvFechaEnvio);

        if (email == null){
            throw new NullPointerException();
        }else{
            cargarDatos();
        }

    }


    public void cargarDatos() {

        if(contacto != null){
            tvNombreContacto.setText(contacto.getNombre());
            //Esta variable la reinicio a null para que vuelva a coger la anterior.
            contacto = null;
        }else {
            tvNombreContacto.setText("Unknow");
        }
        tvTextoEmail.setText(email.getTexto());
        tvDestinoEmail.setText(email.getCorreoDestino());
        tvTemaEmail.setText(email.getTema());
        tvOrigenEmail.setText(email.getCorreoOrigen());
        tvFechaEnvioEmail.setText(email.getFecha());
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Para la cuenta
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        email = iOnAttachListener.getEmail();
        contacto = iOnAttachListener.getContactFromEmail(email);
    }


}
