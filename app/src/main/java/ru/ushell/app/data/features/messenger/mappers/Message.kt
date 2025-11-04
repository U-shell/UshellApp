package ru.ushell.app.data.features.messenger.mappers

import androidx.compose.runtime.Immutable
import java.time.OffsetDateTime

@Immutable
data class Message(
    val author: Boolean,
    val content: String,
    val timestamp: OffsetDateTime,
)

