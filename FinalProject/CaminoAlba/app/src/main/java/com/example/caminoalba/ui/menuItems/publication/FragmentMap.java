package com.example.caminoalba.ui.menuItems.publication;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.caminoalba.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
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

    private GoogleMap map;
    private List<LatLng> breakpoints;
    private int currentBreakpointIndex = 0;
    private LocationManager locationManager;
    private boolean reachedDestination = false;
    private KmlLayer layer;
    private Button btnStreetView;
    private Location currentLocation;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Get the MapView from the layout
        MapView mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();


        // Restore the value of currentBreakpointIndex if the fragment is being re-created.
        if (savedInstanceState != null) {
            currentBreakpointIndex = savedInstanceState.getInt("currentBreakpointIndex");
        }

        // Get the map asynchronously
        mapView.getMapAsync(this);


        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentBreakpointIndex", currentBreakpointIndex);
    }


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

            // Check if layer has finished loading before setting click listener.
            if (layer.isLayerOnMap()) {

                // Iterate over the placemarks in the layer.
                for (KmlPlacemark placemark : layer.getPlacemarks()) {
                    // Access placemark properties and geometry here.
                    String name = placemark.getProperty("name");
                    Geometry<?> geometry = placemark.getGeometry();

                    // Check if this is a breakpoint marker.
                    if (geometry.getGeometryType().equals("Point") && name != null && name.contains("bp")) {
                        LatLng position = ((Point) geometry).getGeometryObject();
                        breakpoints.add(position);
                    }

                }

                Toast.makeText(requireContext(), "Number of breakpoints: " + breakpoints.size(), Toast.LENGTH_SHORT).show();
                System.out.println("These are my breakpoints list " + breakpoints);


            } else {
                Toast.makeText(requireContext(), "KML layer failed to load", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    public void onLocationChanged(Location location) {
        // Get current user location.
        LatLng currentLocationLocal = new LatLng(location.getLatitude(), location.getLongitude());

        // Add marker for current location with red color label.
        if (map != null) {
            map.addMarker(new MarkerOptions()
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
                if (closestDistance < 50) {
                    String name = null;
                    for (KmlPlacemark placemark : layer.getPlacemarks()) {
                        if (placemark.getGeometry().getGeometryType().equals("Point") && placemark.getGeometry().getGeometryObject().equals(closestBreakpoint)) {
                            name = placemark.getProperty("name");
                            break;
                        }
                    }
                    Toast.makeText(requireContext(), "You have reached breakpoint " + name, Toast.LENGTH_SHORT).show();

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
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
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