package ru.ushell.app.data.features.attendance.retrofit

import ru.ushell.app.data.features.attendance.AttendanceRemoteDataSource
import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceApi
import ru.ushell.app.data.features.attendance.mappers.AttendanceGroupRequest

class RetrofitAttendanceDataSource(
    val attendanceApi: AttendanceApi,
//    val statisticAttendanceApi: StatisticAttendanceApi
): AttendanceRemoteDataSource {

    override suspend fun getAttendanceStudent() = attendanceApi.getAttendanceStudent()

    override suspend fun getAttendanceGroupDay(groupId: Int, date: String, numLesson: Int) =
        attendanceApi.getAttendanceGroupDay(groupId, date, numLesson)

    override suspend fun putAttendanceGroup(groupId: Int, attendance: AttendanceGroupRequest) =
        attendanceApi.putAttendanceGroup(groupId, attendance)

}