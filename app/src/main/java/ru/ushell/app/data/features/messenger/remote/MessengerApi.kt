package ru.ushell.app.data.features.messenger.remote

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Streaming
import ru.ushell.app.data.features.messenger.dto.AllUserChatResponse
import ru.ushell.app.data.features.messenger.dto.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.dto.InfoUserMessengerResponse
import ru.ushell.app.data.features.messenger.dto.MessageChatResponse

interface MessengerApi {

    @POST("/messenger/messages/messages")
    suspend fun getMessageChat(@Body body: BodyRequestMessageChat): List<MessageChatResponse>

    @POST("/messenger/messages/count")
    suspend fun getCountNewMessage(@Body body: BodyRequestMessageChat): String

    @Multipart
    @POST("/messenger/messages/file")
    suspend fun uploadFile(
        @Query("senderId") senderId: String,
        @Query("resenderId") resenderId: String,
        @Part file: MultipartBody.Part
    )

    @Streaming
    @GET("/messenger/messages/file/download")
    suspend fun getDownloadFile(
        @Query("senderId") senderId: String,
        @Query("resenderId") resenderId: String,
        @Query("file") fileName: String): Response<ResponseBody>


    @GET("/messenger/messages/{id}")
    suspend fun findMessage()


    @GET("/messenger/users/summaries")
    suspend fun getAllUserSummaries(): AllUserChatResponse

    @GET("/messenger/users/{username}")
    suspend fun getUserSummary()

    @GET("/messenger/users/me")
    suspend fun getInfoUserMessenger(): InfoUserMessengerResponse

    @GET("/messenger/users/all")
    suspend fun getAllUser();
}