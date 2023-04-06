package com.example.caminoalba;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FragmentMap extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap map;

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
        // ...

        // Add a marker in Sydney, Australia and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        try {
            // Read KML file from assets.
            InputStream is = requireActivity().getAssets().open("mapa.kml");

            // Parse KML file and add markers to the map.
            KmlLayer layer = new KmlLayer(map, is, requireContext() );
            layer.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e("KML Error", "XML Parsing Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("KML Error", "IO Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("KML Error", "Error: " + e.getMessage());
        }
    }


}