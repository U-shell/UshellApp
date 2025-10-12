package ru.ushell.app.screens.profile.view

sealed interface ProfileUiState {
    object Empty : ProfileUiState
    object Loading : ProfileUiState
    data class Success(val name: String, val brief: String, val presentAttendance: Int): ProfileUiState
    data class Error(val message: String) : ProfileUiState
}
