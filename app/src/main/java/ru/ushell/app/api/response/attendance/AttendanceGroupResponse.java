package ru.ushell.app.api.response.attendance;

import java.util.Map;

public class AttendanceGroupResponse {

    Map<String, Attendance> response;

    public Map<String, Attendance> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Attendance> response) {
        this.response = response;
    }
}
