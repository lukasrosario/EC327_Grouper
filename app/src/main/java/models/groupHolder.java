package models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xsmil.grouper.CurrentProjActivity;
import com.example.xsmil.grouper.R;

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