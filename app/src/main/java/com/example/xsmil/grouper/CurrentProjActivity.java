package com.example.xsmil.grouper;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;

import models.projectGroup;
import models.user;

public class CurrentProjActivity extends AppCompatActivity {

    private Button creategroupbutton;
    private TextView textViewUserEmail;
    private TextView textViewUserFull;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private DatabaseReference dR;
    private ListView listView;
    private ArrayList <String> groupName = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_proj);

        // Opens Create Group Activity.
        creategroupbutton = (Button) findViewById(R.id.CreateGroupBtn);
        creategroupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreateGroup();
            }
        });

        // If the user is not signed in, it opens LoginActivity.
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        // Displays the users email in TextView using FireBase Authentication.
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewUserEmail = (TextView) findViewById(R.id.TextViewUserEmail);
        textViewUserEmail.setText(user.getEmail());


        textViewUserFull = (TextView) findViewById(R.id.textViewUserFull);


        // Retrieves first and last name from user class in FireBase Database
        // Concatenates the first and last name and sets the textView to display the Full Name.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user userInfo = dataSnapshot.getValue(user.class);
                System.out.println(userInfo.firstName);
                String fullName = userInfo.firstName + " " + userInfo.lastName;
                textViewUserFull.setText(fullName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void openActivityCreateGroup() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }
}

/*public class CurrentProjActivity extends AppCompatActivity implements View.OnClickListener{

    private Button creategroupbutton;
    private TextView textViewUserEmail;


    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private EditText editTextName;
    private Button buttonSave;

    private TextView textViewUserFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_proj);

        creategroupbutton = (Button) findViewById(R.id.CreateGroupBtn);
        creategroupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreateGroup();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.TextViewUserEmail);
        textViewUserEmail.setText(user.getEmail());


        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        textViewUserFull = (TextView) findViewById(R.id.textViewUserFull);
        textViewUserFull.setText("hello");




        databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInfo = dataSnapshot.getValue(UserInformation.class);
                System.out.println(userInfo.name);
                textViewUserFull.setText(userInfo.name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });





    }

    private void openActivityCreateGroup() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }

    private void saveUserInformation(){
        String name = editTextName.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("users").child(user.getUid()).setValue(userInformation);

        /*user newUser = new user(user.getUid(), user.getEmail(),
        databaseReference.child("users").child(user.getUid()).setValue(user);
         */
/*
        Toast.makeText(this,"Information Saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSave)
            saveUserInformation();

    }
}
*/