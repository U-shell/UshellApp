package ru.ushell.app.data.features.timetabel.room.dao.lesson

import ru.ushell.app.data.features.timetabel.room.dao.main.TimetableEntity

data class Lesson(
    val week: Int,
    val dayOfWeek: String,
    val numLesson: Int,
    val subject: String,
    val type: String,
    val withWhom: String,
    val time: String,
    val classroom: String,
    val subgroup: Int
)