package ru.ushell.app.data.features.messanger

import ru.ushell.app.data.features.messanger.dto.Chat
import ru.ushell.app.data.features.messanger.dto.ChatList
import ru.ushell.app.data.features.messanger.dto.Message
import ru.ushell.app.data.features.messanger.dto.MessageList
import ru.ushell.app.data.features.messanger.remote.BodyRequestMessageChat
import ru.ushell.app.data.features.messanger.remote.MessageChatResponse
import ru.ushell.app.data.features.user.UserRepository
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Collections.reverse


class MessengerRepository(
    private val messengerLocalDataSource: MessengerLocalDataSource,
    private val messengerRemoteDataSource: MessengerRemoteDataSource,
    private val userRepository: UserRepository
) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    suspend fun getInfoUserMessenger() = messengerRemoteDataSource.getInfoUserMessenger().also {
        userRepository.setChatId(it.id)
    }

    suspend fun getMessageChat(senderId:String, recipientId:String) {
        val body = BodyRequestMessageChat(senderId=senderId,recipientId=recipientId)

        val result:List<MessageChatResponse> = messengerRemoteDataSource.getMessageChat(body)
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
        MessageList.setMessageList(messages)
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

}