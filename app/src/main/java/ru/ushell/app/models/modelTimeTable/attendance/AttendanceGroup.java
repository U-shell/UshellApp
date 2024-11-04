package ru.ushell.app.models.modelTimeTable.attendance;

import org.json.JSONObject;
import java.util.ArrayList;

public class AttendanceGroup {
    public static ArrayList<AttendanceGroup> AttendanceGroupList = new ArrayList<>();

    public static ArrayList<AttendanceGroup> AttendanceGroupForData(Integer numLesson, Integer subgroup){
        ArrayList<AttendanceGroup> attendance = new ArrayList<>();// посещаемость дня
        for(AttendanceGroup student : AttendanceGroupList){
            if(subgroup.equals(0)) {
                attendance.add(student);
            }
            else if(student.subgroup.equals(subgroup)){
                attendance.add(student);
            }
        }
        return attendance;
    }

    private String nameStudent;
    private Integer subgroup;
    private Integer idStudent;
    private JSONObject attendance;


    public AttendanceGroup(String nameStudent, Integer subgroup, Integer idStudent, JSONObject attendance) {
        this.nameStudent = nameStudent;
        this.subgroup = subgroup;
        this.idStudent = idStudent;
        this.attendance = attendance;
    }

    public String getNameStudent() {
        return nameStudent.replaceFirst(" ", "\n");
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public Integer getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(Integer subgroup) {
        this.subgroup = subgroup;
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public JSONObject getAttendance() {
        return attendance;
    }

    public void setAttendance(JSONObject attendance) {
        this.attendance = attendance;
    }
}
