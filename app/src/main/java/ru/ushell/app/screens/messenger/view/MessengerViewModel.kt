package ru.ushell.app.screens.messenger.view

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.remember
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.ushell.app.data.features.messenger.MessengerRepository
import ru.ushell.app.data.features.messenger.dto.MessageType
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

    fun loadChat(recipientId: String,context: Context) {
        if (currentRecipientId == recipientId) return
        currentRecipientId = recipientId
        messages.clear()

        viewModelScope.launch {
            _uiState.value = MessengerUiState.Loading
            try {
                val oldMessages = messengerRepository.getMessageChat(recipientId, context)
                messages.addAll(oldMessages)
//                messages.addAll(mockMessages)
                _uiState.value = MessengerUiState.Success(messages.toList()) // ✅ без приведения
            } catch (e: Exception) {
                _uiState.value = MessengerUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }

        // Подписка на новые сообщения
        messengerRepository.incomingMessages
            .onEach { newMessage ->
                println(newMessage)
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

    fun sendMessage(recipientId: String, messageContext: Message, context: Context) {
        viewModelScope.launch {
            try {
                if(messageContext.uri != null) {
                    val mimeType = getMimeType(messageContext.uri, context)
                    val messageType = if (mimeType?.startsWith("image/") == true) {
                        MessageType.IMAGE
                    } else {
                        MessageType.FILE
                    }

                    messageContext.type = messageType
                    messageContext.file = getBytesFromUri(messageContext.uri, context)
                    messageContext.fileName = getNameFile(messageContext.uri, context)

                }

                messages.add(0, messageContext)
                _uiState.value = MessengerUiState.Success(messages.toList())

                messengerRepository.sendMessage(
                    recipientId = recipientId,
                    message =  messageContext,
                )
            } catch (e: Exception) {
                println(e)
                // Обработка ошибки
            }
        }
    }

    fun getBytesFromUri(uri: Uri, context: Context): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getMimeType(uri: Uri, context: Context): String? {
        return try {
            context.contentResolver.getType(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getNameFile(uri: Uri, context: Context): String? {
        return try {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    c.getString(nameIndex)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val mockMessages = listOf(
        Message(
            author = false,
            message = "Привет! Как дела?",
            type = MessageType.TEXT,
            timestamp = OffsetDateTime.now().minusDays(1)
        ),
        Message(
            author = false,
            message = "Привет! Как дела?",
            type = MessageType.TEXT,
            timestamp = OffsetDateTime.now().minusDays(1)
        ),
        Message(
            author = false,
            message = "Привет! Как дела?",
            type = MessageType.TEXT,
            timestamp = OffsetDateTime.now().minusMinutes(3)
        ),
        Message(
            author = true,
            message = "Всё отлично! А у тебя?",
            type = MessageType.TEXT,
            timestamp = OffsetDateTime.now().minusMinutes(4)
        ),
        Message(
            author = false,
            message = "Тоже нормально 😊",
            type = MessageType.TEXT,
            timestamp = OffsetDateTime.now().minusMinutes(5)
        )
    ).reversed()
}