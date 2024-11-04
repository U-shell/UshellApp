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

public interface API {
//--------------------------------------------------------------------------------------------------
    /**
     * Info user
     */

    @POST("auth/login")
    Call<ResponseSingIn> postSingIn(@Body RequestBody body);

    @GET("info/group")
    Call<ResponseInfoGroup> getInfoGroup(@Header("Authorization") String token);

    @GET("info/teacher")
    Call<ResponseInfoTeacher> getInfoTeacher(@Header("Authorization") String token);
//--------------------------------------------------------------------------------------------------
    /**
     * Timetable
     */

    @GET("timetable/group")
    Call<ResponseTimeTable> getTimeTableGroup(@Query("id") Integer id);

    @GET("timetable/teacher")
    Call<ResponseTimeTable> getTimeTableTeacher(@Query("id") Integer id);

//--------------------------------------------------------------------------------------------------
    /**
     * Attendance
     */

    @GET("attendance/student")
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
     * CHAT
     */

    @POST("users/me")
    Call<ResponseInfoUserChat> getInfoUserChat(@Body RequestBody requestBody);

    @POST("users/summaries")
    Call<ResponseAllUserChat> getAllUserSummaries(@Body BodyRequestUserDetails bodyRequestUserDetails);

    @POST("message/count")
    Call<String> getCountNewMessage(@Body BodyRequestMessageChat body);

    @POST("message/messages")
    Call<List<ResponseMessageChat>> getMessageChat(@Body BodyRequestMessageChat body);




    @GET("message/messages/{id}")
    Call<Void> findMessages();

    @GET("users/")
    Call<Void> getAllUsersChat();

    @GET("users/summary/{username}")
    Call<Void> getUserSummary();

//--------------------------------------------------------------------------------------------------
}
