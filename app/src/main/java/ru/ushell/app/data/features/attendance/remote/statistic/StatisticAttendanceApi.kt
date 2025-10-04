package ru.ushell.app.data.features.attendance.remote.statistic

import retrofit2.http.GET

interface StatisticAttendanceApi {

    @GET("./statistic/attendance/me")
    fun getStatisticAttendance()

}