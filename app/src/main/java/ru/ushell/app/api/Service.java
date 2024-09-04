package ru.ushell.app.api;

import static ru.ushell.app.api.Service.student.updateTimetableGroup;
import static ru.ushell.app.api.Service.teacher.requestTimetableTeacher;
import static ru.ushell.app.api.Service.teacher.updateTimetableTeacher;
import static ru.ushell.app.models.ModelTimeTable.attendance.Attendance.readAttendanceGroupDay;
import static ru.ushell.app.models.ModelTimeTable.attendance.Attendance.readAttendanceStudent;
import static ru.ushell.app.models.ModelTimeTable.lesson.Lesson.LessonsList;
import static ru.ushell.app.models.e_class.ERoleClass.containsValueGroup;
import static ru.ushell.app.models.e_class.ERoleClass.containsValueTeacher;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONException;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import ru.ushell.app.SQLite.DatabaseHelper;
import ru.ushell.app.api.request.attendance.RequestAttendance;
import ru.ushell.app.api.request.timetable.Request_TT;
import ru.ushell.app.models.ModelTimeTable.lesson.LessonReadDb;
import ru.ushell.app.models.User;
import ru.ushell.app.ui.utils.calendar.CalendarUtils;

public class Service {

    @SuppressLint("StaticFieldLeak")
    public static DatabaseHelper databaseHelperMain;
    private final Context context;

    private interface OnData {
        void onSuccess();
    }

    public Service(Context c) {
        if (databaseHelperMain == null) {
            databaseHelperMain = new DatabaseHelper(c);
        }
        this.context = c;
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
        } else if (containsValueTeacher()) {
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
            RequestAttendance.getStudentAttendance(this.context, infoAttendanceStudent -> {
                try {
                    readAttendanceStudent(this.context);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void getAttendanceGroup(LocalDate date, Integer idGroup, OnData onLoginSuccess) {
        RequestAttendance.getGroupAttendance(
                this.context,
                CalendarUtils.formattedDateDayAttendance(date),
                idGroup,
                infoAttendanceGroup -> {
                    try {
                        readAttendanceGroupDay(this.context);
                        onLoginSuccess.onSuccess();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    static class student{
        private static void requestTimetableGroup() {
            if (databaseHelperMain.readAllDataMain().getCount() == 0) {
                updateTimetableGroup();
            }
        }

        static void updateTimetableGroup() {
            Request_TT.getTimeTableGroup(
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
            Request_TT.getTimeTableTeacher(
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

}
