package com.tytanapps.citibike;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class OwnRendring extends DefaultClusterRenderer<MyItem> {

    public OwnRendring(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }


    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

        markerOptions.icon(item.icon);
        markerOptions.snippet(item.snippet);
        markerOptions.title(item.title);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}