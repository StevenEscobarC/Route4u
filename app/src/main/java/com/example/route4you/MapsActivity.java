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

/**
 * Clase que contiene los métodos requeridos para la visualización del
 * mapa proveniente de la API de google maps
 *
 * @author Legions
 * @version 1.1
 */
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

    /**
     * Verifica que se tengan los permisos del usuario referente a la ubicación del dispositivo móvil,
     * de tenerlos se envía un código de respuesta correcto para que se proceda a mostrar en el mapa
     */
    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    /**
     * Al objeto de tipo GoogleMap se le activa la ubicación del dispositivo
     * además de poner en el mapa la ubicacion obteniendo la latitud y longitud obtenida
     * de la localización
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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


