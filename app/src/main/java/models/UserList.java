package models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xsmil.grouper.HasAuthActivity;
import com.example.xsmil.grouper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import models.user;

public class UserList extends RecyclerView.ViewHolder{

    private View mView;
    private Context mContext;

    private String groupID;

    private final TextView mName;
    private final TextView mEmail;
    private final TextView mUID;
    private final RelativeLayout  mUserContainer;
    private final LinearLayout mUser;

    public UserList(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mName = itemView.findViewById(R.id.etName);
        mEmail = itemView.findViewById(R.id.etEmail);
        mUID = itemView.findViewById(R.id.etUID);
        mUserContainer = itemView.findViewById(R.id.user_container);
        mUser = itemView.findViewById(R.id.userLayout);

        mUserContainer.setMotionEventSplittingEnabled(false);
    }

    public void bind(@NonNull user user) {
        setName(user.firstName + user.lastName);
        setEmail(user.getEmail());
        setUID(user.uid);
//        groupID = group.groupID;
    }

    private void setName(@Nullable String name) {
        mName.setText(name);
    }

    private void setEmail(@Nullable String email) {
        mEmail.setText(email);
    }

    private void setUID(@Nullable String uid) {
         mUID.setText(uid);
    }

}

/*public class UserList extends ArrayAdapter<user> {
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
}*/
