package com.example.caminoalba;


import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.Point;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentMap extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap map;
//    private LocationManager locationManager;
//    private LocationListener locationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Save the GoogleMap object to the member variable.
        map = googleMap;
        // Use the map object to add markers or manipulate the map.
        loadKmlMarkers(map);

    }

    
    public void loadKmlMarkers(GoogleMap map) {

        // Add a marker in Xabia and move the camera
        LatLng xabia = new LatLng(38.7891, 0.1663);
        map.addMarker(new MarkerOptions().position(xabia).title("Marker in Xabia"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(xabia, 15f));

        try {
            // Read KML file from assets.
            InputStream is = requireActivity().getAssets().open("mapa.kml");

            // Parse KML file and add markers to the map.
            KmlLayer layer = new KmlLayer(map, is, requireContext());
            layer.addLayerToMap();

            List<LatLng> breakpoints = new ArrayList<>();

            // Check if layer has finished loading before setting click listener.
            if (layer.isLayerOnMap()) {

                // Iterate over the placemarks in the layer.
                for (KmlPlacemark placemark : layer.getPlacemarks()) {
                    // Access placemark properties and geometry here.
                    String name = placemark.getProperty("name");
                    Geometry<?> geometry = placemark.getGeometry();

                    // Check if this is a breakpoint marker.
                    if (geometry.getGeometryType().equals("Point") && name != null && name.contains("breakpoint")) {
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
}
