package ru.ushell.app.data.features.messenger.mappers

import android.net.Uri
import androidx.compose.runtime.Immutable
import ru.ushell.app.data.features.messenger.dto.MessageType
import java.time.OffsetDateTime

@Immutable
data class Message(
    val author: Boolean,
    val message: String,
    var type: MessageType,
    val timestamp: OffsetDateTime,
    val uri: Uri? = null,
    var file: ByteArray? = null,
    var fileName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (author != other.author) return false
        if (message != other.message) return false
        if (type != other.type) return false
        if (timestamp != other.timestamp) return false
        if (uri != other.uri) return false
        if (!file.contentEquals(other.file)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = author.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + (uri?.hashCode() ?: 0)
        result = 31 * result + (file?.contentHashCode() ?: 0)
        return result
    }

}