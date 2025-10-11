package ru.ushell.app.data.features.messenger

import ru.ushell.app.data.features.messenger.remote.AllUserChatResponse
import ru.ushell.app.data.features.messenger.remote.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.remote.InfoUserMessengerResponse
import ru.ushell.app.data.features.messenger.remote.MessageChatResponse

interface MessengerRemoteDataSource {

    suspend fun getInfoUserMessenger(): InfoUserMessengerResponse

    suspend fun getAllUsers(): AllUserChatResponse

    suspend fun getMessageChat(body: BodyRequestMessageChat): List<MessageChatResponse>

    suspend fun getCountNewMessage(body: BodyRequestMessageChat): Int

}