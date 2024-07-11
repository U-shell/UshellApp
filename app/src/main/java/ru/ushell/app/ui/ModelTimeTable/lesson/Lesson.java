package ru.ushell.app.ui.ModelTimeTable.lesson;


import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Lesson {
    public static ArrayList<Lesson> LessonsList = new ArrayList<>();// расписание группы за две недели
    protected static ArrayList<Lesson> Secondary = new ArrayList<>();// расписание группы за две недели

    public static ArrayList<Lesson> LessonsForDataWeek(Long week, String day, String dateLesson) {

        ArrayList<Lesson> lessons = new ArrayList<>();// расписание конкртеного дня
        for(Lesson secondary :Secondary){
            if(getDateLesson(secondary.Week).equals(dateLesson)){
                lessons.add(secondary);
            }
        }

        for (Lesson lesson : LessonsList) {
            if(lesson.Week.equals(week)){
                if (lesson.DayOfWeek.toLowerCase().equals(day)) {

                    boolean flag = true;

                    for(Lesson lesson_: lessons){
                        if (lesson.NumLesson.equals(lesson_.NumLesson)) {
                            flag = false;
                            break;
                        }
                    }

                    if(flag){
                        lessons.add(lesson);
                    }
                }
            }
        }
        return lessons;
    }


    @SuppressLint("SimpleDateFormat")
    private static String getDateLesson(Long DateLesson) {
        Date timeEnd = new Date(DateLesson);
        SimpleDateFormat sdfUser = new SimpleDateFormat("yyyy-MM-dd");
        return sdfUser.format(timeEnd);
    }


    private Long Week;
    private String DayOfWeek;
    private Integer NumLesson;
    private String TimeLesson;
    private String TypeLesson;
    private String Subject;
    private Integer Subgroup;
    private String Teacher;

    public Lesson(Long week, String dayOfWeek, Integer numLesson,
                  String timeLesson, String typeLesson, String subject,
                  Integer subgroup, String teacher, Integer classroom) {
        Week = week;
        DayOfWeek = dayOfWeek;
        NumLesson = numLesson;
        TimeLesson = timeLesson;
        TypeLesson = typeLesson;
        Subject = subject;
        Subgroup = subgroup;
        Teacher = teacher;
        Classroom = classroom;
    }

    private Integer Classroom;


    public Long getWeek() {
        return Week;
    }

    public void setWeek(Long week) {
        Week = week;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public Integer getNumLesson() {
        return NumLesson;
    }

    public void setNumLesson(Integer numLesson) {
        NumLesson = numLesson;
    }

    public String getTimeLesson() {
        return TimeLesson;
    }

    public void setTimeLesson(String timeLesson) {
        TimeLesson = timeLesson;
    }

    public String getTypeLesson() {
        return TypeLesson;
    }

    public void setTypeLesson(String typeLesson) {
        TypeLesson = typeLesson;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public Integer getSubgroup() {
        return Subgroup;
    }

    public void setSubgroup(Integer subgroup) {
        Subgroup = subgroup;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public Integer getClassroom() {
        return Classroom;
    }

    public void setClassroom(Integer classroom) {
        Classroom = classroom;
    }
}
