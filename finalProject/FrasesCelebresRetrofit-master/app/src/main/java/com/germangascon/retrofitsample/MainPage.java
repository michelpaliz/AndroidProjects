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


public class MainPage extends AppCompatActivity implements
        IListenerList, FragmentList.IOnAttachListener, FragmentDetail.IOnAttachListener  {

    private IAPIService apiService;
    private final List<Frase> frases;
    private final List<Autor> autores;
    private final List<Categoria> categorias;
    private final FragmentoConsultas fragmentoConsultas;
    private final FragmentAdminFunctions fragmentAdminFunctions;
    private Object detailData;



    public MainPage() {
        frases = new ArrayList<>();
        autores = new ArrayList<>();
        categorias = new ArrayList<>();
        fragmentoConsultas = new FragmentoConsultas();
        fragmentAdminFunctions = new FragmentAdminFunctions();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        init();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String value = extras.getString("key");
            if ("admin".equalsIgnoreCase(value)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frgPrincipal, fragmentAdminFunctions, getSupportFragmentManager().getClass().getSimpleName()).addToBackStack(null).commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frgPrincipal, fragmentoConsultas, getSupportFragmentManager().getClass().getSimpleName()).addToBackStack(null).commit();
        }

    }



    public void init() {
        apiService = RestClient.getInstance();
        getFraseList();
        getAutoresList();
        getCategoriasList();
    }

    private void getFraseList() {
        Call<List<Frase>> fraseCall = apiService.getFrases();

        fraseCall.enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                assert response.body() != null;
                frases.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {
                Toast.makeText(MainPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getAutoresList() {
        Call<List<Autor>> autoresCall = apiService.getAutores();

        autoresCall.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                assert response.body() != null;
                autores.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {
                Toast.makeText(MainPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getCategoriasList() {
        Call<List<Categoria>> categoriaCall = apiService.getCategorias();

        categoriaCall.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                assert response.body() != null;
                categorias.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(MainPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public Object[] getDataFrases() {
        if (frases == null) {
            throw new NullPointerException();
        }
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
    public Object getDataForDetail() {
        return detailData;
    }

    @Override
    public List<Frase> getFrases() {
        return frases;
    }

    @Override
    public void onItemSelected(int position) {
        int option = fragmentoConsultas.getOpcion();
        Bundle args = new Bundle();
        FragmentDetail fragmentDetail = new FragmentDetail();
        if (option == 1) {
            args.putInt("option", 1);
            fragmentDetail.setArguments(args);
            detailData = autores.get(position);
            System.out.println("esto es cogido " + detailData);
            System.out.println("esto es frases" + frases);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frgPrincipal, fragmentDetail, getSupportFragmentManager().getClass().
                            getSimpleName()).addToBackStack(null).commit();
        } else if (option == 2) {
            args.putInt("option", 2);
            fragmentDetail.setArguments(args);
            detailData = categorias.get(position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frgPrincipal, fragmentDetail, getSupportFragmentManager().getClass().
                            getSimpleName()).addToBackStack(null).commit();
        }

    }

}
