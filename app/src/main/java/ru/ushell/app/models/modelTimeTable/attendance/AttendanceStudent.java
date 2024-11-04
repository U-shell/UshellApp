package ru.ushell.app.models.modelTimeTable.attendance;

import java.util.ArrayList;

public class AttendanceStudent {
    public static ArrayList<AttendanceStudent> AttendanceStudentList = new ArrayList<>();// посешаемость

    public static ArrayList<AttendanceStudent> AttendanceForData(String data){

        ArrayList<AttendanceStudent> attendance = new ArrayList<>();// посещаемость дня

        for(AttendanceStudent student : AttendanceStudentList){
            if(student.DataAttendanceStudent.equals(data)) {

                attendance.add(student);
            }
        }

        return attendance;
    }

    private String DataAttendanceStudent;
    private Integer NumLesson;
    private Boolean Status;

    public AttendanceStudent(String dataAttendanceStudent, Integer numLesson, Boolean status) {
        DataAttendanceStudent = dataAttendanceStudent;
        NumLesson = numLesson;
        Status = status;
    }

    public String getDataAttendanceStudent() {
        return DataAttendanceStudent;
    }

    public void setDataAttendanceStudent(String dataAttendanceStudent) {
        DataAttendanceStudent = dataAttendanceStudent;
    }

    public Integer getNumLesson() {
        return NumLesson;
    }

    public void setNumLesson(Integer numLesson) {
        NumLesson = numLesson;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }
}
