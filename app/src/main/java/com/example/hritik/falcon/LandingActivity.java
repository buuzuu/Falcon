package com.example.hritik.falcon;


import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hritik.falcon.Common.Common;
import com.example.hritik.falcon.Model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity implements LocationListener {

    @BindView(R.id.map)
    ImageView map;
    @BindView(R.id.alert)
    ImageView alert;
    @BindView(R.id.profile)
    ImageView profile;
    @BindView(R.id.feed)
    ImageView feed;
    @BindView(R.id.document)
    ImageView document;
    FirebaseDatabase database;
    DatabaseReference users;
    LocationManager locationManager;
    
    FirebaseAuth auth;


    private static final String TAG = "LandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        users = database.getReference("Users");
        ButterKnife.bind(this);

        Log.d(TAG, "onCreate: " + auth.getCurrentUser().getDisplayName());
        String email = auth.getCurrentUser().getEmail();
        Log.d(TAG, "onCreate: " + email);
        final StringBuilder builder = new StringBuilder(email);
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Common.currentUser = dataSnapshot.child(String.valueOf(builder.substring(0, builder.indexOf("@")))).getValue(User.class);
                    Log.d(TAG, "onDataChange: " + Common.currentUser.getEmail());

                } else {
                    Toast.makeText(LandingActivity.this, "Data Not Loaded.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Log.d(TAG, "onCreate: " + email);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(LandingActivity.this, ProfileActivity.class);
                profileIntent.putExtra("userEmail", Common.currentUser.getEmail());
                profileIntent.putExtra("userName", Common.currentUser.getFirstName());
                startActivity(profileIntent);


            }
        });


        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingActivity.this, AlertActivity.class));
            }
        });

        // get location

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        onLocationChanged(location);

    }

    public void onLocationChanged(Location location) {

        Log.d(TAG, "onLocationChanged: First Heloo!");
        double lat=location.getLatitude();
        double lon=location.getLongitude();
        Log.d(TAG, "onLocationChanged: Hello");
        Log.d(TAG, "onLocationChanged: "+lat+"---"+lon);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
