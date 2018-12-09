package com.example.xsmil.grouper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.xsmil.grouper.UserList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.projectGroup;
import models.user;

public class ProjectActivity extends AppCompatActivity implements View.OnClickListener{
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencemembers;
    DatabaseReference databaseReferenceUsers;
    private TextView projectName;
    private TextView Class;
    private TextView deadline;
    private TextView description;
    private Button leave;
    private Button back;
    private String val;
    private FirebaseAuth mAuth;
    private List<user> userList;
    private List<String> userID;
    private String currentUser;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page);

        projectName = (TextView) findViewById(R.id.etProjectName);
        Class = (TextView) findViewById(R.id.etCourse);
        deadline = (TextView) findViewById(R.id.etDeadline);
        description = (TextView) findViewById(R.id.etDescription);

        leave = (Button) findViewById(R.id.btLeave);
        back = (Button) findViewById(R.id.btBack);
        leave.setOnClickListener(this);
        back.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.lView);
        val = getIntent().getStringExtra("groupID").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("projectGroups").child(val);
        databaseReferencemembers = FirebaseDatabase.getInstance().getReference("projectGroups").child(val).child("members");
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");

        userList = new ArrayList<user>();

        userID = new ArrayList<String>();

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getUid().trim();

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReferencemembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = (String) userSnapshot.getValue();
                    userID.add(userId);
                }
                databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            if(userID.contains(dataSnapshot1.getKey())){
                                user user1 = dataSnapshot1.getValue(user.class);
                                userList.add(user1);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                UserList adapter = new UserList(ProjectActivity.this, userList);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        if (view == leave){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    projectGroup Project = dataSnapshot.getValue(projectGroup.class);
                    Project.removeMember(currentUser);
                    Map<String, Object> updatedGroupValues = Project.toMap();
                    databaseReference.updateChildren(updatedGroupValues);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Intent newActivity = new Intent(getApplicationContext(),ProjectNoPermissionActivity.class);
            newActivity.putExtra("groupID",val);
            startActivity(newActivity);
        }
        if (view == back){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}

