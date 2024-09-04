package ru.ushell.app.models.ModelTimeTable.lesson;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import ru.ushell.app.ui.utils.calendar.CalendarUtils;

public class Lesson {

    public static ArrayList<Lesson> LessonsList = new ArrayList<>();
    public static ArrayList<Lesson> Secondary = new ArrayList<>();

    public static ArrayList<Lesson> LessonsForDataWeek(LocalDate Data) {

        Long week = CalendarUtils.ParityWeek(Data);
        String DayOfWeek = CalendarUtils.DayOfWeek(Data);
        String DateLesson = CalendarUtils.formattedDateToDbWeek(Data);

        ArrayList<Lesson> lessons = new ArrayList<>();// расписание конкртеного дня
        ArrayList<Lesson> secondaries = new ArrayList<>();// расписание конкртеного дня

        for(Lesson secondary : Secondary){
            if(getDateLesson(secondary.Week).equals(DateLesson)){
                secondaries.add(secondary);
            }
        }

        for (Lesson lesson : LessonsList) {
            if(lesson.Week.equals(week)){
                if (lesson.DayOfWeek.toLowerCase().equals(DayOfWeek)) {

                    boolean flag = true;

                    for(Lesson secondary: secondaries){
                        if (lesson.NumLesson.equals(secondary.NumLesson)) {
                            lessons.add(secondary);
                            secondaries.remove(secondary);
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

        if(!secondaries.isEmpty()){
            lessons.addAll(secondaries);
        }

        // сортировка по номеру урока
        lessons.sort(Comparator.comparing(o -> o.NumLesson));

        return lessons;
    }

    public static Boolean LessonIsDate(LocalDate Data) {

        Long week = CalendarUtils.ParityWeek(Data);
        String DayOfWeek = CalendarUtils.DayOfWeek(Data);
        String DateLesson = CalendarUtils.formattedDateToDbWeek(Data);

        for (Lesson secondary : Secondary) {
            if (getDateLesson(secondary.Week).equals(DateLesson)) {
                return true;
            }
        }

        for (Lesson lesson : LessonsList) {
            if (lesson.Week.equals(week) && lesson.DayOfWeek.toLowerCase().equals(DayOfWeek)) {
                return true;
            }
        }

        return false;
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
    private Integer IdGroup;
    private String WhitWhom;

    public Lesson(Long week, String dayOfWeek, Integer numLesson,
                  String timeLesson, String typeLesson, String subject,
                  Integer subgroup, String whitWhom,
                  Integer idGroup,
                  Integer classroom
    ) {
        Week = week;
        DayOfWeek = dayOfWeek;
        NumLesson = numLesson;
        TimeLesson = timeLesson;
        TypeLesson = typeLesson;
        Subject = subject;
        Subgroup = subgroup;
        WhitWhom = whitWhom;
        IdGroup = idGroup;
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

    public String getWhitWhom() {
        return WhitWhom;
    }

    public void setWhitWhom(String whitWhom) {
        WhitWhom = whitWhom;
    }

    public Integer getClassroom() {
        return Classroom;
    }

    public void setClassroom(Integer classroom) {
        Classroom = classroom;
    }

    public Integer getIdGroup() {
        return IdGroup;
    }

    public void setIdGroup(Integer idGroup) {
        IdGroup = idGroup;
    }

}
