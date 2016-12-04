package com.tytanapps.citibike;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // To retrieve object in second Activity
        Station station = (Station) getIntent().getSerializableExtra("station");

        getSupportActionBar().setTitle(station.name);

        TextView nameTextView = (TextView) findViewById(R.id.name_textview);
        nameTextView.setText(station.name);

        TextView bikesTextView = (TextView) findViewById(R.id.bikes_available_textview);
        bikesTextView.setText("Bikes available: " + station.num_bikes_available);

        TextView docksAvailableTextView = (TextView) findViewById(R.id.docks_available_textview);
        docksAvailableTextView.setText("Docks available: " + station.num_docks_available);

        ImageView mapImageView = (ImageView) findViewById(R.id.map_imageview);
        final String coord = station.lat + "," + station.lon;
        //Picasso.with(this).load("https://maps.googleapis.com/maps/api/staticmap?center=" + coord + "&zoom=15&size=600x400&markers=color:blue|" + coord + "&key=AIzaSyAikxraAlFyM0TxoIr-3QGDY008vFnOscg").into(mapImageView);


        Picasso.with(this).load("https://maps.googleapis.com/maps/api/streetview?size=800x800&location="+coord+"&fov=110&pitch=1&key=AIzaSyAikxraAlFyM0TxoIr-3QGDY008vFnOscg").into(mapImageView);

        Button directionsButton = (Button) findViewById(R.id.directions_button);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr="+coord));
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

}
