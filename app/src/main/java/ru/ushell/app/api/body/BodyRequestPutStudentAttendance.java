package ru.ushell.app.api.body;

import java.util.Map;

public class BodyRequestPutStudentAttendance {
    private String data;
    private String lesson;
    private Map<String,Object> list;

    public BodyRequestPutStudentAttendance(String data, String lesson, Map<String, Object> list) {
        this.data = data;
        this.lesson = lesson;
        this.list = list;
    }

    public Map<String, Object> getList() {
        return list;
    }

    public void setList(Map<String, Object> list) {
        this.list = list;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
