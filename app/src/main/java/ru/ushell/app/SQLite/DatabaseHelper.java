package ru.ushell.app.SQLite;

import static ru.ushell.app.SQLite.TableColumns.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.sql.Date;
import java.sql.Time;

public class DatabaseHelper extends SQLiteOpenHelper {

   private final Context context;

    public DatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create main timetable table
        try {
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME_MAIN + " (" +
//                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_WEEK + " INTEGER, " +
                            COLUMN_DAY_WEEK + " TEXT, " +
                            COLUMN_NUM_LESSON + " INTEGER, " +
                            COLUMN_TIME_START + " TIME, " +
                            COLUMN_TIME_END + " TIME, " +
                            COLUMN_TYPE_LESSON + " TEXT, " +
                            COLUMN_SUBJECT + " TEXT, " +
                            COLUMN_SUBGROUP + " INTEGER, " +
                            COLUMN_WITH_WHOM + " TEXT, " +
                            COLUMN_ID_GROUP + " INTEGER, " +
                            COLUMN_CLASSROOM + " VARCHAR(150)" +
                            ")"
            );

            // Create secondary timetable table
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME_SECONDARY + " (" +
//                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_DATE_LESSON + " VARCHAR(50), " +
                            COLUMN_DAY_WEEK + " TEXT, " +
                            COLUMN_NUM_LESSON + " INTEGER, " +
                            COLUMN_TIME_START + " TIME, " +
                            COLUMN_TIME_END + " TIME, " +
                            COLUMN_TYPE_LESSON + " TEXT, " +
                            COLUMN_SUBJECT + " TEXT, " +
                            COLUMN_SUBGROUP + " INTEGER, " +
                            COLUMN_WITH_WHOM + " TEXT, " +
                            COLUMN_ID_GROUP + " INTEGER, " +
                            COLUMN_CLASSROOM + " VARCHAR(150)" +
                            ")"
            );
        }catch (SQLiteException e) {
            // Handle any errors creating the tables
            Log.e("DatabaseHelper", "Error creating tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME_MAIN);
            db.execSQL("DELETE FROM " + TABLE_NAME_SECONDARY);
        }catch (SQLiteException exception){
            onCreate(db);
        }
    }

//    public void deleteTable(){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN);
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SECONDARY);
//        }catch (NullPointerException e){
//            System.out.println(e);
//            System.out.println("deleteTable");
//        }
//    }


    public void addMainSchedule(
            Integer week, String dayOfWeek, Integer numLesson,
            Time timeStart, Time timeEnd,
            String typeOfLesson, String subject, Integer subgroup,
            String withWhom, Integer id_group, String classroom) {

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
        cv.put(COLUMN_WITH_WHOM, withWhom);
        cv.put(COLUMN_ID_GROUP, id_group);
        cv.put(COLUMN_CLASSROOM, classroom);

        db.insert(TABLE_NAME_MAIN, null, cv);
    }

    public void addSecondarySchedule(Date dateLesson, String dayOfWeek, Integer numLesson,
                                Time timeStart, Time timeEnd,
                                String typeOfLesson, String subject, Integer subgroup,
                                String withWhom, Integer id_group,String classroom){

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
        cv.put(COLUMN_WITH_WHOM, withWhom);
        cv.put(COLUMN_ID_GROUP, id_group);
        cv.put(COLUMN_CLASSROOM, classroom);

        db.insert(TABLE_NAME_SECONDARY,null, cv);
    }

    public Cursor readAllDataMain(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME_MAIN, null, null, null, null, null, null);

    }

    public Cursor readAllDataSecondary(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME_SECONDARY, null, null, null, null, null, null);
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
        cv.put(COLUMN_WITH_WHOM, teacher);
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
}
