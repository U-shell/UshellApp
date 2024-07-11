package ru.ushell.app.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.ushell.app.api.response.ResponseInfoGroup;
import ru.ushell.app.api.response.ResponseInfoTeacher;
import ru.ushell.app.api.response.ResponseSingIn;
import ru.ushell.app.api.response.ResponseTimeTable;
import ru.ushell.app.api.response.attendance.AttendanceGroupResponse;
import ru.ushell.app.api.response.attendance.AttendanceStudentResponse;

// этот файл хранит в себе все возсожные пути которые есть в api
public interface API {

    @POST("auth/signup")
    Call<Void> postSignup(@Body RequestBody body);

    @POST("auth/signin")
    Call<ResponseSingIn> postSingIn(@Body RequestBody body);

//    @PUT("resource/")

    @GET("info/group")
    Call<ResponseInfoGroup> getInfoGroup(@Header("Authorization") String token);

    @GET("info/teacher")
    Call<ResponseInfoTeacher> getInfoTeacher(@Header("Authorization") String token);

    @GET("timetable/group")
    Call<ResponseTimeTable> getTimeTable(@Query("id") Integer id);

    @GET("attendance/student")
    Call<AttendanceStudentResponse> getAttendanceStudent(@Query("id") Integer id,
                                                         @Query("group") String groupName,
                                                         @Header("Authorization") String token);

    @GET("attendance/group/day")
    Call<AttendanceGroupResponse> getAttendanceGroupDay(@Query("name") String groupName,
                                                        @Query("data") String data,
                                                        @Header("Authorization") String token);

    @GET("attendance/group/over")
    Call<?> getAttendanceGroupOver(@Query("name") String groupName, @Query("data") String data);

    @PUT("attendance/update/group")
    Call<HTTP> putAttendanceGroup(@Query("name") String groupName);


}
