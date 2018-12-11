package models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@IgnoreExtraProperties
public class user {
    public String uid;
    public String firstName;
    public String lastName;
    public String email;
    public Map<String, Boolean> projectGroups = new HashMap<>();

    public user() {
        // Default constructor required for calls to DataSnapshot.getValue(user.class)
    }

    public user (String uid, String firstName, String lastName, String email) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addGroup (String groupID) {
        projectGroups.put(groupID, true);
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProjectGroups(Map<String, Boolean> projectGroups) {
        this.projectGroups = projectGroups;
    }

    public void removeGroup (String groupID) {
        if(projectGroups.containsKey(groupID)) {
            projectGroups.remove(groupID);
        }
    }

    // will be used to insert/update user object in database
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        result.put("projectGroups", projectGroups);

        return result;
    }
}