package ru.ushell.app.models.modelTimeTable.attendance;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;

import ru.ushell.app.api.response.attendance.GroupTreeMap;

public class AttendanceAdapter {

    private static final String PREF_NAME = "AttendancePreferences";
    private static final String KEY_ATTENDANCE_STUDENT = "AttendanceStudent";
    private static final String KEY_ATTENDANCE_GROUP_DAY = "AttendanceGroupDay";
    private static final String KEY_ATTENDANCE_GROUP_OVER = "AttendanceGroupOver";

    private static SharedPreferences sharedPreferences;

    private static AttendanceAdapter instance;

    public AttendanceAdapter(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static AttendanceAdapter getInstance(Context context) {
        if (instance == null) {
            instance = new AttendanceAdapter(context.getApplicationContext());
        }
        return instance;
    }

    public void saveAttendanceStudent(JSONObject attendanceStudent){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ATTENDANCE_STUDENT, attendanceStudent.toString());
        editor.apply();
    }

    public JSONObject getAttendanceStudent() throws JSONException {
        String attendanceStudent =  sharedPreferences.getString(KEY_ATTENDANCE_STUDENT,"error");

        JSONObject attendanceStudentJson = new JSONObject();
        if(!attendanceStudent.equals("error")){
            attendanceStudentJson = new JSONObject(attendanceStudent);
            return attendanceStudentJson;
        }
        return attendanceStudentJson;
    }

    public void saveAttendanceGroupDay(TreeMap<String, GroupTreeMap> attendanceGroup){
        Gson gson = new Gson();
        String json = gson.toJson(attendanceGroup);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ATTENDANCE_GROUP_DAY, json);
        editor.apply();
    }

    public TreeMap<String, GroupTreeMap> getAttendanceGroupDay() {
        String json = sharedPreferences.getString(KEY_ATTENDANCE_GROUP_DAY, "");
        TreeMap<String, GroupTreeMap> result = new TreeMap<>();
        if (!json.isEmpty()) {
            try {
                Gson gson = new Gson();
                result = gson.fromJson(json, new TypeToken<TreeMap<String, GroupTreeMap>>(){}.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
