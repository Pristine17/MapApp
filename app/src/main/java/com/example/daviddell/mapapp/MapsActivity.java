package com.example.daviddell.mapapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,LocationFragment.OnResultObtainedListener,GoogleApiClient.OnConnectionFailedListener{


    LocationFragment lFrag;
    private GoogleMap mMap;
    PolylineOptions rectOptions;
    LocationFragment locationFragment;
    GoogleApiClient mGoogleApiClient;
    Polyline line;
    private ArrayList<LatLng> points;
    private final String TAG=MapsActivity.class.getSimpleName();


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setContentView(R.layout.activity_maps);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        locationFragment = (LocationFragment) fm.findFragmentByTag("location_fragment");
        if (locationFragment == null) {
            // add the fragment
            fm.beginTransaction().add(new LocationFragment(),"location_fragment").commit();
        }
        points = new ArrayList<LatLng>();

        rectOptions= new PolylineOptions();



    lFrag=(LocationFragment)getSupportFragmentManager().findFragmentByTag("location_fragment");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == LocationFragment.REQUEST_LOCATION) {
                lFrag.onActivityResult(requestCode, resultCode, data);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    private void redrawLine(LatLng currentLoc){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(false);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        //addMarker(); //add Marker in current position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        line = mMap.addPolyline(options); //add Polyline
    }



    @Override
    public void WeGotTheResult(Location position) {
        Double currentLat = position.getLatitude();
        Double currentLong = position.getLongitude();
        Log.e(TAG, String.valueOf(currentLat));

        Log.e(TAG, String.valueOf(currentLong));
        LatLng currentLoc = new LatLng(currentLat, currentLong);
        points.add(currentLoc);
        redrawLine(currentLoc);
        rectOptions.add(currentLoc);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




}
