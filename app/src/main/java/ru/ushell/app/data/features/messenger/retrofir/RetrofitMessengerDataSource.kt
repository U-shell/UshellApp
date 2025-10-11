package ru.ushell.app.data.features.messenger.retrofir

import ru.ushell.app.data.features.messenger.MessengerRemoteDataSource
import ru.ushell.app.data.features.messenger.remote.AllUserChatResponse
import ru.ushell.app.data.features.messenger.remote.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.remote.InfoUserMessengerResponse
import ru.ushell.app.data.features.messenger.remote.MessageChatResponse
import ru.ushell.app.data.features.messenger.remote.MessengerApi

class RetrofitMessengerDataSource(
    val messengerApi: MessengerApi
) : MessengerRemoteDataSource {

    override suspend fun getInfoUserMessenger(): InfoUserMessengerResponse = messengerApi.getInfoUserMessenger()

    override suspend fun getAllUsers(): AllUserChatResponse = messengerApi.getAllUserSummaries()

    override suspend fun getMessageChat(body: BodyRequestMessageChat): List<MessageChatResponse> =
        messengerApi.getMessageChat(body)

    override suspend fun getCountNewMessage(body: BodyRequestMessageChat): Int = messengerApi.getCountNewMessage(body).toInt()
}
