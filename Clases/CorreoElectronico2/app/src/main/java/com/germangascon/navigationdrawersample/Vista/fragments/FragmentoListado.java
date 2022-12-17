package com.germangascon.navigationdrawersample.Vista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.germangascon.navigationdrawersample.Interfaz.IOnCorreoSeleccionado;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.R;
import com.germangascon.navigationdrawersample.Vista.Adaptadores.AdaptadorEmail;

public class FragmentoListado extends Fragment {


    private AdaptadorEmail adaptadorEmail;
    private TipoFragmento tipoFragmento;
    private IOnCorreoSeleccionado iOnCorreoSeleccionado;
    private Cuenta cuenta;


    public enum TipoFragmento {
        RECEIVED, SENT, UNREADED, SPAM, BIN
    }

    public interface IOnAttachListener {
        Cuenta getCuenta();
        TipoFragmento getTipoFragmento();
    }

    public FragmentoListado() {
        super(R.layout.recycler_view);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adaptadorEmail = new AdaptadorEmail(getContext(), cuenta, tipoFragmento,iOnCorreoSeleccionado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptadorEmail);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Para la cuenta
        iOnCorreoSeleccionado = (IOnCorreoSeleccionado) context;
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        cuenta = iOnAttachListener.getCuenta();
        //Para el tipo de fragmento
        tipoFragmento = iOnAttachListener.getTipoFragmento();
        actualizarLista(tipoFragmento);
    }

    /**
     * @param tipoFragmento el fragmento que vamos a cargar a nuestro layout de nuestro adaptador
     */
    public void actualizarLista(TipoFragmento tipoFragmento) {
        this.tipoFragmento = tipoFragmento;
        if (adaptadorEmail != null) {
            adaptadorEmail.actualizarLista(tipoFragmento);
        }
    }


}
