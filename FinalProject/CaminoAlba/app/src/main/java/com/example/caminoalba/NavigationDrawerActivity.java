package com.example.caminoalba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.caminoalba.databinding.ActivityNavigationDrawerBinding;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;


public class NavigationDrawerActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        // ------ We get the user sent by the login activity -------
        String userStr = preferences.getString("profile", "");
        Profile profile = gson.fromJson(userStr, Profile.class);

        com.example.caminoalba.databinding.ActivityNavigationDrawerBinding binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
//        binding.appBarNavigationDrawer.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvName);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);
        ImageView imgProfile = headerView.findViewById(R.id.imgProfile);
        tvName.setText(profile.getFirstName().toUpperCase() + " " + profile.getLastName().toUpperCase());
        tvEmail.setText(profile.getUser().getEmail());
        if (profile.getPhoto() != null && !profile.getPhoto().isEmpty()){
            Picasso.get().load(profile.getPhoto()).into(imgProfile);
        }else{
            Picasso.get().load(R.drawable.default_image).into(imgProfile);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if (profile.getUser().getType().equalsIgnoreCase("admin")){
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.blogFragment, R.id.newsFragment, R.id.profileFragment, R.id.fragmentConfirmationPartnership, R.id.logOutFragment)
                    .setOpenableLayout(drawer)
                    .build();

            navigationView.getMenu().findItem(R.id.fragmentConfirmationPartnership).setVisible(true);
        }else{
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.blogFragment, R.id.newsFragment, R.id.profileFragment, R.id.logOutFragment)
                    .setOpenableLayout(drawer)
                    .build();

            navigationView.getMenu().findItem(R.id.fragmentConfirmationPartnership).setVisible(false);
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}