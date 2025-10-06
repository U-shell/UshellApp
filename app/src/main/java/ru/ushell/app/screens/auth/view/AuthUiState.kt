package ru.ushell.app.screens.auth.view

sealed interface AuthUiState {
    object Empty : AuthUiState          // Начальное состояние
    object Loading : AuthUiState       // Идёт авторизация
    object Success : AuthUiState       // Успешно
    data class Error(val message: String) : AuthUiState
}