package ru.ushell.app.screens.auth.view

sealed interface AuthUiState {
    object Empty : AuthUiState
    object Success : AuthUiState
    object Loading : AuthUiState
    data class Error(val message: String) : AuthUiState
}