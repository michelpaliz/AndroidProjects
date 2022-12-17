package com.germangascon.navigationdrawersample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.germangascon.navigationdrawersample.Interfaz.IOnCorreoSeleccionado;
import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;
import com.germangascon.navigationdrawersample.Modelo.EmailParser;
import com.germangascon.navigationdrawersample.Vista.Adaptadores.AdaptadorEmail;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoDetalle;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoListado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IOnCorreoSeleccionado, FragmentoListado.IOnAttachListener, FragmentoDetalle.IOnAttachListener {

    private static final String ACCOUNT_KEY = "com.germangascon.correoelectronico.account";
    private static final String LISTING_TYPE_KEY = "com.germangascon.correoelectronico.listingtype";
    private static final String SELECTED_EMAIL_KEY = "com.germangascon.correoelectronico.selectedemail";

    private Cuenta cuenta;
    private FragmentoListado.TipoFragmento tipoFragmento;
    private FragmentoListado fragmentoListado;
    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawer;
    private Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            email = (Email) savedInstanceState.getSerializable(SELECTED_EMAIL_KEY);
            cuenta = (Cuenta) savedInstanceState.getSerializable(ACCOUNT_KEY);
            tipoFragmento = (FragmentoListado.TipoFragmento) savedInstanceState.getSerializable(LISTING_TYPE_KEY);
        }

        cargarDatos();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        //Floating
//        floatingActionButton = findViewById(R.id.floatButton);
//        floatingActionButton.setOnClickListener(v -> {
//                Bundle bundle = new Bundle();
//                NewEmailFragment f = new NewEmailFragment();
//                bundle.putSerializable(NewEmailFragment.ACCOUNT_EXTRA, account);
//                f.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().
//                        replace(R.id.content_frame, f).
//                        addToBackStack(null).commit();
//                fab.hide();
//        });

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer + Toggle
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(ACCOUNT_KEY, cuenta);
        outState.putSerializable(SELECTED_EMAIL_KEY, email);
        super.onSaveInstanceState(outState);
    }

    public void cargarDatos() {
        EmailParser emailParser = new EmailParser(this);
        if (emailParser.startParser()) {
            cuenta = emailParser.getCuenta();
            tipoFragmento = FragmentoListado.TipoFragmento.RECEIVED;
            fragmentoListado = (FragmentoListado) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        } else {
            throw new NullPointerException();
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

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            int visibilidad = floatingActionButton.getVisibility();
            if (visibilidad == View.GONE) {
                floatingActionButton.show();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Se ha hecho click en algún item del NavigationView
        int id = item.getItemId();
        String msg = "";
        if (id == R.id.navRecibidos) {
            tipoFragmento = FragmentoListado.TipoFragmento.RECEIVED;
            msg = getString(R.string.received);
        } else if (id == R.id.navEnviados) {
            tipoFragmento = FragmentoListado.TipoFragmento.SENT;
            msg = getString(R.string.sent);
        } else if (id == R.id.navNoLeidos) {
            tipoFragmento = FragmentoListado.TipoFragmento.UNREADED;
            msg = getString(R.string.unread);
        } else if (id == R.id.navSpam) {
            tipoFragmento = FragmentoListado.TipoFragmento.SPAM;
            msg = getString(R.string.spam);
        } else if (id == R.id.navPapelera) {
            tipoFragmento = FragmentoListado.TipoFragmento.BIN;
            msg = getString(R.string.bin);
        }

        if (tipoFragmento == null) {
            throw new NullPointerException();
        }


        fragmentoListado = new FragmentoListado();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentoListado).commit();
        fragmentoListado.actualizarLista(tipoFragmento);
        setTitle(msg);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    FRAGMENTO LISTADO

    @Override
    public Cuenta getCuenta() {
        if (cuenta == null) {
            cargarDatos();
        }
        return cuenta;
    }

    @Override
    public FragmentoListado.TipoFragmento getTipoFragmento() {
        return tipoFragmento;
    }

    //    FRAGMENTO DETALLE

    @Override
    public Email getEmail() {
//        email = AdaptadorEmail.email;
//        if (email == null){
//            throw new NullPointerException();
//        }
//        return AdaptadorEmail.email;
        return  cuenta.getCorreos().get(AdaptadorEmail.email);
    }

    @Override
    public Contacto getContactFromEmail(Email email) {
        String contactStr;
        if(email.getCorreoOrigen().equals(cuenta.getEmail())) {
            contactStr = email.getCorreoDestino();
        } else {
            contactStr = email.getCorreoOrigen();
        }
        return cuenta.getContact(contactStr);
    }


    @Override
    public void onCorreoSeleccionado(int email) {
        if (cuenta == null) {
            cargarDatos();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new FragmentoDetalle()).addToBackStack(null).commit();
    }
}
