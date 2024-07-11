package ru.ushell.app.ui.ModelTimeTable.attendance;

import static ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceGroup.AttendanceGroupList;
import static ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceStudent.AttendanceStudentList;


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.TreeMap;

import ru.ushell.app.api.response.attendance.GroupTreeMap;

public class AttendanceReadDb{

    public static void AttendanceStudentRead(Context context) throws JSONException {

        JSONObject attendance = AttendanceAdapter.getInstance(context).getAttendanceStudent();
        Iterator<String> dataList = attendance.keys();

        while(dataList.hasNext()) {
            String data = dataList.next();
            Iterator<String> num =  attendance.getJSONObject(data).keys();
            while (num.hasNext()){
                Integer numLesson = Integer.parseInt(num.next());
                boolean status = (boolean) attendance.getJSONObject(data).get(String.valueOf(numLesson));
                AttendanceStudent attendanceStudent = new AttendanceStudent(data,numLesson,status);
                AttendanceStudentList.add(attendanceStudent);
            }
        }
    }

    public static void AttendanceGroupDayRead(Context context) throws JSONException{
        TreeMap<String, GroupTreeMap> attendance = AttendanceAdapter.getInstance(context).getAttendanceGroupDay();
        AttendanceGroupList.clear();
        for(String nameStudent: attendance.keySet()){
            GroupTreeMap student = attendance.get(nameStudent);
            assert student != null;
            AttendanceGroup attendanceGroup = new AttendanceGroup(nameStudent, student.getSubgroup(), student.getIdStudent(), student.getAttendance());
            AttendanceGroupList.add(attendanceGroup);
        }
    }
}
