package com.example.hritik.falcon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hritik.falcon.Common.Common;
import com.example.hritik.falcon.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.edtNewEmail)EditText edtNewEmail;
    @BindView(R.id.edtNewFirstName)EditText edtNewFirstName;
    @BindView(R.id.edtNewLastName)EditText edtNewLastName;
    @BindView(R.id.edtNewPassword)EditText edtNewPassword;
    @BindView(R.id.edtNewUserName)EditText edtNewUserName;


    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;
    Button edtCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        edtCreateAccount=findViewById(R.id.edtCreateAccount);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");

        edtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog=new ProgressDialog(SignupActivity.this);
                dialog.show();
                String temp_email=edtNewEmail.getText().toString();
                String temp_password=edtNewPassword.getText().toString();
                createAccount(temp_email,temp_password);
                dialog.dismiss();
            }
        });



    }

    private void createAccount(final String temp_email, final String temp_password) {
        auth.createUserWithEmailAndPassword(temp_email,temp_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    StringBuilder builder=new StringBuilder(temp_email);

                    User current_User=new User(temp_email,edtNewFirstName.getText().toString(),edtNewLastName.getText().toString(),
                                                temp_password,edtNewUserName.getText().toString());



                    users.child(String.valueOf(builder.substring(0,builder.indexOf("@")))).setValue(current_User);

                    saveUserDetail(temp_email);

                    Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this,LandingActivity.class));
                    finish();
                }else {
                    Toast.makeText(SignupActivity.this, "Can't create", Toast.LENGTH_SHORT).show();
                }




            }
        });


    }

    private void saveUserDetail(String temp_email) {
        final StringBuilder builder=new StringBuilder(temp_email);
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
}
