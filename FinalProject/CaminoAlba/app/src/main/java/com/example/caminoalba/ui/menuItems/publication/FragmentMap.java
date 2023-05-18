package com.example.caminoalba.ui.menuItems.publication;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Path;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.ui.menuItems.partner.FragmentPartner;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.Point;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentMap extends Fragment implements OnMapReadyCallback, LocationListener {

    public boolean isEnabled;
    public String placemarkName;
    private List<Path> breakpointsInfo;
    private OnDataPass dataPasser;
    private GoogleMap map;
    private List<LatLng> breakpoints;
    private int currentBreakpointIndex = 0;
    private LocationManager locationManager;
    private boolean reachedDestination = false;
    private Marker currentLocationMarker;
    private KmlLayer layer;
    private ImageView imgHome, imgMap;
    private Button btnPathInformation;
    private SharedPreferences preferences;
    private Profile profile;
    private String currentPath;


    public interface OnDataPass {
        void onDataPass(String placemarkName, boolean isEnabled);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnDataPass) {
            dataPasser = (OnDataPass) parentFragment;
        } else {
            assert parentFragment != null;
            throw new ClassCastException(parentFragment + " must implement OnDataPass");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataPasser = null;
    }

    private void passDataToParent(String data, boolean isEnabled) {
        if (dataPasser != null) {
            dataPasser.onDataPass(data, isEnabled);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentBreakpointIndex", currentBreakpointIndex);
    }


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        imgHome = view.findViewById(R.id.imgHome);
        imgMap = view.findViewById(R.id.imgMap);
        btnPathInformation = view.findViewById(R.id.btnInformation);
        breakpointsInfo = new ArrayList<>();
        profile = new Profile();
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Gson gson = new Gson();
        String profileStr = preferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);
        // Get the MapView from the layout
        MapView mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();


        int color1 = ContextCompat.getColor(requireContext(), R.color.darkpink); // Replace with your desired color resource ID
        Drawable drawable1 = ContextCompat.getDrawable(requireContext(), R.drawable.icon_map); // Replace with your vector image resource ID
        imgMap.setImageDrawable(drawable1);
        imgMap.setColorFilter(color1, PorterDuff.Mode.SRC_IN);


        // Restore the value of currentBreakpointIndex if the fragment is being re-created.
        if (savedInstanceState != null) {
            currentBreakpointIndex = savedInstanceState.getInt("currentBreakpointIndex");
        }

        // Get the map asynchronously
        mapView.getMapAsync(this);

        return view;
    }


    public void goHome() {

        imgHome.setOnClickListener(v -> {
            // In the child fragment, get the FragmentManager
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            // Pop the back stack to return to the parent fragment
            fragmentManager.popBackStack();

            passDataToParent(placemarkName, isEnabled);
        });

    }



    public void goToPathInformation(List<Path> breakpointsInf) {
        btnPathInformation.setOnClickListener(v -> {
            // Create an instance of the child fragment
            FragmentPartner fragmentPartner = new FragmentPartner();
            // Set the list to the static variable in the FragmentPartner class
            FragmentPartner.setBreakpointsInf(breakpointsInf);
            // Begin a new FragmentTransaction using the getChildFragmentManager() method
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            // Add the child fragment to the transaction and specify a container view ID in the parent layout
            transaction.replace(R.id.fragment_map, fragmentPartner);
            transaction.addToBackStack(null); // Add the fragment to the back stack
            transaction.commit();
        });
    }



//    public void goToPathInformation(List<Path> breakpointsInf) {
//        btnPathInformation.setOnClickListener(v -> {
//            // Create an instance of the child fragment
//            FragmentPartner fragmentPartner = new FragmentPartner();
//            // Set the HashMap as an argument to the fragment
//            fragmentPartner.setBreakpointsInf(breakpointsInf);
//            // Begin a new FragmentTransaction using the getChildFragmentManager() method
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            // Add the child fragment to the transaction and specify a container view ID in the parent layout
//            transaction.replace(R.id.fragment_map, fragmentPartner);
//            transaction.addToBackStack(null); // Add the fragment to the back stack
//            transaction.commit();
//        });
//
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        loadKmlMarkers(map);

        // Initialize location manager.
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // Check if user has granted location permission.
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        } else {
            // Request location permission from user.
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    public void loadKmlMarkers(GoogleMap map) {
        try {
            // Read KML file from assets.
            InputStream is = requireActivity().getAssets().open("mapa.kml");

            // Parse KML file and add markers to the map.
            layer = new KmlLayer(map, is, requireContext());
            layer.addLayerToMap();

            breakpoints = new ArrayList<>();

            int cont = 0;
            // Check if layer has finished loading before setting click listener.
            if (layer.isLayerOnMap()) {

                // Iterate over the placemarks in the layer.
                for (KmlPlacemark placemark : layer.getPlacemarks()) {
                    // Access placemark properties and geometry here.
//                    String id = placemark.getProperty("id");
                    String name = placemark.getProperty("name");
                    String description = placemark.getProperty("description");
                    Geometry<?> geometry = placemark.getGeometry();

                    // Check if this is a breakpoint marker.
                    if (geometry.getGeometryType().equals("Point") && name != null && description != null && name.contains("bp")) {
                        String newString = name.replaceAll("^bp_(\\w+)$", "$1").replaceAll("(?<!^)([A-Z])", "_$1").toUpperCase();
                        System.out.println(newString);
                        LatLng position = ((Point) geometry).getGeometryObject();
                        breakpoints.add(position);
                        // Add the id and name to the breakpointsInfo HashMap.
                        cont++;
                        breakpointsInfo.add(new Path(cont,newString, description, null, false));
                    }

                }

                goToPathInformation(breakpointsInfo);

//                Toast.makeText(requireContext(), "Number of breakpoints: " + breakpoints.size(), Toast.LENGTH_SHORT).show();
                System.out.println("These are my breakpoints list " + breakpoints);


            } else {
                Toast.makeText(requireContext(), "KML layer failed to load", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    public void onLocationChanged(Location location) {

        placemarkName = "";
        isEnabled = false;

        // Get current user location.
        LatLng currentLocationLocal = new LatLng(location.getLatitude(), location.getLongitude());

        // Add marker for current location with red color label.
        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            map.setBuildingsEnabled(true);

            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            currentLocationMarker = map.addMarker(new MarkerOptions()
                    .position(currentLocationLocal)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


            if (!reachedDestination) {
                // Find the closest breakpoint to the user's current location.
                double closestDistance = Double.MAX_VALUE;
                LatLng closestBreakpoint = null;
                for (LatLng breakpoint : breakpoints) {
                    float[] distance = new float[1];
                    Location.distanceBetween(currentLocationLocal.latitude, currentLocationLocal.longitude,
                            breakpoint.latitude, breakpoint.longitude, distance);

                    if (distance[0] < closestDistance) {
                        closestDistance = distance[0];
                        closestBreakpoint = breakpoint;
                    }
                }

                // If user is within 50 meters of closest breakpoint, display the name of the breakpoint.
                if (closestDistance < 200) {
                    String name = null;
                    for (KmlPlacemark placemark : layer.getPlacemarks()) {
                        if (placemark.getGeometry().getGeometryType().equals("Point") && placemark.getGeometry().getGeometryObject().equals(closestBreakpoint)) {
                            name = placemark.getProperty("name");
                            break;
                        }
                    }

//                    Toast.makeText(requireContext(), "You have reached breakpoint " + name, Toast.LENGTH_SHORT).show();
                    currentPath = name;

                    DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profiles").child(profile.getProfile_id());
                    profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Get the Profile object from the snapshot
                                Profile existingProfile = snapshot.getValue(Profile.class);
                                if (existingProfile != null) {
                                    // Update the desired attribute in the Profile object
                                    String newString = currentPath.replaceAll("^bp_(\\w+)$", "$1").replaceAll("(?<!^)([A-Z])", "_$1").toUpperCase();
                                    existingProfile.setCurrentPath(newString);
                                    // Save the updated Profile object to the Firebase Realtime Database
                                    profileRef.setValue(existingProfile);
                                }
                            } else {
                                // Profile object not found
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });


                    placemarkName = name;
                    isEnabled = true;


                    // Update currentBreakpointIndex to the index of the closest breakpoint.
                    int nextBreakpointIndex = currentBreakpointIndex + 1;
                    if (nextBreakpointIndex >= breakpoints.size()) {
                        // User has reached final destination.
                        Toast.makeText(requireContext(), "You have reached your destination", Toast.LENGTH_SHORT).show();
                        reachedDestination = true;
                        locationManager.removeUpdates(this);
                    } else {
                        currentBreakpointIndex = nextBreakpointIndex;
                    }
                }

                goHome();

                // Move camera to current user location.
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLocal, 17));
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Request location updates.
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
            } else {
                // Permission denied.
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}