package ru.ushell.app.data.features.messenger

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.ushell.app.data.features.messenger.mappers.Chat
import ru.ushell.app.data.features.messenger.mappers.ChatList
import ru.ushell.app.data.features.messenger.mappers.Message
import ru.ushell.app.data.features.messenger.remote.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.remote.MessageChatResponse
import ru.ushell.app.data.features.messenger.remote.webSocket.Connect
import ru.ushell.app.data.features.user.UserRepository
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class MessengerRepository(
    private val messengerLocalDataSource: MessengerLocalDataSource,
    private val messengerRemoteDataSource: MessengerRemoteDataSource,
    private val userRepository: UserRepository,
) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private var connect: Connect? = null


    private val _incomingMessages = Channel<Message>(Channel.UNLIMITED)
    val incomingMessages: Flow<Message> = _incomingMessages.receiveAsFlow()



    suspend fun getInfoUserMessenger() = messengerRemoteDataSource.getInfoUserMessenger().also {
        userRepository.setChatId(it.id)
    }

    suspend fun getMessageChat(recipientId: String): MutableList<Message> {
        val senderId = userRepository.getChatId()
        val body = BodyRequestMessageChat(senderId = senderId, recipientId = recipientId)

        val result: List<MessageChatResponse> = messengerRemoteDataSource.getMessageChat(body)
        val messages = mutableListOf<Message>()

        result.forEach {
            messages.add(
                Message(
                    author = senderId == it.senderId,
                    content = it.message,
                    timestamp = OffsetDateTime.parse(it.timestamp, formatter)
                )
            )
        }
        messages.reverse()
        return messages
    }

    suspend fun getAllUser() = messengerRemoteDataSource.getAllUsers().listUsers?.forEach {
        println(it)
        ChatList.add(
            Chat(
                it.username,
                it.name,
                it.id,
                0
//                    getCountNewMessage(userRepository.getChatId(),it.id)
            )
        )
    }

    suspend fun getCountNewMessage(senderId: String, recipientId: String): Int =
        messengerRemoteDataSource.getCountNewMessage(
            BodyRequestMessageChat(
                senderId = senderId,
                recipientId = recipientId
            )
        )

    suspend fun connectWebSocket() {
        if (connect?.isConnected() == true) return

        val chatId = userRepository.getChatId()
        connect = Connect(chatId) { message ->
            _incomingMessages.trySend(message)
        }
        connect?.connect()
    }

    fun disconnectWebSocket() {
        connect?.disconnect()
        connect = null
    }

    fun sendMessage(recipientId: String, message: String) {
        if (connect?.isConnected() == true) {
            connect?.sendChatMessage(recipientId, message)
        } else {
            throw IllegalStateException("WebSocket not connected")
        }
    }
}