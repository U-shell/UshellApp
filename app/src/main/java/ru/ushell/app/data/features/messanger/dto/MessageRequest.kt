package ru.ushell.app.data.features.messanger.dto

data class MessageRequest(
    val senderId: String,
    val recipientId: String,
    val message: String
)