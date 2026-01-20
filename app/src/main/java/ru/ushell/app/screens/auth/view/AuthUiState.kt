package ru.ushell.app.screens.auth.view

sealed interface AuthUiState {
    object Empty : AuthUiState
    data class Success(val status: Boolean) : AuthUiState
    object Loading : AuthUiState
    data class Error(val message: String) : AuthUiState
}