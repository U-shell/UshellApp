package ru.ushell.app.api;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.ushell.app.api.body.chat.BodyRequestMessageChat;
import ru.ushell.app.api.body.chat.BodyRequestUserDetails;
import ru.ushell.app.api.response.*;
import ru.ushell.app.api.response.attendance.AttendanceGroupResponse;
import ru.ushell.app.api.response.attendance.AttendanceStudentResponse;
import ru.ushell.app.api.response.chat.ResponseAllUserChat;
import ru.ushell.app.api.response.chat.ResponseInfoUserChat;
import ru.ushell.app.api.response.chat.ResponseMessageChat;
import ru.ushell.app.api.response.info.ResponseInfoGroup;
import ru.ushell.app.api.response.info.ResponseInfoTeacher;
import ru.ushell.app.api.response.info.ResponseInfoUser;
import ru.ushell.app.api.template.TimetableResponse;

public interface API {

    RetrofitService retrofitService = new RetrofitService();
    API api = retrofitService.getRetrofit().create(API.class);


    @GET("auth/login")
    Call<ResponseLogin> login(@Header("Authorization") String token);

    @GET("auth/logout")
    Call<ResponseLogin> logout(@Header("Authorization") String token);

    @PUT("auth/refresh")
    Call<ResponseInfoUser> refreshToken(@Header("Authorization") String token);

    @GET("info/groups/group")
    Call<ResponseInfoGroup> infoGroup(@Header("Authorization") String token);

    @GET("info/users/me")
    Call<ResponseInfoTeacher> infoMe(@Header("Authorization") String token);

    @GET("info/users/me")
    Call<ResponseInfoTeacher> infoTeacher(@Header("Authorization") String token);

    @GET("/timetable/groups/group")
    Call<TimetableResponse> timetableGroup(@Query("id") Integer id);

    @GET("/timetable/teachers/me")
    Call<TimetableResponse> timetableTeacher(@Header("Authorization") String token);


    @GET("attendance/students/me")
    // TODO: изменить AttendanceStudentResponse
    Call<AttendanceStudentResponse> attendanceMe(@Header("Authorization") String token);

    @GET("statistic/attendance/me")
    Call<?> statisticAttendanceMe(@Header("Authorization") String token);

    @GET("attendance/groups/day")
    Call<?> attendanceMeGroupDay(@Header("Authorization") String token,
                                 @Query("id") Integer groupId,
                                 @Query("date") String date);
//--------------------------------------------------------------------------------------------------
    /**
     * Attendance
     */

    @GET("attendance/students")
    Call<AttendanceStudentResponse> getAttendanceStudent(@Query("id") Integer id,
                                                         @Query("id_group") Integer IdGroup,
                                                         @Header("Authorization") String token);

    @GET("attendance/group/day")
    Call<AttendanceGroupResponse> getAttendanceGroupDay(@Query("id_group") Integer IdGroup,
                                                        @Query("data") String data,
                                                        @Header("Authorization") String token);

//    @GET("attendance/group/over")
//    Call<?> getAttendanceGroupOver(@Query("name") String groupName,
//                                   @Query("data") String data);

    @PUT("attendance/update/group")
    Call<Void> putAttendanceGroup(@Query("id_group") Integer IdGroup,
                                           @Body RequestBody body);

//--------------------------------------------------------------------------------------------------
    /**
     * Chat
     */

    @POST("/chat/users/me")
    Call<ResponseInfoUserChat> getInfoUserChat(@Body RequestBody requestBody);

    @POST("/chat/users/summaries")
    Call<ResponseAllUserChat> getAllUserSummaries(@Body BodyRequestUserDetails bodyRequestUserDetails);

    @POST("/chat/message/count")
    Call<String> getCountNewMessage(@Body BodyRequestMessageChat body);

    @POST("/chat/message/messages")
    Call<List<ResponseMessageChat>> getMessageChat(@Body BodyRequestMessageChat body);


    @GET("/chat/message/messages/{id}")
    Call<Void> findMessages();

    @GET("/chat/users/")
    Call<Void> getAllUsersChat();

    @GET("/chat/users/summary/{username}")
    Call<Void> getUserSummary();

//--------------------------------------------------------------------------------------------------
}
