package com.example.xsmil.grouper;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseIndexArray;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.groupHolder;
import models.projectGroup;
import models.user;

public class CurrentProjActivity extends AppCompatActivity {

    @NonNull
    protected static final Query sGroupsQuery = FirebaseDatabase.getInstance().getReference().child("projectGroups");

    @BindView(R.id.groupsList)
    RecyclerView mRecyclerView;

    private Button creategroupbutton;
    private Button backButton;
    private TextView textViewUserEmail;
    private TextView textViewUserFull;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private DatabaseReference mGroupIndicesRef;

    private DatabaseReference dR;
    private ListView listView;
    private ArrayList <String> groupName = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_proj);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Opens Create Group Activity.
        creategroupbutton = (Button) findViewById(R.id.CreateGroupBtn);
        creategroupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreateGroup();
            }
        });

        backButton = (Button) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMain();
            }
        });

        // If the user is not signed in, it opens LoginActivity.
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        // Displays the user's email in TextView using FireBase Authentication.
        textViewUserEmail = (TextView) findViewById(R.id.TextViewUserEmail);
        textViewUserEmail.setText(user.getEmail());

        textViewUserFull = (TextView) findViewById(R.id.textViewUserFull);
        // Retrieves first and last name from user class in FireBase Database
        // Concatenates the first and last name and sets the textView to display the Full Name.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user userInfo = dataSnapshot.getValue(user.class);
                System.out.println(userInfo.firstName);
                String fullName = userInfo.firstName + " " + userInfo.lastName;
                textViewUserFull.setText(fullName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();
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

        mGroupIndicesRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projectGroups");

        FirebaseRecyclerOptions<projectGroup> options =
                new FirebaseRecyclerOptions.Builder<projectGroup>()
                        .setIndexedQuery(mGroupIndicesRef, sGroupsQuery.getRef(), projectGroup.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<projectGroup, groupHolder>(options) {
            @Override
            public groupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new groupHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.group, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull groupHolder holder, int position, @NonNull projectGroup model) {
                holder.bind(model);
            }
        };
    }

    private void openActivityCreateGroup() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }

    private void openActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}