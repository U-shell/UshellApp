package ru.ushell.app.data.features.attendance.mappers

data class Attendance(
    val date: String,
    val numLesson: Int,
    val status: Boolean
)