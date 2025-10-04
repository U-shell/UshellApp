package ru.ushell.app.data.features.attendance.remote.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceStudentResponse(
    val attendance: Student,
)

data class AttendanceGroupDayResponse(
    @SerializedName("listAttendance")
    val attendance: Map<String, Student> = emptyMap(),
)

data class Student(
    val username: String,
    val nameStudent: String,
    val subgroup: Int,
    val attendance: Map<Int, Status> = emptyMap()
)

enum class Status{
    EXISTS,
    NOT_EXISTS
}