package ru.ushell.app.api.response.attendance;

import java.util.Map;

public class Attendance{
    Integer subgroup;

    Map<String, Object> nameStudent;

    Map<String, Map<String, Object>> attendance;

    public Map<String, Map<String, Object>> getAttendance() {
        return attendance;
    }
    public void setAttendance(Map<String, Map<String, Object>> attendance) {
        this.attendance = attendance;
    }

    public Integer getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(Integer subgroup) {
        this.subgroup = subgroup;
    }

    public Map<String, Object>  getNameStudent() {
        return nameStudent;
    }
}