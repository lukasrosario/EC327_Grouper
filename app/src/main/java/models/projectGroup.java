package models;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.xsmil.grouper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
// will use push() method in Firebase DB to assign random groupIDs
public class projectGroup {
    public String groupID;
    public int maxCapacity;
    private int currentNumMembers;
    public String course;
    public String teamName;
    public String projectTitle;
    public LocalDate projectDeadline;
    public String admin;
    //    public LocalDate formGroupDeadline;
    public String descript; //description is a keyword
    public Map<String, Boolean> members = new HashMap<>();

    public projectGroup() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public projectGroup (String course, String teamName, String projectTitle, LocalDate projectDeadline, String descript, int maxCapacity, String admin) {
        this.course = course;
        this.teamName = teamName;
        this.projectTitle = projectTitle;
        this.projectDeadline = projectDeadline;
//        this.formGroupDeadline = formGroupDeadline;
        this.descript = descript;
        this.maxCapacity = maxCapacity;
        this.admin = admin;
        currentNumMembers = 0;
    }

    public void addMember (String uid) {
        if (currentNumMembers != maxCapacity) {
            members.put(uid, true);
            currentNumMembers++;
        }
    }

    public void removeMember (String uid) {
        if (members.containsKey(uid)) {
            members.remove(uid);
            currentNumMembers--;
        }
    }

    public String getGroupID () {
        return groupID;
    }

    public void setGroupID (String groupID) {
        this.groupID = groupID;
    }

    public int getCurrentNumMembers() {
        return currentNumMembers;
    }


    // defining a destructor for the projectGroup class
    public void removeGroup() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference projGroupReference = db.child("projectGroups");
        DatabaseReference projGroupMembersRef = db.child("projGroupMembers");
        projGroupReference.child(this.groupID).setValue(null); // removes projectGroup from projectGroup node in DB
        projGroupMembersRef.child(this.groupID).setValue(null); // removes projectGroup from projGroupMembers node in DB

        final DatabaseReference userReference = db.child("users");
        final String groupID = this.groupID;

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    user currentUser = singleSnapshot.getValue(user.class);
                    String currentUid = currentUser.uid;
                    currentUser.removeGroup(groupID);

                    Map<String, Object> updatedUserValues = currentUser.toMap();
                    userReference.child(currentUid).setValue(updatedUserValues);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Cancel: ", "onCancelled", databaseError.toException());
            }
        });

// not really sure it is good design to implement this within the class
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(/*need activity here*/);
/*        alertDialogBuilder.setMessage(R.string.deleteGroupAlert);

        // creates pop-up dialog to confirm that project group has been deleted to user
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // does nothing
                    }
                }).create()
                .show();*/
    }

    public void setProjectDeadline (String projectDeadlineString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        projectDeadline = LocalDate.parse(projectDeadlineString, formatter);
    }

 /*   public void setFormGroupDeadline (String groupFormDeadlineString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        projectDeadline = LocalDate.parse(groupFormDeadlineString, formatter);
    }*/

    public String getProjectDeadline () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedProjectDeadline = projectDeadline.format(formatter);
        return formattedProjectDeadline;
    }

/*    public String getFormGroupDeadline () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedFormGroupDeadline = formGroupDeadline.format(formatter);
        return formattedFormGroupDeadline;
    }*/

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupID", groupID);
        result.put("course", course);
        result.put("teamName", teamName);
        result.put("projectTitle", projectTitle);
        result.put("projectDeadline", getProjectDeadline());
//        result.put("formGroupDeadline", getFormGroupDeadline());
        result.put("description", descript);
        result.put("members", members);
        result.put("maxCapacity", maxCapacity);
        result.put("currentNumMembers", currentNumMembers);
        result.put("admin",admin);

        return result;
    }
}