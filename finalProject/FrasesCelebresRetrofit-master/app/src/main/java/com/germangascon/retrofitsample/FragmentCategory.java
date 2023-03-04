package com.germangascon.retrofitsample;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.germangascon.retrofitsample.interfaces.IAPIService;
import com.germangascon.retrofitsample.models.Categoria;
import com.germangascon.retrofitsample.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCategory extends Fragment {


    private TextView tvModify;
    private EditText edNombre;
    private Button btnAdd, btnModify, btnBack;
    private IAPIService iapiService;
    private String nombre;
    private List<Categoria> categorias;
    private Categoria categoria;
    private int option;

    public interface IOnAttachListener {
        List<Categoria> getDataCategoriasAdd();
        Categoria getCategoriaSeleccionada();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        categorias = iOnAttachListener.getDataCategoriasAdd();
        categoria = iOnAttachListener.getCategoriaSeleccionada();
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
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edNombre = view.findViewById(R.id.edNewCategoryName);
        btnAdd = view.findViewById(R.id.btnRegisterNewCategory);
        btnModify = view.findViewById(R.id.btnModifyCategory);
        iapiService = RestClient.getInstance();
        tvModify = view.findViewById(R.id.tvTitulo_addCategory);
        btnBack = view.findViewById(R.id.btnBack_category);

        goingBack();

        btnModify.setVisibility(View.INVISIBLE);
        if (option == 1){
            btnAdd.setVisibility(View.INVISIBLE);
            btnModify.setVisibility(View.VISIBLE);
            modifyCategory();
        }
        registerNewCategory();
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


    public void modifyCategory(){

        tvModify.setText("Modify Category");

        if (categoria == null){
            throw new NullPointerException();
        }

        btnModify.setOnClickListener(v -> {
            if (!validateName()){
                return;
            }

            categoria.setNombre(nombre);
            iapiService.updateCategoria(categoria).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Toast.makeText(getContext(), "Category modified successfully", Toast.LENGTH_SHORT).show();
                    edNombre.setText(null);
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, Category modified unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }






    public void registerNewCategory(){

        btnAdd.setOnClickListener(v -> {
            Categoria categoria;
            if (!validateName()){
                return;
            }
            categoria = new Categoria(nombre);
            iapiService.addCategoria(categoria).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                        edNombre.setText(null);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, Category added unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });


        });


    }

    public boolean comprobarCategoriaExistente(String nombre) {
        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombre)) {
                return false;
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
        } else if (!comprobarCategoriaExistente(strFirstName)) {
            edNombre.setError("The category already exits");
            return false;
        } else {
            edNombre.setError(null);
            nombre = edNombre.getText().toString();
            return true;
        }
    }



}