package ru.ushell.app.data.features.messenger

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.ushell.app.data.features.messenger.mappers.Chat
import ru.ushell.app.data.features.messenger.mappers.ChatList
import ru.ushell.app.data.features.messenger.mappers.Message
import ru.ushell.app.data.features.messenger.dto.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.dto.MessageChatResponse
import ru.ushell.app.data.features.messenger.dto.MessageType
import ru.ushell.app.data.features.messenger.remote.webSocket.Connect
import ru.ushell.app.data.features.user.UserRepository
import ru.ushell.app.data.local.file.SaveFile
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class MessengerRepository(
    private val messengerLocalDataSource: MessengerLocalDataSource,
    private val messengerRemoteDataSource: MessengerRemoteDataSource,
    private val userRepository: UserRepository,
) {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private var connect: Connect? = null

    private val _incomingMessages = Channel<Message>(Channel.UNLIMITED)
    val incomingMessages: Flow<Message> = _incomingMessages.receiveAsFlow()

    private val saveFile = SaveFile()

    suspend fun getInfoUserMessenger() = messengerRemoteDataSource.getInfoUserMessenger().also {
        userRepository.setChatId(it.id)
    }

    suspend fun getMessageChat(recipientId: String, context: Context): MutableList<Message> {
        val senderId = userRepository.getChatId()
        val body = BodyRequestMessageChat(senderId = senderId, recipientId = recipientId)

        val result: List<MessageChatResponse> = messengerRemoteDataSource.getMessageChat(body)
        val messages = mutableListOf<Message>()

        result.forEach {
            if (it.type == MessageType.FILE || it.type == MessageType.IMAGE){
                val uri = downloadFile(
                    recipientId = recipientId,
                    fileName = it.fileName,
                    context = context
                )
                messages.add(
                    Message(
                        author = senderId == it.senderId,
                        message = it.message,
                        type = it.type,
                        uri = uri,
                        timestamp = OffsetDateTime.parse(it.timestamp, formatter)
                    )
                )
            } else{
            messages.add(
                Message(
                    author = senderId == it.senderId,
                    message = it.message,
                    type = it.type,
                    timestamp = OffsetDateTime.parse(it.timestamp, formatter)
                )
            )
                }
        }
        messages.reverse()
        return messages
    }

    suspend fun getAllUser() = messengerRemoteDataSource.getAllUsers().listUsers?.forEach {
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

    suspend fun sendMessage(recipientId:String, message: Message) {
        if (connect?.isConnected() == true) {
            connect?.sendChatMessage(recipientId, message)
            if(message.uri != null) {
                uploadFile(recipientId = recipientId, message = message)
            }
        } else {
            throw IllegalStateException("WebSocket not connected")
        }
    }

    suspend fun uploadFile(recipientId:String, message: Message) {

        val fileBody = message.file!!.toRequestBody("application/octet-stream".toMediaType())
        val file = MultipartBody.Part.createFormData("file", message.fileName, fileBody)

        messengerRemoteDataSource.uploadFile(senderId = userRepository.getChatId(), resenderId = recipientId, file = file)
    }

    suspend fun downloadFile(recipientId:String, fileName: String, context: Context): Uri? {
        val response = messengerRemoteDataSource.getDownloadFile(
            senderId = userRepository.getChatId(),
            resenderId = recipientId,
            fileName)


        return saveFile.saveFileToCache(context = context, body = response.body()!!, displayName = fileName)
    }

}