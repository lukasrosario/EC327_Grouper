package com.example.xsmil.grouper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import models.user;

public class UserList extends ArrayAdapter<user> {
    private Activity context;
    private DatabaseReference reference;
    private List<user> userList;

    public UserList(Activity context, List<user> userList){
        super(context,R.layout.user_layout,userList);
        this.context = context;
        this.userList = userList;
    }
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.user_layout,null,true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.etName);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.etEmail);
        TextView textViewUID = (TextView) listViewItem.findViewById(R.id.etUID);

        user user1 = userList.get(position);

        textViewName.setText(user1.getFirstName());
        textViewEmail.setText(user1.getEmail());
        textViewUID.setText(user1.getUid());

        return listViewItem;
    }
}
