package com.example.hritik.falcon;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.hritik.falcon.Common.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        MarkerOptions markerOptions=new MarkerOptions();
        CircleOptions circleOptions=new CircleOptions();
        circleOptions.center(new LatLng(30.97,76.53));
        circleOptions.radius(5000);
        circleOptions.strokeWidth(3.6f);
        circleOptions.fillColor(Color.parseColor("#4F0e0d0e"));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.title("Affected Region");
        mMap.addCircle(circleOptions);
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(30.97,76.53)));



//        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Common.lat, Common.longi);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.fire))
                .title("Affected Region"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,12.0f));
     //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,2.0f));
    }
}
