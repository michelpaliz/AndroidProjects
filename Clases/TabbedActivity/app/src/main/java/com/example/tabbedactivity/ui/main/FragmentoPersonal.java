package com.example.tabbedactivity.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tabbedactivity.R;

public class FragmentoPersonal extends Fragment {

    private String datos;

    public interface IOnAttachListener{
        String getDatosPersonales();
    }

    public FragmentoPersonal() {
        super(R.layout.fragmento_personal);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvDatosPersonal = view.findViewById(R.id.tvDatosPersonales);
        tvDatosPersonal.setText(datos);
    }

    /**
     *
     * @param context es la actividad donde este el fragmento.
     * el requisto es el que la actividad que implemente el framento tine que imp[lementar el Onattachlistener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Carga el contexto
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        datos = iOnAttachListener.getDatosPersonales();
    }
}
