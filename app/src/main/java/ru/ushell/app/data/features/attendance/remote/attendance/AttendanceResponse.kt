package ru.ushell.app.data.features.attendance.remote.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(
    val statistic: Int,
    val attendance: AttendanceJsonResponse
)

data class AttendanceGroupDayResponse(
    @SerializedName("listAttendance")
    val attendance: Map<String, Student> = emptyMap(),
)

data class AttendanceJsonResponse(
    val attendance: Map<String, Map<String, Status>>? = null
)

data class Student(
    val username: String,
    val nameStudent: String,
    val subgroup: Int,
    val attendance: Status
)

data class AttendanceStudent(
    val attendance: Map<String, Status>? = null
)

enum class Status{
    @SerializedName("EXISTS")
    EXISTS,

    @SerializedName("NOT_EXISTS")
    NOT_EXISTS,

    UNKNOWN;


    companion object {
        @JvmStatic
        fun fromString(value: String): Status? {
            return when (value) {
                "EXISTS" -> EXISTS
                "NOT_EXISTS" -> NOT_EXISTS
                else -> null
            }
        }
    }
}