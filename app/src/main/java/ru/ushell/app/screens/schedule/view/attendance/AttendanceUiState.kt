package ru.ushell.app.screens.schedule.view.attendance

import ru.ushell.app.data.features.attendance.mappers.AttendanceGroup

sealed interface AttendanceUiState {
    object Empty : AttendanceUiState
    object Loading : AttendanceUiState
    data class Success(val attendance: List<AttendanceGroup>) : AttendanceUiState
    data class Error(val message: String) : AttendanceUiState

}