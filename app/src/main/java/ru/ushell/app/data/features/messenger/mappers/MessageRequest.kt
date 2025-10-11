package ru.ushell.app.data.features.messenger.mappers

data class MessageRequest(
    val senderId: String,
    val recipientId: String,
    val message: String
)