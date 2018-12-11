package com.example.xsmil.grouper;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.groupHolder;
import models.projectGroup;

public class MainIndexActivity extends MainActivity {
    private DatabaseReference mGroupIndicesRef;

    // similar to the function implementation in MainActivity.java
    // in this case, the method has been expanded to have more general functionality
    // it will look for the groupIDs located at the mGroupIndicesRef in the sGroupQuery node in the database
    @NonNull
    @Override
    protected FirebaseRecyclerAdapter<projectGroup, groupHolder> newAdapter() {
        mGroupIndicesRef = FirebaseDatabase.getInstance().getReference().child("projectGroups");

        FirebaseRecyclerOptions<projectGroup> options =
                new FirebaseRecyclerOptions.Builder<projectGroup>()
                    .setIndexedQuery(mGroupIndicesRef.limitToLast(50), sGroupQuery.getRef(), projectGroup.class)
                    .setLifecycleOwner(this)
                    .build();

        return new FirebaseRecyclerAdapter<projectGroup, groupHolder>(options) {
            // expands the recyclerview with each list item
            @NonNull
            @Override
            public groupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new groupHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group, parent, false));
            }

            @Override
            // connects the front-end view with the projectGroup object model
            protected  void onBindViewHolder(@NonNull groupHolder holder, int position, @NonNull projectGroup model) {
                holder.bind(model);
            }
        };
    }
}
