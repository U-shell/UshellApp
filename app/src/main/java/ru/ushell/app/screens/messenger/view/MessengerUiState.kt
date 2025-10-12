package ru.ushell.app.screens.messenger.view

import ru.ushell.app.data.features.messenger.mappers.Message


sealed interface MessengerUiState {
    object Empty : MessengerUiState
    object Loading : MessengerUiState
    data class Error(val message: String) : MessengerUiState
    data class Success(val messageList: List<Message>) : MessengerUiState
}