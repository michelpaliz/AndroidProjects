package com.example.navigationdrawerexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.navigationdrawerexample.Fragmentos.FragmentoCamara;
import com.example.navigationdrawerexample.Fragmentos.FragmentoGaleria;
import com.example.navigationdrawerexample.Fragmentos.FragmentoSettings;
import com.example.navigationdrawerexample.Fragmentos.FragmentoShare;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Obtenemos los layout personalizados de nuestro Drawer creado
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        ImageView imageView = view.findViewById(R.id.ivProfile);
        TextView tvUser = view.findViewById(R.id.tvUser);
        tvUser.setText(R.string.nav_header_title);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        tvEmail.setText(R.string.nav_header_subtitle);


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Creamos el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragment = new FragmentoCamara();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentoContenido, fragment)
                    .commit();
            setTitle(R.string.camara);
        } else if (id == R.id.nav_gallery) {
            fragment = new FragmentoGaleria();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentoContenido, fragment)
                    .commit();
            setTitle(R.string.galeria);
        } else if (id == R.id.nav_tools) {
            fragment = new FragmentoSettings();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentoContenido, fragment)
                    .commit();
            setTitle(R.string.tools);
        } else if (id == R.id.nav_share){
            fragment = new FragmentoShare();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentoContenido, fragment)
                    .commit();
            setTitle(R.string.share);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}