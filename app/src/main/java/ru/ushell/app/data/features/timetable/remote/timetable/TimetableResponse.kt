package ru.ushell.app.data.features.timetable.remote.timetable

data class TimetableResponse(
    val mainSchedule: Map<String, Map<String, Map<String, LessonResponse>>> = emptyMap(),
    val secondarySchedule: Map<String, Map<String, Map<String, LessonResponse>>> = emptyMap()
)

data class LessonResponse(
    val timeStart: String,
    val timeEnd: String,
    val subgroup: Int,
    val classroom: String,
    val withWhom: String,
    val subjectName: String,
    val fullType: String
)
