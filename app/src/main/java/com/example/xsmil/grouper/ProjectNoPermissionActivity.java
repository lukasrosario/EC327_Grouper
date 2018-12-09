package com.example.xsmil.grouper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import models.projectGroup;

public class ProjectNoPermissionActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference databaseReference;
    private TextView projectName;
    private TextView Class;
    private TextView deadline;
    private TextView description;
    private Button join;
    private Button back;
    private String val;
    private FirebaseAuth mAuth;
    private String currentUser;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page2);
        projectName = (TextView) findViewById(R.id.etProjectName);
        Class = (TextView) findViewById(R.id.etCourse);
        deadline = (TextView) findViewById(R.id.etDeadline);
        description = (TextView) findViewById(R.id.etDescription);
        join = (Button) findViewById(R.id.btJoin);
        back = (Button) findViewById(R.id.btBack);
        val = getIntent().getStringExtra("groupID");
        databaseReference = FirebaseDatabase.getInstance().getReference("projectGroups").child(val);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getUid().trim();
        join.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("projectTitle").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name  = dataSnapshot.getValue(String.class);
                projectName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("projectDeadline").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deadline.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("course").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Class.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("description").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //@Override
    public void onClick(View view) {
        if (view == join){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    projectGroup Project = dataSnapshot.getValue(projectGroup.class);
                    int length  = Project.getCurrentNumMembers();
                    if(length + 1 > Project.maxCapacity){
                        Toast.makeText(ProjectNoPermissionActivity.this, "Sorry this project has reached its maximum capacity", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    else {
                        Project.addMember(currentUser);
                        Map<String, Object> updatedGroupValues = Project.toMap();
                        databaseReference.updateChildren(updatedGroupValues);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            Intent newActivity = new Intent(this,ProjectActivity.class);
            newActivity.putExtra("groupID",val);
            startActivity(newActivity);
        }
        if (view == back){
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
