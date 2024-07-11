package ru.ushell.app.ui.ModelTimeTable.lesson;


import static ru.ushell.app.SQLite.TableColumns.COLUMN_CLASSROOM;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_DATE_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_DAY_WEEK;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_NUM_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBGROUP;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBJECT;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TEACHER;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_END;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_START;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TYPE_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_WEEK;
import static ru.ushell.app.ui.ModelTimeTable.lesson.Lesson.LessonsList;
import static ru.ushell.app.ui.ModelTimeTable.lesson.Lesson.Secondary;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import ru.ushell.app.SQLite.DatabaseHelper;
import ru.ushell.app.ui.ModelTimeTable.lesson.Lesson;

// получение расписание
public class LessonReadDb {

    static Long Week;
    static Long DateLesson;
    static String DayOfWeek;
    static Integer NumLesson;
    static String TimeLesson;
    static String TypeLesson;
    static String Subject;
    static Integer Subgroup;
    static String Teacher;
    static Integer Classroom;

    public static void ListLesson(DatabaseHelper databaseHelperMain) {
        MainSchedule(databaseHelperMain);
        SecondarySchedule(databaseHelperMain);
    }

    private static void MainSchedule(DatabaseHelper databaseHelperMain) {
        Cursor cursor = databaseHelperMain.readAllDataMain();

        while (cursor.moveToNext()) {

            Week = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_WEEK));
            DayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_WEEK));
            NumLesson = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUM_LESSON));
            TimeLesson = getTimeLesson(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_START)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_END)));
            TypeLesson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_LESSON));
            Subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT));
            Subgroup = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBGROUP));
            Teacher = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER));
            Classroom = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASSROOM));

            Lesson newEvent = new Lesson(Week, DayOfWeek, NumLesson, TimeLesson, TypeLesson, Subject, Subgroup, Teacher, Classroom);
            LessonsList.add(newEvent);
        }
    }

    private static void SecondarySchedule(DatabaseHelper databaseHelperMain) {
        Cursor cursor = databaseHelperMain.readAllDataSecondary();

        while (cursor.moveToNext()) {
            DateLesson = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE_LESSON));
            DayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_WEEK));
            NumLesson = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUM_LESSON));
            TimeLesson = getTimeLesson(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_START)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_END)));
            TypeLesson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_LESSON));
            Subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT));
            Subgroup = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBGROUP));
            Teacher = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER));
            Classroom = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASSROOM));

            Lesson newEvent = new Lesson(DateLesson, DayOfWeek, NumLesson, TimeLesson, TypeLesson, Subject, Subgroup, Teacher, Classroom);
            Secondary.add(newEvent);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static String getTimeLesson(int TimeStart, int TimeEnd) {

        // Получаем текущий часовой пояс пользователя
        TimeZone userTimeZone = TimeZone.getDefault();

        Date timeStart = new Date(TimeStart);
        Date timeEnd = new Date(TimeEnd);

        // Преобразуем время в соответствии с часовым поясом пользователя

        SimpleDateFormat sdfUser = new SimpleDateFormat("HH:mm");
        sdfUser.setTimeZone(userTimeZone);
        String formattedTimeStart = sdfUser.format(timeStart);
        String formattedTimeEnd = sdfUser.format(timeEnd);

        return String.format("%s-%s", formattedTimeStart, formattedTimeEnd);
    }
}
