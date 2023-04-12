package com.example.caminoalba.ui.menuItems.publication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.caminoalba.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.data.kml.KmlLayer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentStreetview extends Fragment implements OnStreetViewPanoramaReadyCallback {


    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_BREAKPOINTS = "breakpoints";
    private static final String ARG_GOOGLE_MAP = "googlemap";

    private List<LatLng> breakpoints;
    private KmlLayer kmlLayer;
    private GoogleMap googleMap;
    private double longitude;
    private double latitude;
    private StreetViewPanoramaView streetViewPanoramaView;

    // TODO: other code for FragmentStreetview class

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_streetview, container, false);

        // Get the StreetViewPanoramaView and initialize it.
        streetViewPanoramaView = (StreetViewPanoramaView) view.findViewById(R.id.streetview_panorama);
        streetViewPanoramaView.onCreate(savedInstanceState);
        streetViewPanoramaView.getStreetViewPanoramaAsync(this);

        // Retrieve the path data from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            latitude = bundle.getDouble(ARG_LATITUDE);
            longitude = bundle.getDouble(ARG_LONGITUDE);
            breakpoints = bundle.getParcelableArrayList(ARG_BREAKPOINTS);
//            googleMap = bundle.getParcelable(ARG_GOOGLE_MAP);
        }

        return view;
    }

    // Method to set up the path for use in the fragment
    public void setBreakpoints(List<LatLng> breakpoints) {
        this.breakpoints = breakpoints;
        // use the path to set up the StreetViewPanoramaView as desired
        // ...
    }


    public FragmentStreetview newInstance(double latitude, double longitude, List<LatLng> breakpoints) {
        FragmentStreetview fragment = new FragmentStreetview();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putParcelableArrayList(ARG_BREAKPOINTS, (ArrayList<? extends Parcelable>) breakpoints);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
            breakpoints = getArguments().getParcelableArrayList(ARG_BREAKPOINTS);
        }
    }

    @Override
    public void onStreetViewPanoramaReady(@NonNull StreetViewPanorama streetViewPanorama) {
        if (breakpoints != null) {
            PolylineOptions polylineOptions = new PolylineOptions();
            for (LatLng point : breakpoints) {
                polylineOptions.add(point);
            }

            polylineOptions.color(Color.RED);
            streetViewPanorama.setPosition(new LatLng(latitude, longitude));

            if (breakpoints != null) {
                // Create a PolylineOptions object to draw the path
                for (LatLng point : breakpoints) {
                    polylineOptions.add(point);
                }
                polylineOptions.color(Color.RED);

                // Get the GoogleMap object from the StreetViewPanorama object
                if (googleMap != null) {
                    if (kmlLayer != null) {
                        kmlLayer.addLayerToMap();
                    }else{
                        Toast.makeText(requireContext(), "Layer not found", Toast.LENGTH_SHORT).show();
                    }
                    googleMap.addPolyline(polylineOptions);
                } else {
                    Toast.makeText(requireContext(), "Map not found", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(requireContext(), "No list found", Toast.LENGTH_SHORT).show();
        }
    }


    public void button(){

    }


    public void setKmlLayer(KmlLayer kmlLayer) {
        this.kmlLayer = kmlLayer;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        streetViewPanoramaView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        streetViewPanoramaView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        streetViewPanoramaView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        streetViewPanoramaView.onLowMemory();
    }

}
