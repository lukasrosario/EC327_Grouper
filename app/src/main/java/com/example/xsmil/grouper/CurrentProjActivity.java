package com.example.xsmil.grouper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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