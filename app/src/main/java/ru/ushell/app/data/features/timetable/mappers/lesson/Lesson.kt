package ru.ushell.app.data.features.timetable.mappers.lesson

import ru.ushell.app.data.features.timetable.room.dao.main.TimetableEntity
import ru.ushell.app.data.features.timetable.room.dao.secondary.TimetableSecondaryEntity
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.DayOfWeek
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.ParityWeek
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.formattedDateToDbWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

val primaryListLesson: ArrayList<TimetableEntity> = ArrayList()
val secondaryListLesson: ArrayList<TimetableSecondaryEntity> = ArrayList()

fun lessonExistDate(date: LocalDate): Boolean{
    val week = ParityWeek(date)
    val dayOfWeek = DayOfWeek(date).lowercase()
    val dateLesson = formattedDateToDbWeek(date)

    if (secondaryListLesson.any { parseDate(it.dateLesson) == dateLesson }) {
        return true
    }

    return primaryListLesson.any { lesson ->
        lesson.week == week && lesson.dayOfWeek.lowercase() == dayOfWeek
    }
}

private fun parseDate(dateLesson: String): String {
    return LocalDate.parse(dateLesson, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
}
