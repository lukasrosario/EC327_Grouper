package com.example.xsmil.grouper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.groupHolder;
import models.projectGroup;

public class MainActivity extends AppCompatActivity {

    @NonNull
    protected static final Query sGroupQuery = FirebaseDatabase.getInstance().getReference().child("projectGroups").orderByChild("projectDeadline").limitToLast(50);

    @BindView(R.id.groupsList)
    RecyclerView mRecyclerView;

    private Button currentprojbutton;
    private Button createGroupButton;
    private Button logoutButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // reorganizes the recyclerview to display in descending order
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        currentprojbutton = (Button) findViewById(R.id.CurrentProjBtn);
        currentprojbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCurrentProj();
            }
        });

        createGroupButton = (Button) findViewById(R.id.buttonCreateGroup);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreateGroup();
            }
        });

        logoutButton = (Button) findViewById(R.id.LogoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut(); // built-in Firebase Authentication functionality to log out current user
                finish();
                Intent intent = new Intent(MainActivity.super.getBaseContext(), LoginActivity.class);
                startActivity(intent);
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
    // implements an inherited function from the android library to build a recyclerview where each
    // list item is a projectGroup as outlined in the groupHolder.java file
    protected RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<projectGroup> options =
                new FirebaseRecyclerOptions.Builder<projectGroup>()
                    .setQuery(sGroupQuery, projectGroup.class)
                    .setLifecycleOwner(this)
                    .build();

        return new FirebaseRecyclerAdapter<projectGroup, groupHolder>(options) {
            // expands the recyclerview with each list item
            @Override
            public groupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new groupHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group, parent, false));
            }

            // connects the front-end view with the projectGroup object model
            @Override
            protected void onBindViewHolder(@NonNull groupHolder holder, int position, @NonNull projectGroup model) {
                holder.bind(model);
            }
        };
    }

    private void openActivityCurrentProj() {
        Intent intent = new Intent(this, CurrentProjActivity.class);
        startActivity(intent);
    }

    private void openActivityCreateGroup() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }
}
