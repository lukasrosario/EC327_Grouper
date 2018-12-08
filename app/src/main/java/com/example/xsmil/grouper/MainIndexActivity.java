package com.example.xsmil.grouper;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.groupHolder;
import models.projectGroup;

public class MainIndexActivity extends MainActivity {
    private DatabaseReference mGroupIndicesRef;

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
            @NonNull
            @Override
            public groupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new groupHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group, parent, false));
            }

            @Override
            protected  void onBindViewHolder(@NonNull groupHolder holder, int position, @NonNull projectGroup model) {
                holder.bind(model);
            }
        };
    }
}
