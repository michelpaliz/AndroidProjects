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
import com.germangascon.retrofitsample.models.Categoria;
import com.germangascon.retrofitsample.models.Frase;
import com.germangascon.retrofitsample.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddPhrase extends Fragment {

    private TextView tvTitulo;
    private EditText edTexto, edFecha, edAutor, edCategoria;
    private String texto, fecha, nombreAutor, nombreCategoria;
    private Button btnAdd, btnModify;
    private Autor autorRecogido;
    private Categoria categoriaRecogida;
    private IAPIService iapiService;
    private Frase frase;
    private int option;


    private List<Categoria> categorias;
    private List<Autor> autores;

    public FragmentAddPhrase() {
        categorias = new ArrayList<>();
        autores = new ArrayList<>();
    }

    public interface IOnAttachListener {
        List<Autor> getDataAutoresPhrase();
        List<Categoria> getDataCategoriasPhrase();
        Frase getFrase();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        autores = iOnAttachListener.getDataAutoresPhrase();
        categorias = iOnAttachListener.getDataCategoriasPhrase();
        frase = iOnAttachListener.getFrase();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_phrase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edTexto = view.findViewById(R.id.edNewText_Phrase);
        edFecha = view.findViewById(R.id.edNewDate_Phrase);
        edAutor = view.findViewById(R.id.edNewAuthor_Phrase);
        edCategoria = view.findViewById(R.id.edNewCategory_Phrase);
        btnAdd = view.findViewById(R.id.btnRegisterNewPhrase);
        btnModify = view.findViewById(R.id.btnModifyPhrase);
        tvTitulo = view.findViewById(R.id.tvTitulo_Phrase);
        iapiService = RestClient.getInstance();
        autorRecogido = new Autor();
        categoriaRecogida = new Categoria();

        btnModify.setVisibility(View.INVISIBLE);

        if (option == 1){
            btnModify.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            modifyPhrase();
        }

        addPhrase();
    }



    public void modifyPhrase(){

        tvTitulo.setText("Modify Phrase");
        btnModify.setOnClickListener(v -> {
            if (!validateTexto() || !validateFecha() || !validateNameAuthor() || !validateNameCategory()) {
                return;
            }

            frase.setTexto(texto);
            frase.setAutor(autorRecogido);
            frase.setCategoria(categoriaRecogida);

            iapiService.updateFrase(frase).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Toast.makeText(getContext(), "Phrase modified successfully", Toast.LENGTH_SHORT).show();
                    edTexto.setText(null);
                    edCategoria.setText(null);
                    edFecha.setText(null);
                    edAutor.setText(null);
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, Phrase modified unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }


    public void addPhrase() {
        btnAdd.setOnClickListener(v -> {
            Frase nuevaFrase;
            if (!validateTexto() || !validateFecha() || !validateNameAuthor() || !validateNameCategory()) {
                return;
            }

            nuevaFrase = new Frase(texto, fecha, autorRecogido, categoriaRecogida);
            iapiService.addFrase(nuevaFrase).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Phrase added successfully", Toast.LENGTH_SHORT).show();
                        edAutor.setText(null);
                        edCategoria.setText(null);
                        edTexto.setText(null);
                        edFecha.setText(null);
                    }

                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, Phrase" +
                            " added unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }


    // ****** VALIDATIONS  **********

    public boolean validateTexto() {
        String strFirstName = edTexto.getText().toString();
        if (strFirstName.isEmpty()) {
            edTexto.setError("Cannot be empty");
            return false;
        } else {
            edTexto.setError(null);
            texto = edTexto.getText().toString();
            return true;
        }
    }

    public boolean validateNameAuthor() {
        String strFirstName = edAutor.getText().toString();
        if (strFirstName.isEmpty()) {
            edAutor.setError("Cannot be empty");
            return false;
        } else if (!comprobarAutorExistente(strFirstName)) {
            edAutor.setError("The author does not exist");
            return false;
        } else {
            edAutor.setError(null);
            nombreAutor = edAutor.getText().toString();
            return true;
        }
    }

    public boolean validateNameCategory() {
        String strCategoryName = edCategoria.getText().toString();
        if (strCategoryName.isEmpty()) {
            edCategoria.setError("Cannot be empty");
            return false;
        } else if (!comprobarCategoriaExistente(strCategoryName)) {
            edCategoria.setError("The category does not exist");
            return false;
        } else {
            edAutor.setError(null);
            nombreCategoria = edCategoria.getText().toString();
            return true;
        }
    }


    public boolean validateFecha() {
        String strFechaProgramada = edFecha.getText().toString();
        boolean containsOnlyDigits = checkDigits(strFechaProgramada);

        if (strFechaProgramada.isEmpty()) {
            edFecha.setError("Cannot be empty");
            return false;
        } else if (containsOnlyDigits) {
            edFecha.setError("Introduce only digits");
            return false;
        } else {
            edFecha.setError(null);
            fecha = edFecha.getText().toString();
            return true;
        }
    }

//    ******** OTHER FUNCTIONS ************

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

    public boolean comprobarAutorExistente(String nombre) {
        for (Autor autor : autores) {
            if (autor.getNombre().equalsIgnoreCase(nombre)) {
                autorRecogido = autor;
                return true;
            }
        }
        return false;

    }

    public boolean comprobarCategoriaExistente(String nombre) {
        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombre)) {
                categoriaRecogida = categoria;
                return true;
            }
        }
        return false;

    }


}