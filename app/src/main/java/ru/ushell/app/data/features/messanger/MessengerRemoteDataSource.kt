package ru.ushell.app.data.features.messanger

import ru.ushell.app.data.features.messanger.remote.AllUserChatResponse
import ru.ushell.app.data.features.messanger.remote.BodyRequestMessageChat
import ru.ushell.app.data.features.messanger.remote.InfoUserMessengerResponse
import ru.ushell.app.data.features.messanger.remote.MessageChatResponse

interface MessengerRemoteDataSource {

    suspend fun getInfoUserMessenger(): InfoUserMessengerResponse

    suspend fun getAllUsers(): AllUserChatResponse

    suspend fun getMessageChat(body: BodyRequestMessageChat): List<MessageChatResponse>

    suspend fun getCountNewMessage(body: BodyRequestMessageChat): Int

}