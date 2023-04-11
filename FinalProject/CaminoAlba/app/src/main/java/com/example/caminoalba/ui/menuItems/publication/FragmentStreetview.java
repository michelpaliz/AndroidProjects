package com.example.caminoalba.ui.menuItems.publication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.example.caminoalba.R;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLink;

import java.util.ArrayList;
import java.util.List;

public class FragmentStreetview extends Fragment implements OnStreetViewPanoramaReadyCallback {


    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_BREAKPOINTS = "breakpoints";
    private static final String ARG_CLOSEST_LATITUDE = "closest_latitude" ;
    private static final String ARG_CLOSEST_LONGITUDE = "closest_longitud";

    private List<LatLng> breakpoints;
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
            breakpoints = bundle.getParcelableArrayList("path");
        }

        return view;
    }

    // Method to set up the path for use in the fragment
    public void setBreakpoints(ArrayList<LatLng> breakpoints) {
        this.breakpoints = breakpoints;
        // use the path to set up the StreetViewPanoramaView as desired
        // ...
    }

//    public static FragmentStreetview newInstance(double latitude, double longitude) {
//        FragmentStreetview fragment = new FragmentStreetview();
//        Bundle args = new Bundle();
//        args.putDouble(ARG_LATITUDE, latitude);
//        args.putDouble(ARG_LONGITUDE, longitude);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static FragmentStreetview newInstance(double latitude, double longitude, LatLng[] breakpoints) {
        FragmentStreetview fragment = new FragmentStreetview();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putParcelableArray(ARG_BREAKPOINTS, breakpoints);
        fragment.setArguments(args);
        return fragment;
    }



//    @Override
//    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
//        // Initialize the StreetViewPanorama object.
//
//        // Set the position of the StreetViewPanorama.
//        panorama.setPosition(new LatLng(latitude, longitude));
//    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        // Initialize the StreetViewPanorama object.

        // Create a new StreetViewPanoramaCamera object and set its properties.
        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                .bearing(0)
                .tilt(0)
                .zoom(1)
                .build();

        // Set the camera position of the StreetViewPanorama.
        panorama.animateTo(camera, 0);

        // Create a new StreetViewPanoramaLink array and set its properties.
        StreetViewPanoramaLink[] links = new StreetViewPanoramaLink[breakpoints.size()];
        for (int i = 0; i < breakpoints.size(); i++) {
            LatLng breakpoint = breakpoints.get(i);
            links[i] = new StreetViewPanoramaLink(String.valueOf(breakpoint.latitude) + "," + String.valueOf(breakpoint.longitude), 0);

        }

        // Set the StreetViewPanoramaCamera and StreetViewPanoramaLink array of the StreetViewPanorama.
        panorama.setPosition(new LatLng(breakpoints.get(0).latitude, breakpoints.get(0).longitude));
        panorama.setStreetNamesEnabled(false);
        panorama.setZoomGesturesEnabled(true);
        panorama.setUserNavigationEnabled(true);
        panorama.setPanningGesturesEnabled(true);
        panorama.setStreetViewPanoramaCamera(camera);
        panorama.setLinks(links);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
        }
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
