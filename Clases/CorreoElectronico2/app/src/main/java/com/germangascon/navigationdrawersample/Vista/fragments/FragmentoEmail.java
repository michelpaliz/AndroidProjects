package com.germangascon.navigationdrawersample.Vista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.germangascon.navigationdrawersample.Interfaz.IOnCorreoSeleccionado;
import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;
import com.germangascon.navigationdrawersample.R;
import com.germangascon.navigationdrawersample.Vista.Adaptadores.AdaptadorEmail;
import com.germangascon.navigationdrawersample.Vista.Logica.CorreoLogica;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class FragmentoEmail extends Fragment {

    public enum TipoFragmento {
        RECEIVED, SENT, UNREADED, DELETED, SPAM
    }

    private TipoFragmento tipoFragmento;
    private Cuenta cuenta;
    private IOnCorreoSeleccionado iOnCorreoSeleccionado;


    public static final String ACCOUNT = "com.germangascon.correoelectronico.account";
    public static final String TYPE = "com.germanascon.correoelectronico.type";




    public interface IOnAttachListener{
        Cuenta getCuenta();
    }

    public FragmentoEmail() {
        super(R.layout.recycler_view);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle !=  null){
            if (bundle.containsKey(ACCOUNT)){
                cuenta = (Cuenta) bundle.getSerializable(ACCOUNT);
            }
            if (bundle.containsKey(TYPE)){
                tipoFragmento = (TipoFragmento) bundle.getSerializable(TYPE);
            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        AdaptadorEmail adaptadorEmail = new AdaptadorEmail(getContext(),cuenta, tipoFragmento);
        CorreoLogica correoLogica = new CorreoLogica(cuenta);
        AtomicReference<HashMap<Email, Contacto>> listaGeneral = null;
        AtomicReference<HashMap<Email, Email>> listaSpam = null;
        adaptadorEmail.setOnClickListener(v -> {
            if (iOnCorreoSeleccionado != null) {
                switch (tipoFragmento) {
                    case RECEIVED:
                        assert false;
                        listaGeneral.set(correoLogica.getCorreosRecibidos());
                        break;
                    case SENT:
                        assert false;
                        listaGeneral.set(correoLogica.getCorreosEnviados());
                        break;
                    case UNREADED:
                        assert false;
                        listaGeneral.set(correoLogica.getCorreosNoLeidos());
                        break;
                    case SPAM:
                        assert false;
                        listaSpam.set(correoLogica.getCorreosSpam());
                        break;
                    case DELETED:
                        assert false;
                        listaGeneral.set(correoLogica.getCorreosEliminados());
                        break;
                }
                iOnCorreoSeleccionado.onCorreoSeleccionado(emails[rvList.getChildAdapterPosition(view)]);
            }

        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iOnCorreoSeleccionado = (IOnCorreoSeleccionado) context;
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        cuenta = iOnAttachListener.getCuenta();
    }
}
