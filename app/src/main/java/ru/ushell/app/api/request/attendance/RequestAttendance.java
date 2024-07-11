package ru.ushell.app.api.request.attendance;

import android.content.Context;

import androidx.annotation.NonNull;

import com.squareup.okhttp.HttpUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.BodyRequestTokenIdentifier;
import ru.ushell.app.api.response.attendance.Attendance;
import ru.ushell.app.api.response.attendance.AttendanceGroupResponse;
import ru.ushell.app.api.response.attendance.AttendanceStudentResponse;
import ru.ushell.app.api.response.attendance.GroupTreeMap;
import ru.ushell.app.models.User;
import ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceAdapter;

public class RequestAttendance {

    public interface AttendanceStudentCallback {
        void onAttendanceStudentReceived(JSONObject infoAttendanceStudent);
    }

    public interface AttendanceGroupDayCallback {
        void onAttendanceGroupReceived(TreeMap<String, GroupTreeMap> infoAttendanceGroup);
    }

    public static void getStudentAttendance(Context context, AttendanceStudentCallback callback){

        RetrofitService retrofitService = new RetrofitService();
        BodyRequestTokenIdentifier tokenIdentifier = new BodyRequestTokenIdentifier();


        API api = retrofitService.getRetrofit().create(API.class);

        Call<AttendanceStudentResponse> attendanceStudent = api.getAttendanceStudent(User.getIdStudent(), User.getGroupName(), tokenIdentifier.getToken());

        attendanceStudent.enqueue(new Callback<AttendanceStudentResponse>() {
            @Override
            public void onResponse(@NonNull Call<AttendanceStudentResponse> call, @NonNull Response<AttendanceStudentResponse> response) {
                AttendanceStudentResponse attendanceStudentResponse = response.body();

                assert attendanceStudentResponse != null;

                User.saveSubgroup(attendanceStudentResponse.getResponse().getSubgroup());

                JSONObject Attendances = new JSONObject();
                if(attendanceStudentResponse.getResponse().getAttendance() != null) {

                    try {
                        Map<String, Map<String, Object>> attendance = attendanceStudentResponse.getResponse().getAttendance();

                        for (String data : attendance.keySet()) {
                            JSONObject lessons = new JSONObject();
                            for (String numLesson : Objects.requireNonNull(attendance.get(data)).keySet()) {
                                lessons.put(numLesson, Objects.requireNonNull(attendance.get(data)).get(numLesson));
                            }
                            Attendances.put(data,lessons);
                        }
                    }catch (JSONException e){
                        System.out.println("error Attendances json");
                    }
                }
                else {
                    callback.onAttendanceStudentReceived(null); // Обработка ошибки
                }

                AttendanceAdapter.getInstance(context).saveAttendanceStudent(Attendances);

                callback.onAttendanceStudentReceived(Attendances);
            }

            @Override
            public void onFailure(@NonNull Call<AttendanceStudentResponse> call, @NonNull Throwable t) {
                callback.onAttendanceStudentReceived(null); // Обработка ошибки
            }
        });
    }

    public static void getGroupAttendance(Context context, String DataLesson, AttendanceGroupDayCallback callback){

        RetrofitService retrofitService = new RetrofitService();
        BodyRequestTokenIdentifier tokenIdentifier = new BodyRequestTokenIdentifier();


        API api = retrofitService.getRetrofit().create(API.class);
        Call<AttendanceGroupResponse> attendanceStudent = api.getAttendanceGroupDay(User.getGroupName(), DataLesson, tokenIdentifier.getToken());

        attendanceStudent.enqueue(new Callback<AttendanceGroupResponse>() {
            @Override
            public void onResponse(@NonNull Call<AttendanceGroupResponse> call, @NonNull Response<AttendanceGroupResponse> response) {

                AttendanceGroupResponse attendanceGroupResponse = response.body();
                assert attendanceGroupResponse != null;
                TreeMap<String, GroupTreeMap> attendanceGroup = new TreeMap<>();
                if(response.code() == 200) {

                    for (String idStudent : attendanceGroupResponse.getResponse().keySet()) {
                        Attendance attendance = attendanceGroupResponse.getResponse().get(idStudent);
                        assert attendance != null;
                        GroupTreeMap student = new GroupTreeMap();

                        Integer subgroup = attendance.getSubgroup();
                        if(attendance.getAttendance() != null) {
                            for (String nameStudent : attendance.getAttendance().keySet()) {

                                JSONObject attendanceStudent = new JSONObject(Objects.requireNonNull(attendance.getAttendance().get(nameStudent)));

                                student.setAttendance(attendanceStudent);
                                student.setSubgroup(subgroup);
                                student.setIdStudent(Integer.parseInt(idStudent));
                                attendanceGroup.put(nameStudent, student);
                            }
                        }else{
                            for (String nameStudent : Objects.requireNonNull(attendanceGroupResponse.getResponse().get(idStudent)).getNameStudent().keySet()){
                                JSONObject attendanceStudent = new JSONObject();
                                student.setAttendance(attendanceStudent);
                                student.setSubgroup(subgroup);
                                student.setIdStudent(Integer.parseInt(idStudent));
                                attendanceGroup.put(nameStudent, student);
                            }
                        }
                    }

                    AttendanceAdapter.getInstance(context).saveAttendanceGroupDay(attendanceGroup);

                    callback.onAttendanceGroupReceived(attendanceGroup); // Обработка ошибки
                }else{
                    AttendanceAdapter.getInstance(context).saveAttendanceGroupDay(attendanceGroup);
                    callback.onAttendanceGroupReceived(null); // Обработка ошибки
                }
            }

            @Override
            public void onFailure(@NonNull Call<AttendanceGroupResponse> call, @NonNull Throwable t) {
                callback.onAttendanceGroupReceived(null); // Обработка ошибки
            }
        });
    }
}
