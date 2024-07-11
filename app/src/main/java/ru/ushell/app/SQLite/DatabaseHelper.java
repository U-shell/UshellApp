package ru.ushell.app.SQLite;

import static ru.ushell.app.SQLite.TableColumns.COLUMN_CLASSROOM;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_DATE_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_DAY_WEEK;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_ID;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_NUM_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBGROUP;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBJECT;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TEACHER;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_END;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_START;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TYPE_LESSON;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_WEEK;
import static ru.ushell.app.SQLite.TableColumns.DATABASE_NAME;
import static ru.ushell.app.SQLite.TableColumns.DATABASE_VERSION;
import static ru.ushell.app.SQLite.TableColumns.TABLE_NAME_MAIN;
import static ru.ushell.app.SQLite.TableColumns.TABLE_NAME_SECONDARY;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.sql.Time;
import java.sql.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                String.format("CREATE TABLE %s", TABLE_NAME_MAIN)
                        + "("
                        + String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT, ",COLUMN_ID)

                        + String.format("%s INTEGER,", COLUMN_WEEK)
                        + String.format("%s TEXT,", COLUMN_DAY_WEEK)

                        + String.format("%s INTEGER,", COLUMN_NUM_LESSON)

                        + String.format("%s TIME,", COLUMN_TIME_START)
                        + String.format("%s TIME,", COLUMN_TIME_END)

                        + String.format("%s TEXT,", COLUMN_TYPE_LESSON)

                        + String.format("%s TEXT,", COLUMN_SUBJECT)
                        + String.format("%s INTEGER,", COLUMN_SUBGROUP)

                        + String.format("%s TEXT,", COLUMN_TEACHER)

                        + String.format("%s VARCHAR(150)", COLUMN_CLASSROOM)
                        + ")";
        db.execSQL(query);

        String query_ =
                String.format("CREATE TABLE %s", TABLE_NAME_SECONDARY)
                        + "("
                        + String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT, ",COLUMN_ID)

                        + String.format("%s VARCHAR(50),", COLUMN_DATE_LESSON)
                        + String.format("%s TEXT,", COLUMN_DAY_WEEK)

                        + String.format("%s INTEGER,", COLUMN_NUM_LESSON)

                        + String.format("%s TIME,", COLUMN_TIME_START)
                        + String.format("%s TIME,", COLUMN_TIME_END)

                        + String.format("%s TEXT,", COLUMN_TYPE_LESSON)

                        + String.format("%s TEXT,", COLUMN_SUBJECT)
                        + String.format("%s INTEGER,", COLUMN_SUBGROUP)

                        + String.format("%s TEXT,", COLUMN_TEACHER)

                        + String.format("%s VARCHAR(150)", COLUMN_CLASSROOM)
                        + ")";
        db.execSQL(query_);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SECONDARY);
        onCreate(db);
    }

    public void addMainSchedule(Integer week, String dayOfWeek, Integer numLesson,
                   Time timeStart, Time timeEnd,
                   String typeOfLesson, String subject, Integer subgroup,
                   String teacher, String classroom){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_WEEK, week);
        cv.put(COLUMN_DAY_WEEK, dayOfWeek);
        cv.put(COLUMN_NUM_LESSON, numLesson);

        cv.put(COLUMN_TIME_START, timeStart.getTime());
        cv.put(COLUMN_TIME_END, timeEnd.getTime());

        cv.put(COLUMN_TYPE_LESSON, typeOfLesson);
        cv.put(COLUMN_SUBJECT, subject);
        cv.put(COLUMN_SUBGROUP, subgroup);
        cv.put(COLUMN_TEACHER, teacher);
        cv.put(COLUMN_CLASSROOM, classroom);

        long result = db.insert(TABLE_NAME_MAIN,null, cv);
        System.out.println(result != -1);

//        if(result == -1){
//            System.out.println(false);
//        }
//        else{
//            System.out.println(true);
//        }
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
//        }
    }

    public void addSecondarySchedule(Date dateLesson, String dayOfWeek, Integer numLesson,
                                Time timeStart, Time timeEnd,
                                String typeOfLesson, String subject, Integer subgroup,
                                String teacher, String classroom){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE_LESSON, dateLesson.getTime());
        cv.put(COLUMN_DAY_WEEK, dayOfWeek);
        cv.put(COLUMN_NUM_LESSON, numLesson);

        cv.put(COLUMN_TIME_START, timeStart.getTime());
        cv.put(COLUMN_TIME_END, timeEnd.getTime());

        cv.put(COLUMN_TYPE_LESSON, typeOfLesson);
        cv.put(COLUMN_SUBJECT, subject);
        cv.put(COLUMN_SUBGROUP, subgroup);
        cv.put(COLUMN_TEACHER, teacher);
        cv.put(COLUMN_CLASSROOM, classroom);

        long result = db.insert(TABLE_NAME_SECONDARY,null, cv);
        System.out.println(result != -1);

//        if(result == -1){
//            System.out.println(false);
//        }
//        else{
//            System.out.println(true);
//        }
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
//        }
    }

    public Cursor readAllDataMain(){
        String query = "SELECT * FROM " + TABLE_NAME_MAIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataSecondary(){
        String query = "SELECT * FROM " + TABLE_NAME_SECONDARY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id,
                    Integer week, String dayOfWeek, Integer numLesson,
                    Time timeStart, Time timeEnd,
                    String typeOfLesson, String subject, Integer subgroup,
                    String teacher, String classroom){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_WEEK, week);
        cv.put(COLUMN_DAY_WEEK, dayOfWeek);
        cv.put(COLUMN_NUM_LESSON, numLesson);
        cv.put(COLUMN_TIME_START, timeStart.getTime());
        cv.put(COLUMN_TIME_END, timeEnd.getTime());
        cv.put(COLUMN_TYPE_LESSON, typeOfLesson);
        cv.put(COLUMN_SUBJECT, subject);
        cv.put(COLUMN_SUBGROUP, subgroup);
        cv.put(COLUMN_TEACHER, teacher);
        cv.put(COLUMN_CLASSROOM, classroom);

        long result = db.update(TABLE_NAME_MAIN, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_MAIN, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_MAIN);
        db.execSQL("DELETE FROM " + TABLE_NAME_SECONDARY);
    }
}
