package models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xsmil.grouper.CurrentProjActivity;
import com.example.xsmil.grouper.MainActivity;
import com.example.xsmil.grouper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class groupHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private View mView;
    private Context mContext;

    private String groupID;

    private final TextView mCourse;
    private final TextView mProjectTitle;
    private final TextView mProjectDeadline;
    private final RelativeLayout mGroupContainer;
    private final LinearLayout mGroup;

    public groupHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mCourse = itemView.findViewById(R.id.course);
        mProjectTitle = itemView.findViewById(R.id.projectTitle);
        mProjectDeadline = itemView.findViewById(R.id.projectDeadline);
        mGroupContainer = itemView.findViewById(R.id.group_container);
        mGroup = itemView.findViewById(R.id.group);

        itemView.setOnClickListener(this);
        mGroupContainer.setMotionEventSplittingEnabled(false);
    }

    public void bind(@NonNull projectGroup group) {
        setCourse(group.course);
        setProjectTitle(group.projectTitle);
        setProjectDeadline(group.getProjectDeadline());
        groupID = group.groupID;
    }

    private void setCourse(@Nullable String course) {
        mCourse.setText(course);
    }

    private void setProjectTitle(@Nullable String projectTitle) {
        mProjectTitle.setText(projectTitle);
    }

    private void setProjectDeadline(@Nullable String projectDeadline) {
        mProjectDeadline.setText(projectDeadline);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, CurrentProjActivity.class); // this should go to the project group details page; just using placeholder for now
        intent.putExtra("groupID", groupID); // not sure if this is right; made it get the groupID of most recently added group to ArrayList
        mContext.startActivity(intent);
    }
}

/*    View mView;
    Context mContext;

    public projectsHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindProject(projectGroup group) {
        TextView tvCourse = (TextView) mView.findViewById(R.id.tvCourse);
        TextView tvProjectTitle = (TextView) mView.findViewById(R.id.tvProjectName);
        TextView tvProjectDeadline = (TextView) mView.findViewById(R.id.tvProjectDeadline);

        tvCourse.setText(group.course);
        tvProjectTitle.setText(group.projectTitle);
        tvProjectDeadline.setText(group.getProjectDeadline());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<projectGroup> groups = new ArrayList<>();
        DatabaseReference projectGroupsRef = FirebaseDatabase.getInstance().getReference().child("projectGroups");
        projectGroupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    groups.add(snapshot.getValue(projectGroup.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("position", itemPosition + "");
//                intent.putExtra("groups", Parcels.wrap(groups));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}

/*    private List mProjectData;
    private Context mContext;

    public ProjectsAdapter(Context mContext, List mProjectData) {
        this.mProjectData = mProjectData;
        this.mContext = mContext;
    }

    @Override
    public ProjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group,
                parent, false);
        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectsViewHolder holder, int position) {
        holder.mCourseView.setText(mProjectData.get(position).getmTitle());
        holder.mProjectNameView.setText(mProjectData.get(position).getmDetails());
        holder.mDescriptionView.setText(mProjectData.get(position).getmDescription);
//        Currently the data is supposed to be held in a list, the tutorial suggested a class.
    }

    @Override
    public int getItemCount() {
        mProjectData.size();
    }

}

class ProjectsViewHolder extends RecyclerView.ViewHolder {
    TextView mCourseView;
    TextView mProjectNameView;
    TextView mDescriptionView;

    ProjectsViewHolder(View itemView) {
        super(itemView);

        mCourseView = itemView.findViewById(R.id.tvCourse);
        mProjectNameView = itemView.findViewById(R.id.tvProjectName);
        mDescriptionView = itemView.findViewById(R.id.tvProjectDescription);
    }
}*/