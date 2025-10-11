package ru.ushell.app.data.features.messenger.remote

data class InfoUserMessengerResponse(
    val id: String,
    val username:String,
    val name: String,
)

data class AllUserChatResponse(
    val listUsers: ArrayList<InfoUserMessengerResponse>?
)

data class BodyRequestMessageChat (
    val senderId: String,
    val recipientId: String
)

data class MessageChatResponse(
    val messageId: String,
    val senderId: String,
    val message: String,
    val status: String,
    val timestamp: String,
)

