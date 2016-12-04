package com.tytanapps.citibike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Iterator;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private BikeData bikeData;
    private GoogleMap mMap;

    boolean onlyAvailableBikes = false;
    boolean onlyAvailableSpots = false;


    // Point the map's listeners at the listeners implemented by the cluster
    // manager.
    CameraPosition mPreviousCameraPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(item.isCheckable()) {
            item.setChecked(! item.isChecked());
        }


        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_bikes) {
            onlyAvailableBikes = !onlyAvailableBikes;
            addBikes();

            return true;
        }


        if (id == R.id.action_spots) {
            onlyAvailableSpots = !onlyAvailableSpots;
            addBikes();

            return true;
        }


        return super.onOptionsItemSelected(item);
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

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7590403,-74.0392714), 10));

        loadBikes();
    }

    private void loadBikes() {
        BikeTask bikeTask = new BikeTask(){
        @Override
        protected void onPostExecute(BikeData bikeData) {
            super.onPostExecute(bikeData);
            MapsActivity.this.bikeData = bikeData;
            setUpClusterer();
            addBikes();
        }
    };
        bikeTask.execute();

    }

    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);


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
    }

    private void addBikes() {
        mClusterManager.clearItems();
        mClusterManager.cluster();

        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(),mMap,mClusterManager));

        Log.d(TAG, "addBikes() called with: bikeData = [" + bikeData + "]");

        List<Station> stationsList = bikeData.getStationsList();
        Iterator<Station> iter = stationsList.iterator();
        while(iter.hasNext()) {
            Station station = iter.next();

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bike);
            MyItem item = new MyItem(icon, station.lat, station.lon, station.name, "Bikes: " + station.num_bikes_available + " | Spots: " + station.num_docks_available);

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //Toast.makeText(MapsActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();

                    Station selectedStation = getBikeDataFromLatLon(marker.getPosition());
                    //Toast.makeText(MapsActivity.this, "ID: "+selectedStation.station_id, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                    intent.putExtra("station", selectedStation);
                    startActivity(intent);

                }
            });

            if((!onlyAvailableBikes || station.num_bikes_available > 0)
                    && (!onlyAvailableSpots || station.num_docks_available > 0)) {

                mClusterManager.addItem(item);
            }



        }

        mClusterManager.cluster();
    }

    private Station getBikeDataFromLatLon(LatLng position) {
        List<Station> stations = bikeData.getStationsList();
        for(Station station : stations) {
            if(station.lat == position.latitude && station.lon == position.longitude)
                return station;
        }

        return null;
    }


}
