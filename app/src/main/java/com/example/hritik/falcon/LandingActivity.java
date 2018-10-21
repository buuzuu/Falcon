package com.example.hritik.falcon;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

    //For notification
    public static final String CHANNEL_ID = "250";
    public static final int notificationId = 24;
    private static final String TAG = "LandingActivity";
    int a=0;
    String s="";
    String ss="";
    String sss="";
    FirebaseDatabase database;
    DatabaseReference notification;





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

    DatabaseReference users,alerts;
    
    String usrName;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ActivityCompat.requestPermissions(LandingActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        database = FirebaseDatabase.getInstance();
        createNotificationChannel();
        auth = FirebaseAuth.getInstance();
        alerts=database.getReference("Alerts");
        users = database.getReference("Users");
        notification=database.getReference("Notification");
        ButterKnife.bind(this);

        //setting up notification

        notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                s= String.valueOf(dataSnapshot.child("isAllowed").getValue());
                ss=String.valueOf(dataSnapshot.child("title").getValue());
                sss=String.valueOf(dataSnapshot.child("message").getValue());

                if (s.equals("true")){
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(LandingActivity.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.falcon_text)
                            .setContentTitle(ss)
                            .setContentText(sss)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(LandingActivity.this);

                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(notificationId, mBuilder.build());



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        
        //Get GPS
        Log.d(TAG, "onCreate: Gps Started");
        GPSTracker tracker=new GPSTracker(getApplicationContext());
        Location location=tracker.getLocation();
        if (location!=null){
             Common.lat= (float) location.getLatitude();
             Common.longi= (float) location.getLongitude();
            Log.d(TAG, "onCreate: "+"Latitude is--"+Common.lat);
            Log.d(TAG, "onCreate: "+"Longitude is--"+Common.longi);
            Log.d(TAG, "onCreate: In GPS");


        }
        Log.d(TAG, "onCreate: GPS eNDEDE");
        
        
        
        

        String email = auth.getCurrentUser().getEmail();
        Log.d(TAG, "onCreate: " + email);
        final StringBuilder builder = new StringBuilder(email);
        usrName=String.valueOf(builder.substring(0,builder.indexOf("@")));
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Common.currentUser = dataSnapshot.child(String.valueOf(builder.substring(0, builder.indexOf("@")))).getValue(User.class);




                } else {
                    Toast.makeText(LandingActivity.this, "Data Not Loaded.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // opening map
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingActivity.this,MapActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(LandingActivity.this, ProfileActivity.class);
                profileIntent.putExtra("userEmail", Common.currentUser.getEmail());
                profileIntent.putExtra("userName", Common.currentUser.getFirstName());
                startActivity(profileIntent);


            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent documentact=new Intent(LandingActivity.this,DocumentActivity.class);
                startActivity(documentact);
            }
        });


        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent alertIntent=new Intent(LandingActivity.this,AlertActivity.class);

                startActivity(alertIntent);


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
        Log.d(TAG, "onCreate-----: "+usrName);
        alerts.orderByChild("userId").equalTo(usrName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();

                while (iterable.hasNext()) {
                    DataSnapshot tempItem = iterable.next();
                    Common.personalFeedsCaption.add(tempItem.child("caption").getValue().toString());
                    Common.personalFeedsImages.add(tempItem.child("imageUrl").getValue().toString());




                }
                Toast.makeText(LandingActivity.this, "Data Loaded", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onDataChange: "+Common.personalFeedsCaption.size());
                for (int i=0;i<Common.personalFeedsCaption.size();i++){
                    Log.d(TAG, "onDataChanged: "+Common.personalFeedsCaption.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        
        //get location
        
        

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onCreate: Gps Started");
        GPSTracker tracker=new GPSTracker(getApplicationContext());
        Location location=tracker.getLocation();
        if (location!=null){
            double lat=location.getLatitude();
            double longi=location.getLongitude();
            Log.d(TAG, "onCreate: "+"Latitude is--"+lat);
            Log.d(TAG, "onCreate: "+"Longitude is--"+longi);
            Log.d(TAG, "onCreate: In GPS");
        }
        Log.d(TAG, "onCreate: GPS eNDEDE");



//        alerts.orderByChild("userId").equalTo(usrName).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
//
//                while (iterable.hasNext()) {
//                    DataSnapshot tempItem = iterable.next();
//                    Common.personalFeedsCaption.add(tempItem.child("caption").getValue().toString());
//                    Common.personalFeedsImages.add(tempItem.child("imageUrl").getValue().toString());
//
//
//
//
//                }
//                Toast.makeText(LandingActivity.this, "Got Alerts", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onDataChange: "+Common.personalFeedsCaption.size());
//                for (int i=0;i<Common.personalFeedsCaption.size();i++){
//                    Log.d(TAG, "onDataChanged: "+Common.personalFeedsCaption.get(i));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Common.personalFeedsCaption.clear();
        Common.personalFeedsImages.clear();
        Common.feedsCaption.clear();
        Common.feedsImages.clear();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
