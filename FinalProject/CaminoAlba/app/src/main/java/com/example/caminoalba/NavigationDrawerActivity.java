package com.example.caminoalba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import com.example.caminoalba.models.Profile;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class NavigationDrawerActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();

        // ------ We get the user sent by the login activity -------
        String userStr = preferences.getString("profile", "");
        Profile profile = gson.fromJson(userStr, Profile.class);

        setSupportActionBar(findViewById(R.id.toolbar));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        //TO HIDE THE DEFAULT ACTION BAR NAME
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //TO PUT A COSTUME NAME FOR MY ACTION BAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);
        titleTextView.setText("CAMINO  DEL  ALBA");

        if (profile.getUser().getType().equalsIgnoreCase("admin")){
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.blogFragment, R.id.newsFragment, R.id.profileFragment, R.id.fragmentConfirmationPartnership, R.id.logOutFragment, R.id.fragment_settings)
                    .setOpenableLayout(drawer)
                    .build();
            navigationView.getMenu().findItem(R.id.fragmentConfirmationPartnership).setVisible(true);
        }else{
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.blogFragment, R.id.newsFragment, R.id.profileFragment, R.id.logOutFragment, R.id.fragment_settings)
                    .setOpenableLayout(drawer)
                    .build();
            navigationView.getMenu().findItem(R.id.fragmentConfirmationPartnership).setVisible(false);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}