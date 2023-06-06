package com.example.caminoalba.ui.menuItems.partner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caminoalba.FragmentConfirmationPartnership;
import com.example.caminoalba.R;
import com.example.caminoalba.models.Badge;
import com.example.caminoalba.models.Path;
import com.example.caminoalba.models.Profile;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FragmentPartner extends Fragment implements OnMapReadyCallback, FragmentConfirmationPartnership.ConfirmationCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private RecyclerView recyclerView;
    private Profile profile;
    private ImageView ivProfilePhoto, ivQr;
    private TextView tvProfileId, tvProfileName;
    private Button btnGoBadge;
    private RecyclerviewPartner adapter;
    private Context context;

    // Define a static variable to store the list
    private static List<Path> breakpointsInf = new ArrayList<>();
    private FragmentConfirmationPartnership.ConfirmationCallback callback; // Declare the callback variable


    public void setCallback(FragmentConfirmationPartnership.ConfirmationCallback callback) {
        this.callback = callback;
    }


    @Override
    public void onBadgeAdded(Badge badge) {
        // Update the UI to display the new badge
        if (profile != null && adapter != null) {
            adapter.notifyDataSetChanged();
        }

        // No need for the recursive call here
    }


    @Override
    public void onConfirmationComplete(Profile updatedProfile) {
        // Update the profile and refresh the RecyclerView
        updateProfile(updatedProfile);

        // Call the callback method if it's not null
        if (callback != null) {
            callback.onConfirmationComplete(updatedProfile);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        callback = null; // Assign null to callback when there is no parent fragment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partner, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_partner);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ivProfilePhoto = view.findViewById(R.id.profilePhoto);
        tvProfileName = view.findViewById(R.id.tvUserName);
        btnGoBadge = view.findViewById(R.id.btnBadge);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Gson gson = new Gson();
        String profileStr = preferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);

        tvProfileName.setText(profile.getFirstName().toUpperCase());

        getUserFromFireBase();

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        btnGoBadge.setOnClickListener(v -> {
            FragmentConfirmationPartnership fragmentPartner = new FragmentConfirmationPartnership();
            setCallback(this); // Set the callback to the current FragmentPartner instance
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_partner, fragmentPartner);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        return view;
    }

    public void getUserFromFireBase() {
        // Get the current Firebase user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Check if the user is logged in
        if (firebaseUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference profileRef = database.getReference("profiles").child(profile.getProfile_id());
            profileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called whenever the data at the specified database reference changes.
                    // Use dataSnapshot to retrieve the updated data.

                    // Get the profile from the dataSnapshot
                    Profile updatedProfile = dataSnapshot.getValue(Profile.class);

                    assert updatedProfile != null;

                    if (!profile.equals(updatedProfile)) {
                        updateProfile(updatedProfile);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void updateProfile(Profile updatedProfile) {
        profile = updatedProfile;
        tvProfileName.setText(profile.getFirstName().toUpperCase());

        if (isAdded() && !isDetached()) {
            if (profile.getPhoto() != null) {
                Glide.with(context)
                        .load(profile.getPhoto())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.default_image)
                                .error(R.drawable.default_image))
                        .circleCrop()
                        .into(ivProfilePhoto);
            }
        }


        if (adapter != null) {
            adapter.setProfile(profile);
            adapter.notifyDataSetChanged();

            // Update the profile's badges list if it exists
            if (profile.getBadges() != null) {
                adapter.setProfile(profile);
            }
        }

        // Update the profile in preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String profileStr = gson.toJson(profile);
        editor.putString("profile", profileStr);
        editor.apply();
    }

    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setAllGesturesEnabled(false); // Disable map gestures
        loadKmlMarkers(map, profile);
//        if (breakpointsInf.isEmpty()) {
//            // Load KML markers only if the list is empty
//            loadKmlMarkers(map, profile);
//        } else {
//            // Use the existing list to populate the RecyclerView
//            if (adapter == null) {
//                adapter = new RecyclerviewPartner(breakpointsInf, profile, requireContext());
//                recyclerView.setAdapter(adapter);
//            } else {
//                adapter.setBreakpointsInf(breakpointsInf);
//                adapter.notifyDataSetChanged();
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        // Update the UI to display the new badge
        getUserFromFireBase();
        if (profile != null && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void loadKmlMarkers(GoogleMap map, Profile updatedProfile) {
        try {
            // Read KML file from assets.
            InputStream is = requireActivity().getAssets().open("mapa.kml");

            // Parse KML file and add markers to the map.
            KmlLayer layer = new KmlLayer(map, is, requireContext());
            layer.addLayerToMap();

            breakpointsInf = new ArrayList<>();

            int cont = 0;
            // Check if layer has finished loading before setting click listener.
            if (layer.isLayerOnMap()) {
                // Iterate over the placemarks in the layer.
                for (KmlPlacemark placemark : layer.getPlacemarks()) {
                    // Access placemark properties and geometry here.
                    String name = placemark.getProperty("name");
                    String description = placemark.getProperty("description");
                    Geometry<?> geometry = placemark.getGeometry();

                    // Check if this is a breakpoint marker.
                    if (geometry.getGeometryType().equals("Point") && name != null && description != null && name.contains("bp")) {
                        String newString = name.replaceAll("^bp_(\\w+)_\\d+$", "$1").replaceAll("(?<!^)([A-Z])", "_$1").toUpperCase();
                        System.out.println(newString);
                        // Add the id and name to the breakpointsInfo HashMap.
                        cont++;

                        String[] parts = name.split("_");
                        String lastNumberString = parts[parts.length - 1];
                        int lastNumber = Integer.parseInt(lastNumberString);

                        if (lastNumber == 0) {
                            breakpointsInf.add(new Path(cont, newString, description, null, 0, false));
                        } else {
                            breakpointsInf.add(new Path(cont, newString, description, null, lastNumber, false));
                        }
                    }
                }

                if (breakpointsInf != null) {
                    // Sort the list based on the 'orden' property
                    breakpointsInf.sort(Comparator.comparingInt(Path::getOrden));
                    // Create the RecyclerView adapter with the sorted list
                    if (adapter == null) {
                        adapter = new RecyclerviewPartner(breakpointsInf, updatedProfile, requireContext());
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setBreakpointsInf(breakpointsInf);
                        adapter.setProfile(updatedProfile);
                        adapter.notifyDataSetChanged();
                    }

                    // Update the profile based on the breakpoints
                    updateProfile(updatedProfile);
                }
            } else {
                Toast.makeText(requireContext(), "KML layer failed to load", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}