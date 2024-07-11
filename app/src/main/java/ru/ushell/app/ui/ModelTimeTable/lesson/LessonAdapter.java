package ru.ushell.app.ui.ModelTimeTable.lesson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.metrics.LogSessionId;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.Console;
import java.util.ArrayList;

import ru.ushell.app.R;
import ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceStudent;

public class LessonAdapter extends RecyclerView.Adapter<LessonViewHolder> {
    Context context;
    ArrayList<Lesson> lesson;
    ArrayList<AttendanceStudent> attendanceStudents;
    private final OnItemListener onItemListener;

    public LessonAdapter(Context context, ArrayList<Lesson> lesson, ArrayList<AttendanceStudent> attendanceStudents, OnItemListener onItemListener) {
        this.context = context;
        this.lesson = lesson;
        this.attendanceStudents = attendanceStudents;
        this.onItemListener = onItemListener;
    }

    // дизайн панели
    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lessonItems = LayoutInflater.from(context).inflate(R.layout.lesson_item, parent, false);
        return new LessonViewHolder(lessonItems, onItemListener, lesson);
    }


    // что будет находиться в дизайне
    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {

        holder.timeStartEnd.setText(lesson.get(position).getTimeLesson());
        holder.formatLesson.setText(lesson.get(position).getTypeLesson());
        holder.nameLesson.setText(lesson.get(position).getSubject());
        holder.nameTeacher.setText(lesson.get(position).getTeacher());

        if(lesson.get(position).getSubgroup() != 0) {
            holder.subgroup.setText(String.format("Подгруппа %s",String.valueOf(lesson.get(position).getSubgroup())));
        }else{
            holder.subgroup.setText(null);
            holder.subgroup.setVisibility(View.GONE);
            holder.layoutSubgroup.setVisibility(View.GONE);
        }

        VisitStudent(holder, position);
    }
    private void VisitStudent(@NonNull LessonViewHolder holder, int position){
        boolean flag = false;
        Integer numLesson = lesson.get(position).getNumLesson();
        for(int index = 0; index < attendanceStudents.size() ;index++) {
            flag = true;
            if(attendanceStudents.get(index).getNumLesson().equals(numLesson) && attendanceStudents.get(index).getStatus().equals(Boolean.TRUE)){
                holder.visit.setText(" ");
                break;
            }
            else{
                holder.visit.setText("H");
            }
        }
        if(!flag)holder.visit.setText("H");
        }

    @Override
    public int getItemCount() {
        return lesson.size();
    }

    public interface  OnItemListener {
        void onItemClick(int position, Lesson lesson);
    }
}
