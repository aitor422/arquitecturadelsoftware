
package com.micompany.culturapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_LOCATION = 0;

    private MapView mapView = null;
    private MapboxMap mapboxMap = null;
    private LocationServices locationServices; //gestiona las localizaciones

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    ArrayList<NavItem> mNavItemsFoot = new ArrayList<NavItem>();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    private FloatingActionButton floatingActionButton;
    ImageButton burger;

    /* Llamado cuando una opcion del menu es seleccionada*/
    private void selectItemFromDrawer(int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, Puntuacion.class);
                break;
            default:
                intent = new Intent(this, Anadir.class);
                break;

        }
        startActivity(intent);
    }

    /*Llamado cuando la opcion ajustes del menu es seleccionada*/
    private void selectItemFromFooter(int position) {
        Intent intent;
        switch (position) {
            default:
                intent = new Intent(this, Ajustes.class);
                break;

        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("culturapp")
                .clientKey("empty")
                .server("https://culturapp.herokuapp.com/parse/")
                .build());

        mapView = (MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        locationServices = LocationServices.getLocationServices(MainActivity.this);

        //Asignar accion al abrir la hamburguesa
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        burger = (ImageButton) findViewById(R.id.burgerMenu);
        burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {
                mapboxMap = map;
                configureMap();
            }
        });





        //Asignar accion al seleccionar icono localizacion
        floatingActionButton = (FloatingActionButton) findViewById(R.id.location_toggle_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapboxMap != null) {
                    toggleGps(!mapboxMap.isMyLocationEnabled());
                    //floatingActionButton.setVisibility(View.GONE);
                }
            }
        });

        //Añadir opciones a los arrays de NavItem (para el menu)
        mNavItems.add(new NavItem("Puntuación"/*, "Meetup destination", R.drawable.ic_action_home*/));
        mNavItems.add(new NavItem("Añadir marcador"/*, "Change your preferences", R.drawable.ic_action_settings*/));
        mNavItemsFoot.add(new NavItem("Ajustes"/*, "Get to know about us", R.drawable.ic_action_about*/));

        //Añadir array al menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        //Añadir accion al seleccionar una opcion del menu
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        //Añadir array al menu (abajo)
        mDrawerList = (ListView) findViewById(R.id.navListFoot);
        DrawerListAdapter adapterFoot = new DrawerListAdapter(this, mNavItemsFoot);
        mDrawerList.setAdapter(adapterFoot);
        //Añadir accion al seleccionar una opcion del menu abajo (ajustes)
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromFooter(position);
            }
        });

    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        //Cerrar menu al pausar
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation(true);
            }
        }
    }



    private void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Check if user has granted location permission
            if (!locationServices.areLocationPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            }
            else
                enableLocation(true);
        } else {
            enableLocation(false);
        }
    }

    private void enableLocation(boolean enabled) {
        Location lastLocation;
        if (enabled) {
            // If we have the last location of the user, we can move the camera to that position.
            locationServices.addLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 16));
                    }
                }
            });
        }
        lastLocation =  locationServices.getLastLocation();
        // Enable or disable the location layer on the map
        if (lastLocation != null) {
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));
            floatingActionButton.setVisibility(View.GONE);
        }
        mapboxMap.setMyLocationEnabled(true);
    }

    // He movido aquí todas las configuraciones sobre el mapa por claridad.
    private void configureMap() {
        mapboxMap.getMyLocationViewSettings().setPadding(500, 500, 500, 500);
        mapboxMap.getMyLocationViewSettings().setForegroundTintColor(Color.parseColor("#0EB179"));
        mapboxMap.getMyLocationViewSettings().setAccuracyAlpha(0);
        mapboxMap.setOnScrollListener(new MapboxMap.OnScrollListener() {
            @Override
            public void onScroll() {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });
        toggleGps(!mapboxMap.isMyLocationEnabled());

        //Obtener marcadores
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Marcador");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject marcador : objects){
                        double longitud = (double) marcador.get("longitud");
                        double latitud = (double) marcador.get("latitud");
                        MarkerViewOptions markerViewOptions = new MarkerViewOptions()
                                .position(new LatLng( latitud, longitud ) );
                        mapboxMap.addMarker(markerViewOptions);
                    }
                } else {
                    Toast.makeText(
                            getBaseContext(),
                            "Error al obtener marcadores",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                double latitud=marker.getPosition().getLatitude();
                double longitud=marker.getPosition().getLongitude();
                //Obtener marcadores
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Marcador")
                        .whereEqualTo("longitud", longitud).whereEqualTo("latitud", latitud);;
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            ParseObject marcador = objects.get(0);
                            Intent intent = new Intent(MainActivity.this, Contestar.class);
                            intent.putExtra("PREGUNTA",(String) marcador.get("pregunta"));
                            intent.putExtra("NOMBRE",(String) marcador.get("nombre"));
                            intent.putExtra("OPC_CORRECTA",(int) marcador.get("opc_correcta"));
                            intent.putExtra("NUM_OPCIONES",(int) marcador.get("num_opciones"));
                            intent.putExtra("LATITUD",(double) marcador.get("latitud"));
                            intent.putExtra("LONGITUD",(double) marcador.get("longitud"));
                            String encoded = (String) marcador.get("imagen");
                            byte [] encodeByte = Base64.decode(encoded,Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            intent.putExtra("IMAGEN",bitmap);
                            startActivity(intent);
                        } else {
                            Toast.makeText(
                                    getBaseContext(),
                                    "Error al obtener marcadores",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return true;
            }
        });

    }






/******************** CLASES INTERNAS ************************/

    /* CLASE NavItem
     *  Opciones del menu
     */
    class NavItem {
        String mTitle;
        //String mSubtitle;
        //int mIcon;

        public NavItem(String title/*, String subtitle, int icon*/) {
            mTitle = title;
            //mSubtitle = subtitle;
            //mIcon = icon;
        }
    }

    /* CLASE DrawerListAdapter
     *  Para añadir lista opciones al menu
     */
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            //TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            //subtitleView.setText( mNavItems.get(position).mSubtitle );
            //iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }

}