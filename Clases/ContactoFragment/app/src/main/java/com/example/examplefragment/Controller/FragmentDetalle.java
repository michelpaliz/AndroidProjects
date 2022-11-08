package com.example.examplefragment.Controller;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;


public class FragmentDetalle extends Fragment {

    public static final String EXTRA_CONTACTO = "com.example.examplefragment.EXTRA_CONTACTO";
    private TextView tvNombre ;
    private TextView tvApellidos;
    private TextView tvTelefono1;
    private TextView tvTelefono2;
    private TextView tvEmpresa;
    private TextView tvDireccion;
    private TextView tvNacimiento;
    private Contacto contacto;
    private final StringBuilder sb;

    public interface IOnAttachListener{
        //Esta implementacion los coje desde el main que este nuestros contacto
        Contacto getContactoDetalle();
    }

    public FragmentDetalle(){
        super(R.layout.list_item_detalle_recyclerview);
        this.sb = new StringBuilder();
        this.contacto = null;
    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        if (getArguments()!= null){
//            contacto = (Contacto)getArguments().getSerializable(EXTRA_CONTACTO);
//        }
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNombre = view.findViewById(R.id.tvNombre);
        tvApellidos = view.findViewById(R.id.tvApellido);
        tvTelefono1 = view.findViewById(R.id.tvTelf1);
        tvTelefono2 = view.findViewById(R.id.tvTelf2);
        tvEmpresa = view.findViewById(R.id.tvEmpresa);
        tvDireccion = view.findViewById(R.id.tvDireccion);
        tvNacimiento = view.findViewById(R.id.tvNacimiento);
        if(contacto != null) {
            mostrarDetalle(contacto);
        }else{
            throw new NullPointerException();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        contacto = attachListener.getContactoDetalle();
    }

    //Este metodo hacemos los setters de nuestro view
    //Por ejemplo puede ir los RecyclerView y los TextViews.
    public void mostrarDetalle(Contacto contacto){
        sb.setLength(0);
        sb.append(contacto.getNombre()).append(" ").append(contacto.getApellido1()).append(" ").append(contacto.getApellido2());
        tvNombre.setText(contacto.getNombre());
        tvApellidos.setText(sb.toString());
        tvNacimiento.setText(contacto.getNacimiento());
        tvEmpresa.setText(contacto.getEmpresa());
        tvDireccion.setText(contacto.getDireccion());
        tvTelefono1.setText(contacto.getTelefono1());
        tvTelefono2.setText(contacto.getTelefono2());
    }




}
