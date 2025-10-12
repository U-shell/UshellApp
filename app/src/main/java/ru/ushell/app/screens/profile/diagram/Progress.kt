package ru.ushell.app.screens.profile.diagram

import androidx.compose.ui.graphics.Color

data class Progress(
    val name: String,
    val allClasses: Int,
    val finishedClasses: Int,
    val colorMain: Color,
    val progressColor: Color = Color.Gray,
)

fun allLearnedProgress(presentAttendance: Int) = listOf(
    Progress(
        name = "Просмотренные\nлекций",
        allClasses = 20,
        finishedClasses = 0,
        colorMain = Color(0xFF6650a4),
        progressColor = Color(0xFF6650a4),
    ),
    Progress(
        name = "Посещаемость\nзанятий",
        allClasses = 100,
        finishedClasses = presentAttendance,
        colorMain = Color(0xFFFFF6EB),
        progressColor = Color(0xFFF76400),
    ),

    Progress(
        name = "Рейтинг\n ",
        allClasses = 20,
        finishedClasses = 0,
        colorMain = Color(0xFF051774),
        progressColor = Color(0xFF2295F8),
    ),
)
