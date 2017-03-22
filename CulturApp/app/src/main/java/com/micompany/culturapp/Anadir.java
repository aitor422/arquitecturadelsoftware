package com.micompany.culturapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class Anadir extends AppCompatActivity {
    private ImageButton flecha;
    Marker marker;
    private FloatingActionButton alante;
    public static Anadir anadir1;
    private LocationServices locationServices;

    private MapView mapView = null;
    private MapboxMap mapboxMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapboxAccountManager.start(this, getString(R.string.access_token));
        super.onCreate(savedInstanceState);
        anadir1 = this;
        setContentView(R.layout.activity_anadir1);
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {
                mapboxMap = map;
                locationServices = LocationServices.getLocationServices(Anadir.this);
                final Location lastLocation = locationServices.getLastLocation();
                final MarkerViewOptions markerViewOptions = new MarkerViewOptions()
                        .position(new LatLng(lastLocation));

                mapboxMap.addMarker(markerViewOptions);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));
                marker = mapboxMap.getMarkers().get(0);
                mapboxMap.setOnScrollListener(new MapboxMap.OnScrollListener() {
                    @Override
                    public void onScroll() {
                        marker.setPosition(mapboxMap.getCameraPosition().target);
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getCameraPosition().target), 16));
                    }
                });

            }
        });
        flecha = (ImageButton) findViewById(R.id.flecha);
        alante = (FloatingActionButton) findViewById(R.id.forward);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });

        alante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Anadir.this, Anadir2.class);
                Double l1=marker.getPosition().getLatitude();
                Double l2=marker.getPosition().getLongitude();
                String coordl1 = l1.toString();
                String coordl2 = l2.toString();
                intent.putExtra("LATITUD", coordl1);
                intent.putExtra("LONGITUD", coordl2);
                startActivity(intent);
            }
        });

    }

    private void killActivity() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}
