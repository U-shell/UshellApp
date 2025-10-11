package ru.ushell.app.data.features.attendance.mappers

import ru.ushell.app.data.features.attendance.remote.attendance.Status


data class AttendanceGroup(
    val id: String,
    val nameStudent: String,
    val subgroup: Int,
    val attendance: Status
)

data class AttendanceStudentGroup(
    val id: String,
    val date: String,
    val numLesson:  Int,
    var attendance: Status = Status.UNKNOWN
)



data class AttendanceGroupRequest(
    val date: String,
    val lesson: Int,
    val list: Map<String, Status>
)