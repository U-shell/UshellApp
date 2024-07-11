package ru.ushell.app.ui.ModelTimeTable.lesson;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ru.ushell.app.R;
import ru.ushell.app.models.ERole;
import ru.ushell.app.models.RoleIdentifier;
import ru.ushell.app.models.User;

public class LessonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final ArrayList<Lesson> lessons;
    protected final View parentView;
    protected final LinearLayout layoutSubgroup;
    public final TextView timeStartEnd, formatLesson, nameLesson, nameTeacher, visit, subgroup;
    private final LessonAdapter.OnItemListener onItemListener;

    public LessonViewHolder(@NonNull View itemView, LessonAdapter.OnItemListener onItemListener, ArrayList<Lesson> lessons) {
        super(itemView);

        parentView = itemView.findViewById(R.id.cardView);
        timeStartEnd = itemView.findViewById(R.id.time_start_end_lesson);
        formatLesson = itemView.findViewById(R.id.format_lesson);
        nameLesson = itemView.findViewById(R.id.name_lesson);
        nameTeacher = itemView.findViewById(R.id.teacher_name);
        visit = itemView.findViewById(R.id.Visit_student);
        subgroup = itemView.findViewById(R.id.subgroup);
        layoutSubgroup = itemView.findViewById(R.id.subgroup_layout);


        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.lessons = lessons;
    }
    @Override
    public void onClick(View view) {
        Set<ERole> eRoleSet = new HashSet<>();
        eRoleSet.add(ERole.ROLE_HEAD);
        eRoleSet.add(ERole.ROLE_DEPUTY);
        eRoleSet.add(ERole.ROLE_TEACHER);
        eRoleSet.add(ERole.ROLE_ADMIN);
        RoleIdentifier roleIdentifier = new RoleIdentifier(eRoleSet);
        if(roleIdentifier.identification(User.getRoles())){
            onItemListener.onItemClick(getAdapterPosition(), lessons.get(getAdapterPosition()));
        }
    }
}

