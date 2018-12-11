package com.example.xsmil.grouper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import models.UserList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.groupHolder;
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
    private Button remove;
    private String val;
    private FirebaseAuth mAuth;
    private List<user> userList;
    private List<String> userID;
    private String currentUser;
    ListView listView;

    @BindView(R.id.userLayout)
    RecyclerView mRecyclerView;

    DatabaseReference mMemberIDsRef;

    @NonNull
    protected static final Query sUsersQuery = FirebaseDatabase.getInstance().getReference().child("users");

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page);

        projectName = (TextView) findViewById(R.id.etProjectName);
        Class = (TextView) findViewById(R.id.etCourse);
        deadline = (TextView) findViewById(R.id.etDeadline);
        description = (TextView) findViewById(R.id.etDescription);

        leave = (Button) findViewById(R.id.btLeave);
        back = (Button) findViewById(R.id.btBack);
        remove = (Button) findViewById(R.id.btRemove);
        leave.setOnClickListener(this);
        back.setOnClickListener(this);
        remove.setOnClickListener(this);

        val = getIntent().getStringExtra("groupID").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("projectGroups").child(val);
        databaseReferencemembers = FirebaseDatabase.getInstance().getReference("projectGroups").child(val).child("members");
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");

        userList = new ArrayList<user>();

        userID = new ArrayList<String>();

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getUid().trim();

        mRecyclerView = (RecyclerView) findViewById(R.id.lView);

        isUserAdmin();

    }

    private void isUserAdmin(){
        databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!currentUser.equals(dataSnapshot.getValue().toString())){
                    remove.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();

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

        databaseReference.child("course").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Class.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    @NonNull
    protected RecyclerView.Adapter newAdapter() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mMemberIDsRef = FirebaseDatabase.getInstance().getReference().child("projectGroups").child(val).child("members");

        FirebaseRecyclerOptions<user> options =
                new FirebaseRecyclerOptions.Builder<user>()
                        .setIndexedQuery(mMemberIDsRef, sUsersQuery.getRef(), user.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<user, UserList>(options) {
            @Override
            public UserList onCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserList(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull UserList holder, int position, @NonNull user model) {
                holder.bind(model);
            }
        };
    }

/*    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference("projectGroups").child(val).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey().toString();
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

        databaseReference.child("course").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Class.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


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

                    // update groups under user id in database
                    removeGroupFromUserInDB(Project.groupID);
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
        if(view == remove){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    projectGroup Project = dataSnapshot.getValue(projectGroup.class);
                    Project.removeGroup();
                    removeGroupFromUserInDB(Project.groupID);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void removeGroupFromUserInDB(String groupID) {

        final String currentUid = mAuth.getCurrentUser().getUid();
        final String groupIDFinal = groupID;

        databaseReferenceUsers.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user currentUser = dataSnapshot.getValue(user.class);
                currentUser.removeGroup(groupIDFinal);

                Map<String, Object> updatedUserValues = currentUser.toMap();
                databaseReferenceUsers.child(currentUid).setValue(updatedUserValues);

                DatabaseReference projGroupMembersRef = FirebaseDatabase.getInstance().getReference().child("projGroupMembers");
                projGroupMembersRef.child(groupIDFinal).child(currentUid).setValue(updatedUserValues);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Cancel: ", "onCancelled", databaseError.toException());
            }
        });
    }

}


