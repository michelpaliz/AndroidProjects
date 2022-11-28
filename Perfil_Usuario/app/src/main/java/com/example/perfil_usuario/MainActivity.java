package com.example.perfil_usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.perfil_usuario.Modelo.Persona;
import com.example.perfil_usuario.Modelo.PersonaDatosProfesionales;
import com.example.perfil_usuario.Modelo.Usuario;
import com.example.perfil_usuario.databinding.ActivityMainBinding;
import com.example.perfil_usuario.ui.main.FragmentoPersonal;
import com.example.perfil_usuario.ui.main.FragmentoProfesional;
import com.example.perfil_usuario.ui.main.FragmentoSesion;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements FragmentoPersonal.IOnAttachListener, FragmentoProfesional.IOnAttachListener , FragmentoSesion.IOnAttachlistener{

    private Usuario usuario;


    public MainActivity() {
        super(R.layout.activity_main);
        cargarDatos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creamos el viewpager
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        TabStateAdapter tabStateAdapter = new TabStateAdapter(this);
        viewPager2.setAdapter(tabStateAdapter);

        //Creamos las pestanyas
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Datos personales");
                        break;
                    case 1:
                        tab.setText("Datos profesionales");
                        break;
                    case 2:
                        tab.setText("Cambiar contrasenya");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

    //CARGAMOS LOS DATOS EN NUESTRA PERSONA
    public void cargarDatos(){
        usuario = new Usuario("Michael","1234", new Persona("53944920F","Michael","paliz", "02-12-2000 " ,"calle senija" , new PersonaDatosProfesionales("MICHEL S.L","B16841652","michelpaliz@hotmail.com","techpaliz.com","calle sagunto")));
    }

    @Override
    public Persona getDatosPersonales() {
        if (usuario == null){
            cargarDatos();
        }
        return usuario.getPersona();
    }

    @Override
    public PersonaDatosProfesionales getDatosProfesionales() {
        if (usuario == null){
            cargarDatos();
        }
        return usuario.getPersona().getDatosProfesionales();
    }

    @Override
    public Usuario getUsuario() {
        if (usuario == null){
            cargarDatos();
        }
        return usuario;
    }


    public static class TabStateAdapter extends FragmentStateAdapter{


        public TabStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new FragmentoPersonal();
                case 1:
                    return new FragmentoProfesional();
                case 2:
                    return new FragmentoSesion();
            }
            return new Fragment();
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }




}