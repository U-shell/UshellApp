package ru.ushell.app.api;

import static ru.ushell.app.api.Service.student.updateTimetableGroup;
import static ru.ushell.app.api.Service.teacher.requestTimetableTeacher;
import static ru.ushell.app.api.Service.teacher.updateTimetableTeacher;
import static ru.ushell.app.models.eClass.ERoleClass.containsValueGroup;
import static ru.ushell.app.models.eClass.ERoleClass.containsValueTeacher;
import static ru.ushell.app.models.modelChat.chat.Chat.ChatList;
import static ru.ushell.app.models.modelTimeTable.attendance.Attendance.readAttendanceGroupDay;
import static ru.ushell.app.models.modelTimeTable.attendance.Attendance.readAttendanceStudent;
import static ru.ushell.app.models.modelTimeTable.lesson.Lesson.LessonsList;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONException;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import ru.ushell.app.SQLite.DatabaseHelper;
import ru.ushell.app.api.request.attendance.RequestAttendance;
import ru.ushell.app.api.request.chat.RequestMessageChat;
import ru.ushell.app.api.request.chat.RequestUserChat;
import ru.ushell.app.api.request.timetable.RequestTimetable;
import ru.ushell.app.models.User;
import ru.ushell.app.models.modelTimeTable.lesson.LessonReadDb;
import ru.ushell.app.ui.utils.calendar.CalendarUtils;

public class Service {

    @SuppressLint("StaticFieldLeak")
    public static DatabaseHelper databaseHelperMain;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public interface OnData {
        void onSuccess();
    }

    public Service(){}

    public Service(Context c) {
        if (databaseHelperMain == null) {
            databaseHelperMain = new DatabaseHelper(c);
        }
        context = c;
    }

    /**
     * Сборка происходить при первом вхоже
     */
    public void buildData() {
        requestTimetable();
        requestLessons();
        requestStudentAttendance();
    }

    /**
     * Обновление компонентов
     */
    public void updateData() {
        if (containsValueGroup()) {
            updateTimetableGroup();
        }
        if (containsValueTeacher()) {
            updateTimetableTeacher();
        }
        requestStudentAttendance();
    }

    private void requestLessons() {
        if (LessonsList.isEmpty()) {
            LessonReadDb.listLesson(databaseHelperMain);
            try {
                TimeUnit.SECONDS.sleep(0); // This seems unnecessary, remove it if possible
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestTimetable() {
        if (containsValueGroup()) {
            student.requestTimetableGroup();
        } else if (containsValueTeacher()) {
            requestTimetableTeacher();
        }
    }

    private void requestStudentAttendance() {
        if (containsValueGroup()) {
            RequestAttendance.getStudentAttendance(context, infoAttendanceStudent -> {
                try {
                    readAttendanceStudent(context);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void getAttendanceGroup(LocalDate date, Integer idGroup, OnData onLoginSuccess) {
        try {
            RequestAttendance.getGroupAttendance(
                    context,
                    CalendarUtils.formattedDateDayAttendance(date),
                    idGroup,
                    infoAttendanceGroup -> {
                        try {
                            readAttendanceGroupDay(context);
                            onLoginSuccess.onSuccess();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }catch (Exception e){
            System.out.println(e);
        }
    }

    static class student{
        private static void requestTimetableGroup() {
            if (databaseHelperMain.readAllDataMain().getCount() == 0) {
                updateTimetableGroup();
            }
        }

        static void updateTimetableGroup() {
            RequestTimetable.getTimeTableGroup(
                    User.getIDGroup(),
                    databaseHelperMain,
                    infoGroupData -> {
                        if (infoGroupData) {
                            LessonReadDb.listLesson(databaseHelperMain);
                        } else {
                            System.out.println("user tt bad");
                        }
                    }
            );
        }
    }

    static class teacher{

        static void requestTimetableTeacher() {
            if (databaseHelperMain.readAllDataMain().getCount() == 0) {
                updateTimetableTeacher();
            }
        }

        static void updateTimetableTeacher(){
            RequestTimetable.getTimeTableTeacher(
                    User.getIdUser(),
                    databaseHelperMain,
                    infoGroupData -> {
                        if (infoGroupData) {
                            LessonReadDb.listLesson(databaseHelperMain);
                        } else {
                            System.out.println("teacher tt bad");
                        }
                    }
            );
        }
    }

    public void getChatUser(){
        if(ChatList.isEmpty()) {
            RequestUserChat.getAllUser();
        }
    }

    public void getMessageChat(String senderId, String recipientId){
        RequestMessageChat.getMessageChat(senderId,recipientId);
    }
}
