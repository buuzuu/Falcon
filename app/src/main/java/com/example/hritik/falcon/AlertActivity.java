package com.example.hritik.falcon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class AlertActivity extends AppCompatActivity {

    TextView addAlert;
    FloatingActionButton fab;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ImageView imageView;
    private static final String TAG = "AlertActivity";
    public final int requestCode = 21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);

        addAlert = findViewById(R.id.addAlert);
        fab = findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Alert");
        toolbar.setTitleTextColor(Color.WHITE);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertPopup();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode==requestCode&&resultCode==RESULT_OK){
            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }


    }

    private void createAlertPopup() {

        builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        View view=LayoutInflater.from(AlertActivity.this).inflate(R.layout.create_alert_layout,null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        imageView=view.findViewById(R.id.camera_image);
        Button click_image_button=view.findViewById(R.id.click_image);
        EditText camera_caption=view.findViewById(R.id.camera_caption);
        TextView latitude=view.findViewById(R.id.latitude);
        TextView lngitude=view.findViewById(R.id.logitude);
        Button btn_upload=view.findViewById(R.id.btn_upload);

        // clicking image

        click_image_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureImage,requestCode);
                    }
                }
        );




    }





}
