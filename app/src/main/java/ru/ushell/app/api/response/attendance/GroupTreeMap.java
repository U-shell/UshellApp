package ru.ushell.app.api.response.attendance;

import org.json.JSONObject;

import java.util.TreeMap;

public class GroupTreeMap{

    Integer idStudent;
    JSONObject attendance;
    Integer subgroup;

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


    public Integer getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(Integer subgroup) {
        this.subgroup = subgroup;
    }
}
