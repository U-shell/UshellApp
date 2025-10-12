package ru.ushell.app.screens.auth.view

sealed interface AuthUiState {
    object Empty : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}