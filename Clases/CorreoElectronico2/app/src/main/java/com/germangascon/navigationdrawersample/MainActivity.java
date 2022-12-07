package com.germangascon.navigationdrawersample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.germangascon.navigationdrawersample.Interfaz.IOnCorreoSeleccionado;
import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;
import com.germangascon.navigationdrawersample.Modelo.EmailParser;
import com.germangascon.navigationdrawersample.Modelo.ModeloContacto;
import com.germangascon.navigationdrawersample.Vista.Logica.CorreoLogica;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoEliminados;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoEnviados;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoEnvio;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoNoLeidos;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoRecibidos;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoSpam;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentoRecibidos.IOnAttachListener, FragmentoEnviados.IOnAttachListener, FragmentoSpam.IOnAttachListener, FragmentoNoLeidos.IOnAttachListener, IOnCorreoSeleccionado, FragmentoEliminados.IOnAttachListener {
    private DrawerLayout drawer;
    private ModeloContacto modeloContacto;

    private Cuenta cuenta;


    public MainActivity() {
        super(R.layout.activity_main);
    }

    public void cargarDatos() {
        EmailParser emailParser = new EmailParser(this);
        if (emailParser.startParser()) {
            cuenta = emailParser.getCuenta();
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void onCorreoSeleccionado(int posicion) {
        if (cuenta == null) {
            cargarDatos();
        }
    }


    @Override
    public Cuenta getCuenta() {
        if (cuenta == null) {
            cargarDatos();
        }
        return cuenta;
    }


    @Override
    public void onBackPressed() {
        /*
         * Si el usuario pulsa el botón atrás mientras está mostrándose el menú del NavigationView,
         * hacemos que se cierre dicho menú, ya que el comportamiento por defecto es cerrar la
         * Activity.
         */
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Cargamos el menú de la ActionBar
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se ha hecho click en algún item del menú de la ActionBar
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void testing() {
        CorreoLogica correoLogica = new CorreoLogica(cuenta);
        HashMap<Email, Contacto> hashMap1 = correoLogica.getCorreosRecibidos();
        Stream<Map.Entry<Email, Contacto>> stream1 = hashMap1.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));

        List<Map.Entry<Email, Contacto>> test1 = stream1.collect(Collectors.toList());


        HashMap<Email, Email> hashMap2 = correoLogica.getCorreosSpam();
        Stream<Map.Entry<Email, Email>> stream2 = hashMap2.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getKey().getFecha().compareTo(o1.getKey().getFecha()));
        List<Map.Entry<Email, Email>> test2 = stream2.collect(Collectors.toList());


        test1.forEach( t -> System.out.println(t.getKey().getFecha() + " " + t.getValue().getDireccion()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Para pruebas de datos.
        cargarDatos();
        testing();

        //Floating
        FloatingActionButton floatingActionButton = findViewById(R.id.floatButton);
        floatingActionButton.setOnClickListener(v -> {
            Fragment fragment = new FragmentoEnvio();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            setTitle("Send new message");
        });


                Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Obtenemos una referencia al header del Navigation para poder modificarlo en tiempo de ejecución
        View headerView = navigationView.getHeaderView(0);
        ImageView ivUser = headerView.findViewById(R.id.ivProfile);
        TextView tvUser = headerView.findViewById(R.id.tvUser);
        tvUser.setText(R.string.nav_header_title);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);
        tvEmail.setText(R.string.nav_header_subtitle);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment f;
        // Se ha hecho click en algún item del NavigationView
        int id = item.getItemId();

        if (id == R.id.navRecibidos) {
            f = new FragmentoRecibidos();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            setTitle(R.string.received);
        } else if (id == R.id.navEnviados) {
            f = new FragmentoEnviados();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            setTitle("Sent messages");
        } else if (id == R.id.navNoLeidos) {
            f = new FragmentoNoLeidos();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            setTitle("Not read");
        } else if (id == R.id.navSpam) {
            f = new FragmentoSpam();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            setTitle("Spam");
        } else if (id == R.id.navPapelera) {
            f = new FragmentoEliminados();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            setTitle("Papelera");
        }else if (id == R.id.navEnviar) {
            f = new FragmentoEnvio();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            setTitle("Send new message");
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
