package ru.ushell.app.data.features.messenger.retrofir

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import ru.ushell.app.data.features.messenger.MessengerRemoteDataSource
import ru.ushell.app.data.features.messenger.dto.AllUserChatResponse
import ru.ushell.app.data.features.messenger.dto.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.dto.InfoUserMessengerResponse
import ru.ushell.app.data.features.messenger.dto.MessageChatResponse
import ru.ushell.app.data.features.messenger.remote.MessengerApi

class RetrofitMessengerDataSource(
    val messengerApi: MessengerApi
) : MessengerRemoteDataSource {

    override suspend fun getMessageChat(body: BodyRequestMessageChat): List<MessageChatResponse> =
        messengerApi.getMessageChat(body)

    override suspend fun getCountNewMessage(body: BodyRequestMessageChat): Int =
        messengerApi.getCountNewMessage(body).toInt()

    override suspend fun uploadFile(senderId: String, resenderId: String, file: MultipartBody.Part) =
        messengerApi.uploadFile(senderId,resenderId, file)

    override suspend fun getDownloadFile(senderId: String, resenderId: String, fileName: String): Response<ResponseBody> =
        messengerApi.getDownloadFile(senderId,resenderId,fileName)

    override suspend fun getAllUsers(): AllUserChatResponse =
        messengerApi.getAllUserSummaries()

    override suspend fun getInfoUserMessenger(): InfoUserMessengerResponse =
        messengerApi.getInfoUserMessenger()
}
