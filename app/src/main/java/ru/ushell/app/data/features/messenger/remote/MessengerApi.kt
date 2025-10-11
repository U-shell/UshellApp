package ru.ushell.app.data.features.messenger.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MessengerApi {

    @GET("/messenger/users/me")
    suspend fun getInfoUserMessenger(): InfoUserMessengerResponse

    @GET("/messenger/users/summaries")
    suspend fun getAllUserSummaries(): AllUserChatResponse

    @POST("/messenger/messages/count")
    suspend fun getCountNewMessage(@Body body: BodyRequestMessageChat): String

    @POST("/messenger/messages/messages")
    suspend fun getMessageChat(@Body body: BodyRequestMessageChat): List<MessageChatResponse>

    @GET("/messenger/messages/{id}")
    suspend fun findMessage()

    @GET("/messenger/users/all")
    suspend fun getAllUser();

    @GET("/messenger/users/{username}")
    suspend fun getUserSummary()
}