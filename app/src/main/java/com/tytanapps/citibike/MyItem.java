package com.tytanapps.citibike;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    BitmapDescriptor icon;
    String title;
    String snippet;
    BikeData bikeData;


    public MyItem(BitmapDescriptor icon, Double lat , Double lon, String title ,String snippet)
    {
        mPosition = new LatLng(lat,lon);
        this.icon = icon;
        this.title = title;
        this.snippet = snippet;

    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String toString() {
        return "MYITEM";
    }
}