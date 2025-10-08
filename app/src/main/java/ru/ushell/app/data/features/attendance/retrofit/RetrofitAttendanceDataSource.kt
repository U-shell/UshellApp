package ru.ushell.app.data.features.attendance.retrofit

import ru.ushell.app.data.features.attendance.AttendanceRemoteDataSource
import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceApi

class RetrofitAttendanceDataSource(
    val attendanceApi: AttendanceApi,
//    val statisticAttendanceApi: StatisticAttendanceApi
): AttendanceRemoteDataSource {

    override suspend fun getAttendanceStudent() = attendanceApi.getAttendanceStudent()

}