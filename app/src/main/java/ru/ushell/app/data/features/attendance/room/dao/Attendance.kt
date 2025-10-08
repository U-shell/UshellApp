package ru.ushell.app.data.features.attendance.room.dao

data class Attendance(
    val date: String,
    val numLesson: Int,
    val status: Boolean
)