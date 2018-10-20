package com.example.hritik.falcon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_Email)TextView user_emailId;
    @BindView(R.id.userName)TextView user_Name;
    @BindView(R.id.log_out)Button log_out;
    private static final String TAG = "ProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        String user_Email=getIntent().getStringExtra("userEmail");
        String userName=getIntent().getStringExtra("userName");
        Log.d(TAG, "onCreate: "+user_Email);
        user_emailId.setText(user_Email);
        user_Name.setText(userName);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finish();
            }
        });


    }
}
