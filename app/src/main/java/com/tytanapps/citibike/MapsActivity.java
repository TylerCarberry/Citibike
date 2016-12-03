package com.tytanapps.citibike;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Iterator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;

    // Point the map's listeners at the listeners implemented by the cluster
    // manager.
    CameraPosition mPreviousCameraPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //enableStrictMode();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(11);

        BikeTask bikeTask = new BikeTask(){
            @Override
            protected void onPostExecute(BikeData bikeData) {
                super.onPostExecute(bikeData);
                setUpClusterer(bikeData);
            }
        };
        bikeTask.execute();


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").snippet("Snippet"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer(BikeData bikeData) {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition position = mMap.getCameraPosition();
                if(mPreviousCameraPosition == null || mPreviousCameraPosition.zoom != position.zoom) {
                    mPreviousCameraPosition = mMap.getCameraPosition();
                    mClusterManager.cluster();
                }
            }
        });
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addBikes(bikeData);
    }

    private void addBikes(BikeData bikeData) {
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(),mMap,mClusterManager));

        Log.d(TAG, "addBikes() called with: bikeData = [" + bikeData + "]");

        List<Station> stationsList = bikeData.getStationsList();
        Iterator<Station> iter = stationsList.iterator();
        while(iter.hasNext()) {
            Station station = iter.next();

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bike);


            MyItem item = new MyItem(icon, station.lat, station.lon, station.name, "Bikes: " + station.num_bikes_available + " | Spots: " + station.num_docks_available);
            mClusterManager.addItem(item);

            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(station.lat, station.lon);
            //mMap.addMarker(new MarkerOptions().position(sydney).title(station.name).snippet("Bikes: " + station.num_bikes_available+"\n Spots: " + station.num_docks_available));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }
    }







}
