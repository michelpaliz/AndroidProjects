package com.example.perfil_usuario.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.perfil_usuario.Modelo.ConfigurarUsuario;
import com.example.perfil_usuario.Modelo.Usuario;
import com.example.perfil_usuario.R;

public class FragmentoSesion extends Fragment {

    private Usuario usuario;

    public interface IOnAttachlistener{
        Usuario getUsuario();
    }

    private TextView tvInformacion;
    private TextView tvComunicacion;
    private EditText etContrasenyaActual;
    private EditText etContrasenyaNueva;
    private EditText etConstrasenyaConfirmacion;
    private Button btnCambiarContrasenya;
    private Button btnGuardarCambios;

    public FragmentoSesion() {
        super(R.layout.framgento_sesion);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvInformacion = view.findViewById(R.id.tvInformacion);
        etContrasenyaActual= view.findViewById(R.id.etContrasenyaActual);
        etConstrasenyaConfirmacion = view.findViewById(R.id.etRepetirContrasenya);
        etContrasenyaNueva = view.findViewById(R.id.etNuevaConstrasenya);
        btnCambiarContrasenya = view.findViewById(R.id.btnCambiarContrasenya);
        btnGuardarCambios = view.findViewById(R.id.btnConfirmar);
        tvComunicacion = view.findViewById(R.id.tvComunicacion);
        if (usuario != null){
            cargarDatos(usuario);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachlistener iOnAttachlistener = (IOnAttachlistener) context;
        usuario = iOnAttachlistener.getUsuario();

    }

    public void cargarDatos(Usuario usuario){
        ConfigurarUsuario configurarUsuario = new ConfigurarUsuario(usuario);
        tvInformacion.setText("NOMBRE DEL USUARIO " + usuario.getNombre().toUpperCase());
        btnCambiarContrasenya.setOnClickListener(view -> {
            etContrasenyaActual.setVisibility(View.VISIBLE);
            etContrasenyaNueva.setVisibility(View.VISIBLE);
            etConstrasenyaConfirmacion.setVisibility(View.VISIBLE);
            if (configurarUsuario.cambioContrasenya(usuario.getContrasenya())){
                tvComunicacion.setVisibility(View.VISIBLE);
                btnGuardarCambios.setVisibility(View.VISIBLE);
                btnGuardarCambios.setOnClickListener(view1 -> {
                        configurarUsuario.setNuevaContrasenya(usuario.getContrasenya());
                     tvComunicacion.setText(configurarUsuario.getMensaje());
                });
            }else{
                tvComunicacion.setText(configurarUsuario.getMensaje());
            }

        });

    }





}
