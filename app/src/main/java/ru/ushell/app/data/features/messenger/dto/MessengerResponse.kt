package ru.ushell.app.data.features.messenger.dto

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
    val type: MessageType,
    val fileName: String,
    val message: String,
    val status: String,
    val timestamp: String,
)

