package com.example.xsmil.grouper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HasAuthActivity extends AppCompatActivity {
    private String value;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private String currentUser;
    private boolean hasAuthenticity = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        value = getIntent().getStringExtra("groupID");
        reference = FirebaseDatabase.getInstance().getReference("projectGroups").child(value).child("members");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getUid();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    if(currentUser.equals(dataSnapshot1.getKey().toString())){
                        Intent newActivity = new Intent(getApplicationContext(), ProjectActivity.class);
                        newActivity.putExtra("groupID",value);
                        startActivity(newActivity);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent newActvity = new Intent(getApplicationContext(), ProjectNoPermissionActivity.class); //change this
        newActvity.putExtra("groupID",value);
        startActivity(newActvity);
    }
}
