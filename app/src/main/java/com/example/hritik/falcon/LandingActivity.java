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

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity  {

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
    DatabaseReference users,alerts;

    String usrName;
    FirebaseAuth auth;


    private static final String TAG = "LandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        alerts=database.getReference("Alerts");
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
                    usrName=Common.currentUser.getUserName();
                    Log.d(TAG, "onDataChange:............"+usrName);
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

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingActivity.this,FeedsActivity.class));
            }
        });
        // get all feeds

        alerts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                Log.d(TAG, "onDataChange: Total users " + dataSnapshot.getChildrenCount());
                while (iterable.hasNext()) {


                    DataSnapshot tempItem = iterable.next();
                    Common.feedsCaption.add(tempItem.child("caption").getValue().toString());
                    Common.feedsImages.add(tempItem.child("imageUrl").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // get personal alerts
     //   String usrName=Common.currentUser.getUserName();

      //  Log.d(TAG, "onCreate........................: "+usrName);
        alerts.orderByChild("userId").equalTo(usrName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Common.personalFeedsCaption.add(dataSnapshot1.child("caption").getValue().toString());
                    Common.personalFeedsImages.add(dataSnapshot1.child("imageUrl").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
