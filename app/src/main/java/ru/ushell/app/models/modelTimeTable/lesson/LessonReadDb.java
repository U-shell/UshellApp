package ru.ushell.app.models.modelTimeTable.lesson;


import static ru.ushell.app.SQLite.TableColumns.COLUMN_CLASSROOM;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_DATE_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_DAY_WEEK;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_ID_GROUP;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_NUM_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBGROUP;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBJECT;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_WITH_WHOM;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_END;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_START;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TYPE_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_WEEK;
import static ru.ushell.app.models.modelTimeTable.lesson.Lesson.LessonsList;
import static ru.ushell.app.models.modelTimeTable.lesson.Lesson.Secondary;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import ru.ushell.app.SQLite.DatabaseHelper;

// получение расписание
public class LessonReadDb {

    public static void listLesson(DatabaseHelper databaseHelperMain) {
        readMainSchedule(databaseHelperMain);
        readSecondarySchedule(databaseHelperMain);
    }


    private static void readMainSchedule(DatabaseHelper databaseHelperMain) {
        Cursor cursor = databaseHelperMain.readAllDataMain();
        LessonsList.clear();
        while (cursor.moveToNext()) {
            long week = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_WEEK));
            String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_WEEK));
            int numLesson = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUM_LESSON));
            String timeLesson = getTimeLesson(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_START)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_END)));
            String typeLesson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_LESSON));
            String subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT));
            int subgroup = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBGROUP));
            String withWhom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WITH_WHOM));
            int idGroup = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_GROUP));
            int classroom = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASSROOM));

            Lesson newEvent = new Lesson(week, dayOfWeek, numLesson, timeLesson, typeLesson, subject, subgroup, withWhom, idGroup, classroom);
            LessonsList.add(newEvent);
        }
    }

    private static void readSecondarySchedule(DatabaseHelper databaseHelperMain) {
        Cursor cursor = databaseHelperMain.readAllDataSecondary();
        Secondary.clear();
        while (cursor.moveToNext()) {
            long dateLesson = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE_LESSON));
            String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_WEEK));
            int numLesson = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUM_LESSON));
            String timeLesson = getTimeLesson(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_START)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_END)));
            String typeLesson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_LESSON));
            String subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT));
            int subgroup = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBGROUP));
            String withWhom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WITH_WHOM));
            int idGroup = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_GROUP));
            int classroom = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASSROOM));

            Lesson newEvent = new Lesson(dateLesson, dayOfWeek, numLesson, timeLesson, typeLesson, subject, subgroup, withWhom, idGroup, classroom);
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
