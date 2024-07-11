package ru.ushell.app.models;

import java.util.ArrayList;
/**
 * Вспомогательный файл сохранения данный студентов группы
 */
public class Student {
    public static ArrayList<Student> StudentDataList = new ArrayList<>();// посешаемость

    public static ArrayList<Student> StudentForData(String num_day_week){
        ArrayList<Student> StudentInOut = new ArrayList<>();// посещаемость дня
        for(Student student : StudentDataList){
            if(student.DatadatyStudent.equals(num_day_week)) {
                StudentInOut.add(student);
            }
        }
        return StudentInOut;
    }


    private String DatadatyStudent , TimeLessonGo, MeningData;

    public Student(String DatadatyStudent , String TimeLessonGo, boolean MeningData)
    {
        this.DatadatyStudent = DatadatyStudent;
        this.TimeLessonGo = TimeLessonGo;
        this.MeningData = String.valueOf(MeningData);
    }
    public String getDatadatyStudent() {
        return DatadatyStudent;
    }

    public void setDatadatyStudent(String datadatyStudent) {
        DatadatyStudent = datadatyStudent;
    }

    public String getTimeLessonGo() {
        return TimeLessonGo;
    }

    public void setTimeLessonGo(String timeLessonGo) {
        TimeLessonGo = timeLessonGo;
    }

    public String getMeningData() {
        return MeningData;
    }

    public void setMeningData(String meningData) {
        MeningData = meningData;
    }


}
