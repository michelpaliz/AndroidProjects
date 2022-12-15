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
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;
import com.germangascon.navigationdrawersample.Modelo.EmailParser;
import com.germangascon.navigationdrawersample.Modelo.ModeloContacto;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoEmail;
import com.germangascon.navigationdrawersample.Vista.fragments.FragmentoEnviados;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentoEnviados.IOnAttachListener, IOnCorreoSeleccionado {

    private static final String ACCOUNT_KEY = "com.germangascon.correoelectronico.account";
    private static final String SELECTED_EMAIL_KEY = "com.germangascon.correoelectronico.selectedemail";
    private static final String LISTING_TYPE_KEY = "com.germangascon.correoelectronico.listingtype";

    private Cuenta cuenta;
    private Email email;
    private FragmentoEmail.TipoFragmento tipoFragmento;
    private FragmentoEmail fragmentoEmail;
    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            cuenta = (Cuenta) savedInstanceState.getSerializable(ACCOUNT_KEY);
        }

        this.tipoFragmento = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (cuenta == null) {
            cargarDatos();
        }

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
        super.onSaveInstanceState(outState);
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
            tipoFragmento = FragmentoEmail.TipoFragmento.RECEIVED;
            msg = getString(R.string.received);
        } else if (id == R.id.navEnviados) {
            tipoFragmento = FragmentoEmail.TipoFragmento.SENT;
            msg = getString(R.string.sent);
        } else if (id == R.id.navNoLeidos) {
            tipoFragmento = FragmentoEmail.TipoFragmento.UNREADED;
            msg = getString(R.string.unread);
        } else if (id == R.id.navSpam) {
            tipoFragmento = FragmentoEmail.TipoFragmento.SPAM;
            msg = getString(R.string.spam);
        } else if (id == R.id.navPapelera) {
            tipoFragmento = FragmentoEmail.TipoFragmento.BIN;
            msg = getString(R.string.bin);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentoEmail).commit();
        fragmentoEmail.actualizarLista(tipoFragmento);
        setTitle(msg);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
