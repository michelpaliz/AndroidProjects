package com.germangascon.retrofitsample;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.germangascon.retrofitsample.interfaces.IAPIService;
import com.germangascon.retrofitsample.models.Autor;
import com.germangascon.retrofitsample.rest.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAuthor extends Fragment {

    private IAPIService iapiService;
    private EditText edNombre, edNacimiento, edMuerte, edProfesion;
    private Button btnAddAuthor, btnBack, btnModify;
    private TextView tvTitulo;
    private String nombre, nacimiento, muerte, profesion;
    private List<Autor> autoresCogidos = new ArrayList<>();
    private Autor autorSeleccionado;
    private int option;


    public interface IOnAttachListener {
        List<Autor> getDataAutoresAdd();
        Autor getAutorSeleccionado();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        autoresCogidos = iOnAttachListener.getDataAutoresAdd();
        autorSeleccionado = iOnAttachListener.getAutorSeleccionado();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            option = bundle.getInt("option", -1);
        }
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edNombre = view.findViewById(R.id.edNewAuthorName);
        edNacimiento = view.findViewById(R.id.edNewAuthorNacimiento);
        edMuerte = view.findViewById(R.id.edNewAuthorMuerte);
        edProfesion = view.findViewById(R.id.edProfesion);
        btnAddAuthor = view.findViewById(R.id.btnRegisterNewAuthor);
        btnBack = view.findViewById(R.id.btnBack);
        btnModify = view.findViewById(R.id.btnModifyAuthor);
        iapiService = RestClient.getInstance();
        tvTitulo = view.findViewById(R.id.tvTitulo_Author);

        goingBack();

        //****** Add Author *****

        btnModify.setVisibility(View.INVISIBLE);

        System.out.println("ESTA OPCIOOOOOON ES " + option);
        if (option == 1){
            btnAddAuthor.setVisibility(View.INVISIBLE);
            btnModify.setVisibility(View.VISIBLE);
            modifyAuthor();
        }

        registerNewAuthor();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_author, container, false);
    }


    public void goingBack(){
        btnBack.setOnClickListener(v -> {
            FragmentAdminFunctions nextFrag= new FragmentAdminFunctions();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        });
    }



    public void modifyAuthor(){
        tvTitulo.setText("Modify Author");
        if (autorSeleccionado == null){
            throw new NullPointerException();
        }

        btnModify.setOnClickListener( v -> {
            if (!validateName() || !validateNacimiento() || !validateMuerte() || !validateProfesion()) {
                return;
            }

            autorSeleccionado.setNombre(nombre);
            autorSeleccionado.setNacimiento(Integer.parseInt(nacimiento));
            autorSeleccionado.setMuerte(muerte);
            autorSeleccionado.setProfesion(profesion);


            iapiService.updateAutor(autorSeleccionado).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Toast.makeText(getContext(), "Author modified successfully", Toast.LENGTH_SHORT).show();
                    edNombre.setText(null);
                    edNacimiento.setText(null);
                    edMuerte.setText(null);
                    edProfesion.setText(null);
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, Author modified unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });

        });

    }



    public void registerNewAuthor() {

        if (autoresCogidos == null){
            throw new NullPointerException();
        }

        btnAddAuthor.setOnClickListener(v -> {

            if (!validateName() || !validateNacimiento() || !validateMuerte() || !validateProfesion()) {
                return;
            }

            Autor nuevoAutor = new Autor(nombre, Integer.parseInt(nacimiento), muerte, profesion);
            iapiService.addAutor(nuevoAutor).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Author added successfully", Toast.LENGTH_SHORT).show();
                        edNombre.setText(null);
                        edNacimiento.setText(null);
                        edMuerte.setText(null);
                        edProfesion.setText(null);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, Author added unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });


        });



    }


    public boolean comprobarAutorExistente(String nombre) {
        for (Autor autor : autoresCogidos) {
            if (autor.getNombre().equalsIgnoreCase(nombre)) {
                return  false;
            }
        }
        return true;

    }


    // ********** VALIDACIONES *********

    public boolean validateName() {
        String strFirstName = edNombre.getText().toString();
        if (strFirstName.isEmpty()) {
            edNombre.setError("Cannot be empty");
            return false;
        } else if (!comprobarAutorExistente(strFirstName)) {
            edNombre.setError("The author already exits");
            return false;
        } else {
            edNombre.setError(null);
            nombre = edNombre.getText().toString();
            return true;
        }
    }

    public boolean validateProfesion() {
        String strProfesion = edProfesion.getText().toString();
        if (strProfesion.isEmpty()) {
            edProfesion.setError("Cannot be empty");
            return false;
        } else {
            edProfesion.setError(null);
            profesion = edProfesion.getText().toString();
            return true;
        }

    }

    public boolean validateMuerte() {
        String strMuerte = edMuerte.getText().toString();
        boolean containsOnlyDigits = checkDigits(strMuerte);

        if (strMuerte.isEmpty()) {
            edMuerte.setError("Cannot be empty");
            return false;
        } else if (containsOnlyDigits) {
            edMuerte.setError("Introduce only digits");
            return false;
        } else {
            edMuerte.setError(null);
            muerte = edMuerte.getText().toString();
            return true;
        }
    }

    public boolean validateNacimiento() {
        String strNacimiento = edNacimiento.getText().toString();
        boolean containsOnlyDigits = checkDigits(strNacimiento);

        if (strNacimiento.isEmpty()) {
            edNacimiento.setError("Cannot be empty");
            return false;
        } else if (containsOnlyDigits) {
            edNacimiento.setError("Introduce only digits");
            return false;
        } else {
            edNacimiento.setError(null);
            nacimiento = edNacimiento.getText().toString();
            return true;
        }
    }

    public boolean checkDigits(String text) {
        boolean containsOnlyDigits = false;
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) { // in case that a char is NOT a digit, enter to the if code block
                containsOnlyDigits = true;
                break; // break the loop, since we found that this char is not a digit
            }
        }
        return containsOnlyDigits;
    }



}