package com.example.xsmil.grouper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.sql.Ref;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import models.projectGroup;
import models.user;

public class CreateGroupActivity extends AppCompatActivity {

    DatabaseReference db;
    DatabaseReference projectGroupRef;
    DatabaseReference userRef;
    FirebaseAuth firebaseAuth;

    Button back;
    Button create;

    private String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        db = FirebaseDatabase.getInstance().getReference();
        projectGroupRef = db.child("projectGroups");
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = db.child("users");

        back = (Button) findViewById(R.id.btn_back);
        create = (Button) findViewById(R.id.btn_create);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain();
            }
        });

        // Pressing create creates new project group and saves information to database
        // No way to update data appropriately right now... as soon as group is saved; information is "cleared"
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewProjGroup();;

                /*if (TextUtils.isEmpty(groupID)) {
                    createNewProjGroup();
                }
                else {
                    updateProjGroup();
                }*/
            }
        });

//        toggleButton();

        projectGroupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                projectGroup newProjGroup = dataSnapshot.getValue(projectGroup.class);
                String groupID = dataSnapshot.getKey();
                System.out.println("Group ID: " + newProjGroup.groupID);
                System.out.println("Course:" + newProjGroup.course);
                System.out.println("Team Name: " + newProjGroup.teamName);
                System.out.println("Project Title: " + newProjGroup.projectTitle);
                System.out.println("Project Deadline: " + newProjGroup.projectDeadline);
//                System.out.println("Deadline to form group: " + newProjGroup.formGroupDeadline);
                System.out.println("Description: " + newProjGroup.descript);
                System.out.println("Members: " + newProjGroup.members);
                System.out.println("Max Capacity: " + newProjGroup.maxCapacity);
                Log.d("Added","new project group with groupID: " + groupID);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d("Changed", "project group with groupID: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Removed","project group with groupID: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d("Moved","project group with groupID: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Cancel:",databaseError.toString());
            }
        });

    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // change the button text between save and update
 /*   private void toggleButton() {
        save = (Button) findViewById(R.id.btn_save);

        if (TextUtils.isEmpty(groupID)) {
            save.setText(getResources().getString(R.string.save));
        }
        else {
            save.setText(getResources().getString(R.string.update));
        }
    }*/

    public void createNewProjGroup() {
        // Create new Project Group at /projectGroups
        final String groupID = projectGroupRef.push().getKey();

        final EditText projectNameInput = (EditText) findViewById(R.id.proj_name);
        final EditText courseInput = (EditText) findViewById(R.id.course);
        final EditText teamNameInput = (EditText) findViewById(R.id.team_name);
        final EditText projectDeadlineInput = (EditText) findViewById(R.id.proj_deadline_input);
//        final EditText groupFormDeadlineInput = (EditText) findViewById(R.id.form_group_deadline);
        final EditText descriptionInput = (EditText) findViewById(R.id.description);
        final EditText maxCapacityInput = (EditText) findViewById(R.id.max_capacity_input);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.createProjectGroup);

        alertDialogBuilder
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // does nothing
                    }
                })
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // get user input and set it to result
                        String projectName = projectNameInput.getText().toString();
                        String course = courseInput.getText().toString();
                        String teamName = teamNameInput.getText().toString();
                        String projectDeadlineString = projectDeadlineInput.getText().toString();
//                        String groupFormDeadlineString = groupFormDeadlineInput.getText().toString();
                        String description = descriptionInput.getText().toString();
                        String maxCapacity = maxCapacityInput.getText().toString();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // this needs to match the pattern of how it's entered in the string...may need to update depending on front-end
                        LocalDate projectDeadline = LocalDate.parse(projectDeadlineString, formatter);
//                        LocalDate groupFormDeadline = LocalDate.parse(groupFormDeadlineString, formatter);

                        // checks to make sure maximum capacity is a number entered. -- could probably do something similar for LocalDate, but need to know what exception would be thrown if not
                        try {
                            int num = Integer.parseInt(maxCapacity);
                            Log.i("",num+" is a number");
                        } catch (NumberFormatException e) {
                            Log.i("",maxCapacity+" is not a number");
                            Toast.makeText(getApplicationContext(), "Please enter a number for max capacity", Toast.LENGTH_SHORT).show();
                        }

                        String currentUid = firebaseAuth.getCurrentUser().getUid();

                        projectGroup projGroup = new projectGroup(course, teamName, projectName, projectDeadline, description, Integer.parseInt(maxCapacity));
                        projGroup.setGroupID(groupID);
                        projGroup.addMember(currentUid);
                        Map<String, Object> projectGroupValues = projGroup.toMap();

                        projectGroupRef.child(groupID).setValue(projectGroupValues);

                        addGroupToUserInDB(groupID);

                        confirmationAlert();
                        goToMain();
                    }
                }).create()
                .show();


        /*Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/projectGroups/" + groupID, projectGroupValues);
        db.updateChildren(childUpdates);*/
    }

    public void confirmationAlert() {
        // Dialog pops up to allow user to confirm group has been successfully created
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.confirmationAlert);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // does nothing
                    }
                }).create()
                .show();
    }

    // need to figure this out
    public void addGroupToUserInDB(String groupID) {

        final String currentUid = firebaseAuth.getCurrentUser().getUid();
        final String groupIDFinal = groupID;

        userRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user currentUser = dataSnapshot.getValue(user.class);
                currentUser.addGroup(groupIDFinal);

                Map<String, Object> updatedUserValues = currentUser.toMap();
                userRef.child(currentUid).setValue(updatedUserValues);

                DatabaseReference projGroupMembersRef = db.child("projGroupMembers");
                projGroupMembersRef.child(groupIDFinal).child(currentUid).setValue(updatedUserValues);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Cancel: ", "onCancelled", databaseError.toException());
            }
        });
    }

/*    public void updateProjGroup() {
        // Updates Project Group at groupID

        final EditText projectNameInput = (EditText) findViewById(R.id.proj_name);
        final EditText courseInput = (EditText) findViewById(R.id.course);
        final EditText teamNameInput = (EditText) findViewById(R.id.team_name);
        final EditText projectDeadlineInput = (EditText) findViewById(R.id.proj_deadline);
        final EditText groupFormDeadlineInput = (EditText) findViewById(R.id.form_group_deadline);
        final EditText descriptionInput = (EditText) findViewById(R.id.description);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String projectName = projectNameInput.getText().toString();
                String course = courseInput.getText().toString();
                String teamName = teamNameInput.getText().toString();
                String projectDeadlineString = projectDeadlineInput.getText().toString();
                String groupFormDeadlineString = groupFormDeadlineInput.getText().toString();
                String description = descriptionInput.getText().toString();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // this needs to match the pattern of how it's entered in the string...may need to update depending on front-end
                LocalDate projectDeadline = LocalDate.parse(projectDeadlineString, formatter);
                LocalDate groupFormDeadline = LocalDate.parse(groupFormDeadlineString, formatter);

                if (!TextUtils.isEmpty(projectName))
                    projectGroupRef.child(groupID).child("projectTitle").setValue(projectName);

                if (!TextUtils.isEmpty(course))
                    projectGroupRef.child(groupID).child("course").setValue(course);

                if (!TextUtils.isEmpty(teamName))
                    projectGroupRef.child(groupID).child("teamName").setValue(teamName);

                if (!TextUtils.isEmpty(projectDeadlineString))
                    projectGroupRef.child(groupID).child("projectDeadline").setValue(projectDeadline);

                if (!TextUtils.isEmpty(groupFormDeadlineString))
                    projectGroupRef.child(groupID).child("formGroupDeadline").setValue(groupFormDeadline);

                if (!TextUtils.isEmpty(description))
                    projectGroupRef.child(groupID).child("description").setValue(description);

            }
        });
    }*/
}
