package com.micompany.culturapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class Anadir extends AppCompatActivity {

    public static Anadir anadir1;

    Marker marker;
    private ImageButton flecha;
    private FloatingActionButton alante;

    private LocationServices locationServices;
    private MapView mapView = null;
    private MapboxMap mapboxMap = null;


    Double longitud, latitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_anadir1);

        anadir1 = this;

        mapView = (MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {
                mapboxMap = map;
                IconFactory iconFactory = IconFactory.getInstance(Anadir.this);
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.markericon, null);
                final Icon icon = iconFactory.fromDrawable( drawable, 100, 108);
                locationServices = LocationServices.getLocationServices(Anadir.this);
                final Location lastLocation = locationServices.getLastLocation();
                final MarkerViewOptions markerViewOptions = new MarkerViewOptions()
                        .position(new LatLng(lastLocation));

                mapboxMap.addMarker(markerViewOptions
                                    .icon(icon));
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

        //Añadir accion: Finalizar actividad al pulsar atras
        flecha = (ImageButton) findViewById(R.id.flecha);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });

        //Añadir accion: Ir a Añadir 2 al pulsar siguiente
        alante = (FloatingActionButton) findViewById(R.id.forward);
        alanteClickListener();

    }

    //Finalizar actitvidad
    private void killActivity() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Si el usuario vuelve de una vista posterior se vuelve a hablitar el ClickListener de flecha.
        alanteClickListener();
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

    private void alanteClickListener() {

        alante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se quita el onClickListener para que el usuario no pueda sobrecargar las peticiones a avanzar.
                alante.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                latitud=marker.getPosition().getLatitude();
                longitud=marker.getPosition().getLongitude();

                Toast.makeText(
                        getBaseContext(),
                        "Comprobando si existen marcadores en esta ubicación",
                        Toast.LENGTH_SHORT).show();

                ParseQuery<Marcador> query = ParseQuery.getQuery(Marcador.class)
                        .whereEqualTo("longitud", longitud).whereEqualTo("latitud", latitud);
                ;
                query.findInBackground(new FindCallback<Marcador>() {
                    public void done(List<Marcador> objects, ParseException e) {
                        if (e == null) {
                            if (objects.isEmpty()){
                                Intent intent = new Intent(Anadir.this, Anadir2.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(
                                        getBaseContext(),
                                        "Ya existe un marcador en esa ubicación" + objects.toString(),
                                        Toast.LENGTH_SHORT).show();

                                alanteClickListener();
                            }
                        } else {
                            Toast.makeText(
                                    getBaseContext(),
                                    "Error en la conexion",
                                    Toast.LENGTH_SHORT).show();

                            alanteClickListener();
                        }
                    }
                });
            }
        });
    }

}
