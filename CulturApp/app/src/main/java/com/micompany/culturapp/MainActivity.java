package com.micompany.culturapp;


        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Color;
        import android.location.Location;

        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.view.View;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.BaseAdapter;



        import com.mapbox.mapboxsdk.MapboxAccountManager;
        import com.mapbox.mapboxsdk.camera.CameraPosition;
        import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
        import com.mapbox.mapboxsdk.geometry.LatLng;
        import com.mapbox.mapboxsdk.location.LocationListener;
        import com.mapbox.mapboxsdk.location.LocationServices;
        import com.mapbox.mapboxsdk.maps.MapView;
        import com.mapbox.mapboxsdk.maps.MapboxMap;
        import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_LOCATION = 0;

    private MapView mapView = null;
    private MapboxMap mapboxMap = null;
    private LocationServices locationServices;
    private FloatingActionButton floatingActionButton;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    ArrayList<NavItem> mNavItemsFoot = new ArrayList<NavItem>();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    ImageButton burger;
    ImageView logo;


    /*
    * Called when a particular item from the navigation drawer
    * is selected.
    * */
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

        locationServices = LocationServices.getLocationServices(MainActivity.this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        burger = (ImageButton) findViewById(R.id.burgerMenu);
        logo = (ImageView) findViewById(R.id.logo);

        burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });


        mapView = (MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {
                mapboxMap = map;
                mapboxMap.setOnScrollListener(new MapboxMap.OnScrollListener() {
                    @Override
                    public void onScroll() {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                });
                mapboxMap.getMyLocationViewSettings().setPadding(500, 500, 500, 500);
                mapboxMap.getMyLocationViewSettings().setForegroundTintColor(Color.parseColor("#0EB179"));
                mapboxMap.getMyLocationViewSettings().setAccuracyAlpha(0);
                toggleGps(!mapboxMap.isMyLocationEnabled());
                anadirMarcadores();//TODO
                floatingActionButton.setVisibility(View.GONE);
            }
        });


        floatingActionButton = (FloatingActionButton) findViewById(R.id.location_toggle_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapboxMap != null) {
                    toggleGps(!mapboxMap.isMyLocationEnabled());
                    floatingActionButton.setVisibility(View.GONE);
                }
            }
        });
        //floatingActionButton.setVisibility(View.GONE);
        mNavItems.add(new NavItem("Puntuación"/*, "Meetup destination", R.drawable.ic_action_home*/));
        mNavItems.add(new NavItem("Añadir marcador"/*, "Change your preferences", R.drawable.ic_action_settings*/));
        mNavItemsFoot.add(new NavItem("Ajustes"/*, "Get to know about us", R.drawable.ic_action_about*/));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerList = (ListView) findViewById(R.id.navListFoot);
        DrawerListAdapter adapterFoot = new DrawerListAdapter(this, mNavItemsFoot);
        mDrawerList.setAdapter(adapterFoot);

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

    private void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Check if user has granted location permission
            if (!locationServices.areLocationPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            } else {
                enableLocation(true);
            }
        } else {
            enableLocation(false);
        }
    }

    private void enableLocation(boolean enabled) {
        Location lastLocation;
        if (enabled) {
            lastLocation = locationServices.getLastLocation();
            // If we have the last location of the user, we can move the camera to that position.
            if (lastLocation != null) {
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));
            }

            locationServices.addLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 16));
                        locationServices.removeLocationListener(this);
                    }
                }
            });
        }
        else
            lastLocation = locationServices.getLastLocation();
            if (lastLocation != null) {
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));
            }
        // Enable or disable the location layer on the map
        mapboxMap.setMyLocationEnabled(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation(true);
            }
        }
    }

    private void anadirMarcadores() {
        //TODO
    }
}
