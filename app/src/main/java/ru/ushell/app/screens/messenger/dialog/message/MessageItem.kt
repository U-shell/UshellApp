package ru.ushell.app.screens.messenger

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import ru.ushell.app.data.features.messenger.dto.MessageType
import ru.ushell.app.data.features.messenger.mappers.Message
import ru.ushell.app.screens.messenger.dialog.message.type.FileMessage
import ru.ushell.app.screens.messenger.dialog.message.type.ImageMessage
import ru.ushell.app.screens.messenger.dialog.message.type.TextMessage

import java.time.OffsetDateTime
import kotlin.toString

@Composable
fun MessageItem(
    messageComponent: Message,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .then(
                if (messageComponent.author) {
                    Modifier.padding(
                        start = 50.dp,
                        end = 10.dp
                    )
                } else {
                    Modifier.padding(
                        start = 10.dp,
                        end = 50.dp,
                    )
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            if (messageComponent.author) {
                Arrangement.End
            } else {
                Arrangement.Start
            }
    ) {
        MessageItemContent(
            messageComponent = messageComponent,
        )
    }
}

@Composable
private fun MessageItemContent(
    messageComponent: Message,
    modifier: Modifier = Modifier
) {
    val message = messageComponent.message
    val timeMessage = messageComponent.timestamp.formatAsTime()

    val bubbleShape = if (messageComponent.author) {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 4.dp, bottomEnd = 10.dp)
    }

    Surface(
        shape = bubbleShape,
        contentColor = Color(0xFFD7D7D7),
        color = Color(0xFFD7D7D7),
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        when(messageComponent.type) {
            MessageType.TEXT -> {
                TextMessage(
                    timeMessage = timeMessage,
                    message = message
                )
            }
            MessageType.IMAGE -> {
                ImageMessage(
                    timeMessage = timeMessage,
                    imageUri = messageComponent.uri ?: Uri.EMPTY,
                    message = message
                )
            }
            MessageType.FILE -> {
                FileMessage(
                    timeMessage = timeMessage,
                    fileUri = messageComponent.uri ?: Uri.EMPTY,
                    message = message
                )
            }
            MessageType.VIDEO -> TODO()
            MessageType.VOICE -> TODO()
            MessageType.UNKNOWN -> TODO()
        }
    }
}

fun OffsetDateTime.formatAsTime(): String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}


@Preview
@Composable
fun MessageItemTextPreview(){
    MessageItem(
        messageComponent = Message(
            author = true,
            message = "Привет",
            type = MessageType.TEXT,
            timestamp = OffsetDateTime.now()
        ),
    )
}

@Preview
@Composable
fun MessageItemImagePreview(){
    MessageItem(
        messageComponent = Message(
            author = true,
            message = "Привет",
            type = MessageType.IMAGE,
            timestamp = OffsetDateTime.now()
        ),
    )
}

@Preview
@Composable
fun MessageItemFilePreview(){
    MessageItem(
        messageComponent = Message(
            author = true,
            message = "Привет",
            type = MessageType.FILE,
            timestamp = OffsetDateTime.now()
        ),
    )
}
