package ru.ushell.app.data.features.attendance.remote.attendance

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import ru.ushell.app.data.features.attendance.mappers.AttendanceGroupRequest

interface AttendanceApi {

    @GET("./attendance/students/me")
    suspend fun getAttendanceStudent(): AttendanceResponse

    @GET("./attendance/groups/day")
    suspend fun getAttendanceGroupDay(
        @Query("id") groupId: Int,
        @Query("date") date: String,
        @Query("num") numLesson: Int
    ): AttendanceGroupDayResponse

    @PUT("./attendance/groups/update")
    suspend fun putAttendanceGroup(
        @Query("id") groupId: Int,
        @Body attendance: AttendanceGroupRequest
    )
}