package com.germangascon.retrofitsample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.germangascon.retrofitsample.interfaces.IAPIService;
import com.germangascon.retrofitsample.interfaces.IListenerList;
import com.germangascon.retrofitsample.models.Autor;
import com.germangascon.retrofitsample.models.Categoria;
import com.germangascon.retrofitsample.models.Frase;
import com.germangascon.retrofitsample.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin extends AppCompatActivity implements FragmentAuthor.IOnAttachListener,
        FragmentCategory.IOnAttachListener, FragmentPhrase.IOnAttachListener, FragmentList.IOnAttachListener, IListenerList
 {

    private int option;
    private IAPIService iapiService;
    private List<Autor> autores;
    private List<Categoria> categorias;
    private List<Frase> frases;
    private Bundle bundle;
    private FragmentoConsultas fragmentoConsultas;
    private Autor autor;
    private Categoria categoria;
    private Frase frase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FragmentAuthor fragmentAuthor = new FragmentAuthor();
        FragmentCategory fragmentCategory = new FragmentCategory();
        FragmentPhrase fragmentPhrase = new FragmentPhrase();
        fragmentoConsultas = new FragmentoConsultas();

        autores = new ArrayList<>();
        categorias = new ArrayList<>();
        frases = new ArrayList<>();
        iapiService = RestClient.getInstance();
        bundle = new Bundle();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            option = extras.getInt("option");
        }

        getAutoresList();
        getCategorias();
        getFraseList();

        switch (option) {
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, fragmentAuthor,
                                getSupportFragmentManager().getClass().getSimpleName()).
                        addToBackStack(null).commit();

                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, fragmentCategory,
                                getSupportFragmentManager().getClass().getSimpleName()).
                        addToBackStack(null).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, fragmentPhrase,
                                getSupportFragmentManager().getClass().getSimpleName()).
                        addToBackStack(null).commit();
                break;

            case 4:
                bundle.putInt("option", 0);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, fragmentoConsultas,
                                getSupportFragmentManager().getClass().getSimpleName()).
                        addToBackStack(null).commit();
                break;

        }

    }


    private void getAutoresList() {
        Call<List<Autor>> autoresCall = iapiService.getAutores();

        autoresCall.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                assert response.body() != null;
                autores.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {
                Toast.makeText(Admin.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getCategorias() {
        iapiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                assert response.body() != null;
                categorias.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(Admin.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getFraseList() {
        Call<List<Frase>> fraseCall = iapiService.getFrases();

        fraseCall.enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                assert response.body() != null;
                frases.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {
                Toast.makeText(Admin.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public List<Autor> getDataAutoresAdd() {
        return autores;
    }

    @Override
    public List<Categoria> getDataCategoriasAdd() {
        return categorias;
    }

     @Override
    public List<Autor> getDataAutoresPhrase() {
        return autores;
    }

    @Override
    public List<Categoria> getDataCategoriasPhrase() {
        return categorias;
    }

     @Override
     public Frase getFrase() {
         return frase;
     }

     @Override
    public Object[] getDataFrases() {
        return frases.toArray();
    }

    @Override
    public Object[] getDataAutores() {
        return autores.toArray();
    }

    @Override
    public Object[] getDataCategoria() {
        return categorias.toArray();
    }

     @Override
     public Autor getAutorSeleccionado() {
         return autor ;
     }

     @Override
     public Categoria getCategoriaSeleccionada() {
         return categoria;
     }

    @Override
    public void onItemSelected(int position) {
        option = fragmentoConsultas.getOpcion();
        Bundle args = new Bundle();

        if (option == 1) {
            FragmentAuthor fragmentAuthor = new FragmentAuthor();
            args.putInt("option", 1);
            fragmentAuthor.setArguments(args);
            autor = autores.get(position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, fragmentAuthor, getSupportFragmentManager().getClass().
                            getSimpleName()).addToBackStack(null).commit();
        } else if (option == 2) {
            FragmentCategory fragmentCategory = new FragmentCategory();
            args.putInt("option", 1);
            fragmentCategory.setArguments(args);
            categoria = categorias.get(position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, fragmentCategory, getSupportFragmentManager().getClass().
                            getSimpleName()).addToBackStack(null).commit();
        } else if (option == 3) {
            FragmentPhrase fragmentPhrase = new FragmentPhrase();
            args.putInt("option", 1);
            fragmentPhrase.setArguments(args);
            frase = frases.get(position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, fragmentPhrase, getSupportFragmentManager().getClass().
                            getSimpleName()).addToBackStack(null).commit();
        }
    }
}
