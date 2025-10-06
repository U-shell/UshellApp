package ru.ushell.app.screens.timetable.view

import ru.ushell.app.data.features.timetabel.room.dao.lesson.Lesson

sealed interface TimetableUiState {
    object Empty : TimetableUiState
    object Loading : TimetableUiState
    data class Success(val lessons: List<Lesson>) : TimetableUiState
    data class Error(val message: String) : TimetableUiState
}