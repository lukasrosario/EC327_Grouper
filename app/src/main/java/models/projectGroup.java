package models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// START Users class

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
//    public LocalDate formGroupDeadline;
    public String descript; //description is a keyword
    public List<String> members = new ArrayList<String>();

    public projectGroup() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public projectGroup (String course, String teamName, String projectTitle, LocalDate projectDeadline, String descript, int maxCapacity) {
        this.course = course;
        this.teamName = teamName;
        this.projectTitle = projectTitle;
        this.projectDeadline = projectDeadline;
//        this.formGroupDeadline = formGroupDeadline;
        this.descript = descript;
        this.maxCapacity = maxCapacity;
        currentNumMembers = 0;
    }

    public void addMember (String buid) {
        if (currentNumMembers != maxCapacity) {
            members.add(buid);
            currentNumMembers++;
        }
    }

    public void removeMember (String buid) {
        if (members.contains(buid)) {
            members.remove(buid);
            currentNumMembers--;
        }
    }

    public String getGroupID () {
        return groupID;
    }

    public void setGroupID (String groupID) {
        this.groupID = groupID;
    }

    public void removeGroup () {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference projGroupReference = db.child("projectGroups");
        projGroupReference.child(this.groupID).setValue(null);
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

        return result;
    }
}

// END Users class