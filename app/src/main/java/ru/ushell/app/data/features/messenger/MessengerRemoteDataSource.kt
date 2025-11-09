package ru.ushell.app.data.features.messenger

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import ru.ushell.app.data.features.messenger.dto.AllUserChatResponse
import ru.ushell.app.data.features.messenger.dto.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.dto.InfoUserMessengerResponse
import ru.ushell.app.data.features.messenger.dto.MessageChatResponse

interface MessengerRemoteDataSource {

    suspend fun getMessageChat(body: BodyRequestMessageChat): List<MessageChatResponse>

    suspend fun getCountNewMessage(body: BodyRequestMessageChat): Int

    suspend fun uploadFile(senderId: String, resenderId: String, file: MultipartBody.Part)

    suspend fun getDownloadFile(senderId: String, resenderId: String, fileName: String): Response<ResponseBody>

    suspend fun getAllUsers(): AllUserChatResponse

    suspend fun getInfoUserMessenger(): InfoUserMessengerResponse

}