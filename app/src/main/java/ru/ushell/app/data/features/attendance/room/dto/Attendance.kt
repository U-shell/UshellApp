package ru.ushell.app.data.features.attendance.room.dto

data class Attendance(
    val date: String,
    val numLesson: Int,
    val status: Boolean
)