package com.example.hritik.falcon;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hritik.falcon.Adapter.AlertAdapter;
import com.example.hritik.falcon.Common.Common;
import com.example.hritik.falcon.Model.Alert;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AlertActivity extends AppCompatActivity {

    TextView addAlert;
    FloatingActionButton fab;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ImageView imageView;
    EditText camera_caption;
    private static final String TAG = "AlertActivity";
    public final int requestCode = 21;
    private FirebaseDatabase database;
    private static final int PICK_IMAGE_REQUEST = 234;
    private DatabaseReference alerts;
    private FirebaseStorage storage;
    int val;
    View view;
    private Uri filePath;
    private StorageReference storageReference;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.recyclerView);
        alerts=database.getReference("Alerts");
        addAlert = findViewById(R.id.addAlert);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("alertImages");
        fab = findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Alert");
        toolbar.setTitleTextColor(Color.WHITE);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AlertActivity.this));
        AlertAdapter alertAdapter=new AlertAdapter(this,Common.personalFeedsCaption,Common.personalFeedsImages);
        recyclerView.setAdapter(alertAdapter);
        alertAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertPopup();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Common.personalFeedsImages.clear();
        Common.personalFeedsCaption.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAlertPopup() {

        builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
         view=LayoutInflater.from(AlertActivity.this).inflate(R.layout.create_alert_layout,null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        imageView=view.findViewById(R.id.camera_image);
        Button click_image_button=view.findViewById(R.id.click_image);
        camera_caption=view.findViewById(R.id.camera_caption);
        TextView latitude=view.findViewById(R.id.latitude);
        TextView lngitude=view.findViewById(R.id.logitude);
        Button btn_upload=view.findViewById(R.id.btn_upload);

        // clicking image

        click_image_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                }
        );

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadFile();


            }
        });




    }

    private void uploadFile() {

        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        progressDialog.setCancelable(false);

        alerts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                val = 1 + Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                Log.d(TAG, "onDataChange: "+val);
                Log.d(TAG, "onDataChange: "+filePath);
                storageReference.child(String.valueOf("alertNo-0"+val)).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();
                        Toast.makeText(view.getContext(), "File Uploaded ", Toast.LENGTH_SHORT).show();

                        storageReference.child(String.valueOf("alertNo-0"+val)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                saveAlertToDB(uri);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.getMessage());
                                Toast.makeText(AlertActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void saveAlertToDB(final Uri uri) {
        alerts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Alert alert=new Alert(uri.toString(),Common.currentUser.getUserName(),"34.12","90.01",camera_caption.getText().toString());
                alerts.child(String.valueOf("0" + val)).setValue(alert);
                Toast.makeText(view.getContext(), "Alert Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AlertActivity.this, "Failed !!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
