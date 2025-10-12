package ru.ushell.app.screens.timetable.view.timetable

import ru.ushell.app.data.features.attendance.mappers.Attendance
import ru.ushell.app.data.features.timetable.mappers.lesson.Lesson

sealed interface TimetableUiState {
    object Empty : TimetableUiState
    object Loading : TimetableUiState
    data class Success(val lessons: List<Lesson>, val attendance: List<Attendance>?) : TimetableUiState
    data class Error(val message: String) : TimetableUiState
}