package ru.ushell.app.data.features.messenger.mappers

import ru.ushell.app.data.features.messenger.dto.MessageType

data class MessageRequest(
    val senderId: String,
    val recipientId: String,
    val type: MessageType,
    val message: String? = null,
    val fileName: String? = null
)