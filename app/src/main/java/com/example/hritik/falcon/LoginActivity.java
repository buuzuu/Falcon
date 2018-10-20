package com.example.hritik.falcon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hritik.falcon.Common.Common;
import com.example.hritik.falcon.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.userEmail)EditText userEmail;
    @BindView(R.id.userPassword)EditText userPassword;
    @BindView(R.id.btn_log_in)Button sign_in;
    @BindView(R.id.btn_log_up)Button sign_up;
    DatabaseReference users;
    FirebaseAuth auth;
    private static final String TAG = "LoginActivity";
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        auth=FirebaseAuth.getInstance();
        users=FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // checking if user is null
        if (firebaseUser != null) {
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // checking if user is null
        if (firebaseUser != null) {
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.btn_log_in)
    public void sign_in_user(){
        auth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    saveUserDetail();


                    startActivity(new Intent(LoginActivity.this,LandingActivity.class));
                    finish();

                }else {
                    Log.d(TAG, "onComplete: "+userEmail.getText().toString()+"--"+userPassword.getText().toString());
                    Toast.makeText(LoginActivity.this, "Unable to sign in.", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void saveUserDetail() {
        final StringBuilder builder=new StringBuilder(userEmail.getText().toString());
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Common.currentUser=dataSnapshot.child(String.valueOf(builder.substring(0,builder.indexOf("@")))).getValue(User.class);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.btn_log_up)
    public void sign_up_user(){

        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
    }
}
