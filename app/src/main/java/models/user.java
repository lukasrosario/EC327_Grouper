package models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// START users class

@IgnoreExtraProperties
public class user {
    public String uid; //update to uid
    public String firstName;
    public String lastName;
    public String email;
    public List<String> projectGroups = new ArrayList<String>();

    public user() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public user (String uid, String firstName, String lastName, String email) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addGroup (String groupID) {
        projectGroups.add(groupID);
    }

    public void removeGroup (String groupID) {
        projectGroups.remove(groupID);
    }

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

// END users class