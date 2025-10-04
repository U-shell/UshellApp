package ru.ushell.app.data.features.attendance.remote.attendance

import retrofit2.http.GET

interface AttendanceApi {

    @GET("./attendance/students/me")
    fun getAttendanceStudent(): AttendanceStudentResponse

    @GET("./attendance/group/day")
    fun getAttendanceGroupDay(): AttendanceGroupDayResponse

}