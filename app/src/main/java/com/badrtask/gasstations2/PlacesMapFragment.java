package com.badrtask.gasstations2;


import android.Manifest;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;

import com.google.android.gms.maps.model.LatLng;

import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.MapView;
import com.badrtask.gasstations2.pojos.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.badrtask.gasstations2.locationmanager.GPSTracker;


public class PlacesMapFragment extends Fragment {


    private MapView mMapView;

    private GoogleMap googleMap;

    // GPS Location
    private GPSTracker gps;

    /// Array list will be in the listview.

    private ArrayList<Place> GasStationList;

    private View rootView;

    private LatLng sydney;

    // Reference from MainActivity to call the Method GetGasStatioList.
    private MainActivity activity;


    public static PlacesMapFragment newInstance() {
        return new PlacesMapFragment();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // creating GPS Class object
        gps = new GPSTracker(getActivity());

        // check if GPS location can get
        if (!gps.canGetLocation()) {// Startt of if
            // Can't get user's current location
            gps.showSettingsAlert();
        }/// End of if

        rootView = inflater.inflate(R.layout.fragment_places_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }// End of catch

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                activity = (MainActivity) getActivity();

                GasStationList = activity.getGasStationPlacesList();
                for (int i = 0; i < GasStationList.size(); i++) {
                    sydney = new LatLng(GasStationList.get(i).getLat(), GasStationList.get(i).getLng());
                    googleMap.addMarker(new MarkerOptions().position(sydney).title(GasStationList.get(i).getName()).snippet("Marker Description"));
                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        return rootView;
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     *
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}