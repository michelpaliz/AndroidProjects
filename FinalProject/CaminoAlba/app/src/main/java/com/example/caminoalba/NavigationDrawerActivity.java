package com.example.caminoalba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.ui.menuItems.profile.FragmentProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class NavigationDrawerActivity extends AppCompatActivity implements FragmentProfile.ProfileUpdateListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View headerView;
    private TextView tvName;
    private TextView tvEmail;
    private ImageView imgProfile;
    private Profile profile;


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        setSupportActionBar(findViewById(R.id.toolbar));
        getUser();
        chargeData();

    }


    public void getUser() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Gson gson = new Gson();
        String profileStr = preferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);
    }


    public void chargeData() {
        //BE AWARE THAT WE HAVE TO PUT THE RULES FOR THE NAVIGATION VIEW AFTERWARDS ITS DECLARATION NOT BEFORE
        //TO HIDE THE DEFAULT ACTION BAR NAME
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        imgProfile = headerView.findViewById(R.id.imgProfile);
        //TO PUT A COSTUME NAME FOR MY ACTION BAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);
        titleTextView.setText("CAMINO DEL ALBA");

        updateProfileData(profile);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.blogFragment, R.id.newsFragment, R.id.profileFragment, R.id.logOutFragment, R.id.fragment_settings, R.id.fragment_partner)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(NavigationDrawerActivity.this, R.id.nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(NavigationDrawerActivity.this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void updateProfileData(Profile profile) {
        if (profile.getPhoto() != null && !profile.getPhoto().isEmpty()) {
            Glide.with(NavigationDrawerActivity.this)
                    .load(profile.getPhoto())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.default_image)
                            .error(R.drawable.default_image))
                    .circleCrop()
                    .into(imgProfile);
        } else {
            Picasso.get().load(R.drawable.default_image).into(imgProfile);
        }

        tvName.setText(profile.getFirstName().toUpperCase() + " " + profile.getLastName().toUpperCase());
        tvEmail.setText(profile.getUser().getEmail());
        navigationView.invalidate();
    }


    protected void recreateNavigationDrawer() {
        // Update the navigation drawer menu items here
        updateMenuItemsLanguage();

        // Update the profile data
        updateProfileData(profile);
    }

    private void updateMenuItemsLanguage() {
        // Retrieve the navigation menu from the navigation view
        Menu menu = navigationView.getMenu();

        // Iterate over the menu items and update their language
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);

            // Update the title of each menu item with the new language
            String newTitle = translateMenuItemTitle(menuItem.getItemId());
            menuItem.setTitle(newTitle);
        }
    }

    private String translateMenuItemTitle(int itemId) {
        // Implement your logic to translate the menu item titles based on the selected language
        // Return the translated title for the given menu item ID
        // You can use a resource file or any other translation mechanism
        // Example:
        switch (itemId) {
            case R.id.profileFragment:
                return "Perfil";
            case R.id.newsFragment:
                return "Noticias";
            case R.id.blogFragment:
                return "Publicaciones";
            case R.id.fragment_partner:
                return "Sellado";
            case R.id.fragment_settings:
                return "Configuracion";
            case R.id.logOutFragment:
                return "Salir";
            // Add more cases for other menu items
            default:
                return "";
        }
    }


//    @Override
//    public void onProfileUpdated(Profile profile) {
//        this.profile = profile;
//        updateProfileData(profile);
//    }

    @Override
    public void onProfileUpdated(Profile profile) {
        this.profile = profile;
        recreateNavigationDrawer();
    }
}