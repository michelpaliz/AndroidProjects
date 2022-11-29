package com.example.tabbedactivity;

import android.os.Bundle;
import com.example.tabbedactivity.ui.main.FragmentoPersonal;
import com.example.tabbedactivity.ui.main.FragmentoProfesional;
import com.example.tabbedactivity.ui.main.FragmentoSinglenton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class MainActivity extends AppCompatActivity implements FragmentoPersonal.IOnAttachListener, FragmentoProfesional.IOnAttachListener {

    //Es una forma de inyectar bindigs en nuestro compilador/proyecto
//    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Creamos el toolbar
        setTitle("Datos del usuario");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //El viewpager esta abajo de las pestanyas de los tabs
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        TabStateAdapter   tabStateAdapter = new TabStateAdapter(this);
        viewPager2.setAdapter(tabStateAdapter); //apartir de ahora el viewpager puede gestionar las pestanyas por medio del adaptador

        //Las pestanyas de los tabs
        TabLayout tabs = findViewById(R.id.tabs);
        //si es fixed lo divide proporcionalment ; el auto coje lo mas justo como el default;
        tabs.setTabMode(TabLayout.MODE_FIXED);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabs, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Datos personales");
                        break;
                    case 1:
                        tab.setText("Datos profesionales");
                        break;
                }
            }
        });

        //Para que se incluya el tableLayout con el viewpager2
        tabLayoutMediator.attach();

//        tabs.setupWithViewPager(viewPager2);


    }

    @Override
    public String getDatosPersonales() {
        return "Datos personales";
    }

    @Override
    public String getDatosProfesionales() {
        return "Datos profesionales";
    }

    //TODO cargar 2 fragmentos y devolverlo
    public static class TabStateAdapter extends FragmentStateAdapter {

        //No hacemos nada en el constructor.
        public TabStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }


        //Gestiona la creacion de los fragments
        @NonNull
        @Override
        public Fragment createFragment(int position) {
        //Para crear fragmentos tener en cuenta que los fragmentos se reutilizan no se crean cada vez;
            switch (position){
                case 0:
                    return  new FragmentoPersonal();
                case 1:
                    return new FragmentoProfesional();
                default:
                    return new FragmentoSinglenton();
            }
            //Esto es para que no salte el error ya que indicamos en el get ItemCount tiene 2 opciones;
        }

        //2 PESTANYAS QUE TENEMOS
        @Override
        public int getItemCount() {
            return 3;
        }
    }


}