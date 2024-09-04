package ru.ushell.app.api.request.attendance;

import android.content.Context;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.BodyRequestPutStudentAttendance;
import ru.ushell.app.api.body.BodyRequestTokenIdentifier;
import ru.ushell.app.api.response.attendance.Attendance;
import ru.ushell.app.api.response.attendance.AttendanceGroupResponse;
import ru.ushell.app.api.response.attendance.AttendanceStudentResponse;
import ru.ushell.app.api.response.attendance.GroupTreeMap;
import ru.ushell.app.models.ModelTimeTable.attendance.AttendanceAdapter;
import ru.ushell.app.models.User;

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
        Call<AttendanceStudentResponse> attendanceStudent = api.getAttendanceStudent(User.getIdUser(), User.getIDGroup(), tokenIdentifier.getToken());

        attendanceStudent.enqueue(new Callback<AttendanceStudentResponse>() {
            @Override
            public void onResponse(@NonNull Call<AttendanceStudentResponse> call, @NonNull Response<AttendanceStudentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AttendanceStudentResponse attendanceStudentResponse = response.body();
                    if (attendanceStudentResponse.getResponse().getSubgroup() != null) {
                        User.saveSubgroup(attendanceStudentResponse.getResponse().getSubgroup());
                    } else {
                        return;
                    }

                    JSONObject Attendances = new JSONObject();
                    if (attendanceStudentResponse.getResponse().getAttendance() != null) {

                        try {
                            Map<String, Map<String, Object>> attendance = attendanceStudentResponse.getResponse().getAttendance();

                            for (String data : attendance.keySet()) {
                                JSONObject lessons = new JSONObject();
                                for (String numLesson : Objects.requireNonNull(attendance.get(data)).keySet()) {
                                    lessons.put(numLesson, Objects.requireNonNull(attendance.get(data)).get(numLesson));
                                }
                                Attendances.put(data, lessons);
                            }
                        } catch (JSONException e) {
                            System.out.println("error Attendances json");
                        }
                    } else {
                        callback.onAttendanceStudentReceived(null); // Обработка ошибки
                    }

                    AttendanceAdapter.getInstance(context).saveAttendanceStudent(Attendances);

                    callback.onAttendanceStudentReceived(Attendances);
                } else {
                    callback.onAttendanceStudentReceived(null); // Handle error appropriately
                }
            }

            @Override
            public void onFailure(@NonNull Call<AttendanceStudentResponse> call, @NonNull Throwable t) {
                callback.onAttendanceStudentReceived(null); // Обработка ошибки
            }
        });
    }

    public static void getGroupAttendance(Context context, String DataLesson, Integer IdGroup, AttendanceGroupDayCallback callback){

        RetrofitService retrofitService = new RetrofitService();
        BodyRequestTokenIdentifier tokenIdentifier = new BodyRequestTokenIdentifier();


        API api = retrofitService.getRetrofit().create(API.class);

        Call<AttendanceGroupResponse> attendanceStudent = api.getAttendanceGroupDay(IdGroup, DataLesson, tokenIdentifier.getToken());

        attendanceStudent.enqueue(new Callback<AttendanceGroupResponse>() {
            @Override
            public void onResponse(@NonNull Call<AttendanceGroupResponse> call, @NonNull Response<AttendanceGroupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    AttendanceGroupResponse attendanceGroupResponse = response.body();
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
                }else{
                    callback.onAttendanceGroupReceived(null); // Handle error appropriately
                }
            }

            @Override
            public void onFailure(@NonNull Call<AttendanceGroupResponse> call, @NonNull Throwable t) {
                callback.onAttendanceGroupReceived(null); // Обработка ошибки
            }
        });
    }

    public static void putStudentAttendance(Integer NameGroup, BodyRequestPutStudentAttendance bodyRequestPutStudentAttendance){

        RetrofitService retrofitService = new RetrofitService();
        BodyRequestTokenIdentifier tokenIdentifier = new BodyRequestTokenIdentifier();

        Gson gson = new Gson();
        String json = gson.toJson(bodyRequestPutStudentAttendance);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        API api = retrofitService.getRetrofit().create(API.class);

        Call<Void> attendanceStudent = api.putAttendanceGroup(NameGroup, requestBody);

        attendanceStudent.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                System.out.println("ep");
                System.out.println(response.code());
            }
            @Override
            public void onFailure(@NonNull Call<Void> call,@NonNull Throwable t) {
                System.out.println("bad");
                System.out.println(call);
                System.out.println(call.request().url());
                System.out.println(call.request().isHttps());
            }
        });
    }
}
