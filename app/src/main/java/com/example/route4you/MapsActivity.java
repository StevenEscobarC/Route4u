package com.example.route4you;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng miUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocalizacion();
    }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng villapilar = new LatLng(5.087557, -75.527373);
        LatLng sultana = new LatLng(5.061053, -75.469550);
        LatLng liborio = new LatLng(5.075687, -75.519110);
        LatLng villamaria = new LatLng(5.047128, -75.514777);
        LatLng veracruz = new LatLng(5.094939, -75.539474);
        LatLng tablazo = new LatLng(5.020009, -75.540466);
        LatLng cumbre = new LatLng(5.068574, -75.472216);
        LatLng fatima = new LatLng(5.050482, -75.499001);


        mMap.addMarker(new MarkerOptions().position(villapilar).title("Socobuses villapilar"));
        mMap.addMarker(new MarkerOptions().position(sultana).title("Socobuses sultana"));
        mMap.addMarker(new MarkerOptions().position(liborio).title("Serviturismo liborio"));
        mMap.addMarker(new MarkerOptions().position(villamaria).title("Serviturismo villamaria"));
        mMap.addMarker(new MarkerOptions().position(veracruz).title("Unitrans veracruz"));
        mMap.addMarker(new MarkerOptions().position(tablazo).title("Unitrans tablazo"));
        mMap.addMarker(new MarkerOptions().position(cumbre).title("Unitrans cumbre"));
        mMap.addMarker(new MarkerOptions().position(fatima).title("Unitrans fatima"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Consider calling
            //  ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(miUbicacion).title("ubicacion actual"));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(16)
                        .bearing(90)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            /**
             * @deprecated
             * */
            @Deprecated
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Cuando se necesite
            }

            @Override
            public void onProviderEnabled(String provider) {
                //Cuando se necesite
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Cuando se necesite
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }
}


