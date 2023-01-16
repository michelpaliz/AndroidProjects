package com.germangascon.navigationdrawersample.Vista.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.R;

import java.util.List;

public class FragmentoNuevoMensaje extends Fragment {

    public static final String ACCOUNT_EXTRA = "nuevo_mensaje";
    private AutoCompleteTextView autoCompleteEmailDestino;
    private String[] autoCompletadoContactos;
    private EditText etTema;
    private EditText etTexto;
    private Cuenta cuenta;
    private List<String> contactos;

    public FragmentoNuevoMensaje() {
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(ACCOUNT_EXTRA)) {
                cuenta = (Cuenta) bundle.getSerializable(ACCOUNT_EXTRA);
                assert cuenta != null;
                List<Contacto> contactos = cuenta.getContactoList();
                autoCompletadoContactos = new String[contactos.size()];
                for (int i = 0; i < contactos.size(); i++) {
                    autoCompletadoContactos[i] = contactos.get(i).getEmail();
                }

            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragmento_envio, container, false);
        TextView tvFrom = view.findViewById(R.id.tvFromNew);
        tvFrom.setText(cuenta.getEmail());
        autoCompleteEmailDestino = view.findViewById(R.id.etToNew);
        if (autoCompletadoContactos != null) {
            ArrayAdapter<String> adapatador = new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, autoCompletadoContactos);
            autoCompleteEmailDestino.setThreshold(1);
            autoCompleteEmailDestino.setAdapter(adapatador);
        }
        etTema = view.findViewById(R.id.etSubjectNew);
        etTexto = view.findViewById(R.id.etBodyNew);
        Button btnSubmit = view.findViewById(R.id.bSendNew);

        btnSubmit.setOnClickListener(v ->
                {
                    if (validar()) {
                        Toast.makeText(this.getContext(), "Correo Enviado correctamente", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    }
                }
        );

        return view;
    }


    public static boolean isValidEmail(CharSequence charSequence) {
        return !TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }


    public boolean validar() {
        if (!isValidEmail(autoCompleteEmailDestino.getText())) {
            Toast.makeText(getContext(), autoCompleteEmailDestino.getText().toString() + " no es un email valido ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etTema.getText().toString().length() == 0) {
            Toast.makeText(getContext(), " Indique el asunto ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etTexto.getText().toString().length() == 0) {
            Toast.makeText(getContext(), " Indique el texto ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}








