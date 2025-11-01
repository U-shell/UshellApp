package ru.ushell.app.screens.messenger.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.messenger.MessengerRepository
import ru.ushell.app.data.features.messenger.mappers.Message
import java.time.OffsetDateTime

@HiltViewModel
class MessengerViewModel  @Inject constructor(
    private val messengerRepository: MessengerRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<MessengerUiState>(MessengerUiState.Empty)
    val uiState: StateFlow<MessengerUiState> = _uiState.asStateFlow()

    private var currentRecipientId: String? = null
    private val messages = mutableListOf<Message>()

    fun loadChat(recipientId: String) {
        if (currentRecipientId == recipientId) return
        currentRecipientId = recipientId
        messages.clear()

        viewModelScope.launch {
            _uiState.value = MessengerUiState.Loading
            try {
                val oldMessages = messengerRepository.getMessageChat(recipientId)
                messages.addAll(oldMessages)
                _uiState.value = MessengerUiState.Success(messages.toList()) // ✅ без приведения
            } catch (e: Exception) {
                _uiState.value = MessengerUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }

        // Подписка на новые сообщения
        messengerRepository.incomingMessages
            .onEach { newMessage ->
                // Фильтруем по текущему собеседнику, если нужно
                // (если WebSocket присылает сообщения от всех — добавьте recipientId в Message)
                messages.add(0, newMessage) // или add в конец — зависит от порядка
                _uiState.value = MessengerUiState.Success(messages.toList())
            }
            .launchIn(viewModelScope)
    }

    fun connect() {
        viewModelScope.launch {
            messengerRepository.connectWebSocket()
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            messengerRepository.disconnectWebSocket()
        }
    }

    fun sendMessage(recipientId: String, message: String) {
        try {
            val outgoingMessage = Message(
                author = true,
                content = message,
                timestamp = OffsetDateTime.now()
            )
            messages.add(0, outgoingMessage)
            _uiState.value = MessengerUiState.Success(messages.toList())

            messengerRepository.sendMessage(recipientId, message)
        } catch (e: Exception) {
            // Обработка ошибки
        }
    }
}