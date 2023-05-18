package com.example.caminoalba;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caminoalba.models.Profile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NavigationDrawerActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;


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
        getCurrentUser();

    }


    public void getCurrentUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            DatabaseReference profiles = FirebaseDatabase.getInstance().getReference("profiles");
            DatabaseReference profileRef = profiles.child(uid);

            profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Retrieve the user object from the dataSnapshot
                    Profile profile = dataSnapshot.getValue(Profile.class);

                    if (profile != null) {
                        // Use the user object as needed
                        // ...

                        setSupportActionBar(findViewById(R.id.toolbar));

                        DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        NavigationView navigationView = findViewById(R.id.nav_view);
                        View headerView = navigationView.getHeaderView(0);
                        TextView tvName = headerView.findViewById(R.id.tvName);
                        TextView tvEmail = headerView.findViewById(R.id.tvEmail);
                        ImageView imgProfile = headerView.findViewById(R.id.imgProfile);

                        //BE AWARE THAT WE HAVE TO PUT THE RULES FOR THE NAVIGATION VIEW AFTERWARDS ITS DECLARATION NOT BEFORE
                        //TO HIDE THE DEFAULT ACTION BAR NAME
                        ActionBar actionBar = getSupportActionBar();
                        if (actionBar != null) {
                            actionBar.setDisplayShowTitleEnabled(false);
                        }
                        //TO PUT A COSTUME NAME FOR MY ACTION BAR
                        Toolbar toolbar = findViewById(R.id.toolbar);
                        TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);
                        titleTextView.setText("CAMINO  DEL  ALBA");

                        if (profile.getPhoto() != null && !profile.getPhoto().isEmpty()) {
                            Glide.with(NavigationDrawerActivity.this)
                                    .load(profile.getPhoto())
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.default_image) // Placeholder image while loading
                                            .error(R.drawable.default_image)) // Image to display in case of error
                                    .circleCrop() // Apply circular cropping
                                    .into(imgProfile);
                        } else {
                            Picasso.get().load(R.drawable.default_image).into(imgProfile);
                        }

                        tvName.setText(profile.getFirstName().toUpperCase() + " " + profile.getLastName().toUpperCase());
                        tvEmail.setText(profile.getUser().getEmail());

                        // ...

                        if (profile.getUser().getType().equalsIgnoreCase("admin")) {
                            mAppBarConfiguration = new AppBarConfiguration.Builder(
                                    R.id.blogFragment, R.id.newsFragment, R.id.profileFragment, R.id.fragmentConfirmationPartnership, R.id.logOutFragment, R.id.fragment_settings)
                                    .setOpenableLayout(drawer)
                                    .build();
                            navigationView.getMenu().findItem(R.id.fragmentConfirmationPartnership).setVisible(true);
                        } else {
                            mAppBarConfiguration = new AppBarConfiguration.Builder(
                                    R.id.blogFragment, R.id.newsFragment, R.id.profileFragment, R.id.logOutFragment, R.id.fragment_settings)
                                    .setOpenableLayout(drawer)
                                    .build();
                            navigationView.getMenu().findItem(R.id.fragmentConfirmationPartnership).setVisible(false);
                        }

                        NavController navController = Navigation.findNavController(NavigationDrawerActivity.this, R.id.nav_host_fragment_content_navigation_drawer);

                        NavigationUI.setupActionBarWithNavController(NavigationDrawerActivity.this, navController, mAppBarConfiguration);
                        NavigationUI.setupWithNavController(navigationView, navController);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error, if any
                    // ...
                }
            });
        }
    }
}