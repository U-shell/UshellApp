package ru.ushell.app.screens.messenger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.data.features.messanger.dto.Message
import java.time.OffsetDateTime

@Composable
fun MessageItem(
    msg: Message,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
) {

    val spaceBetweenAuthors =
        if (isLastMessageByAuthor)
            Modifier.padding(top = 10.dp)
        else
            Modifier.padding(top = 3.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(spaceBetweenAuthors)
            .then(
                if(msg.author)
                    Modifier.padding(
                        start = 50.dp,
                        end = 10.dp
                    )
                else
                    Modifier.padding(
                        start = 10.dp,
                        end = 50.dp,
                    )
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            if(msg.author){
                Arrangement.End
            }else{
                Arrangement.Start
            }
    ) {
        ChatItemBubble(
            message = msg,
        )
    }
}

@Composable
fun ChatItemBubble(
    message: Message,
) {

    val backgroundBubbleColor = if (message.author) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape =
                if (message.author) {
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 4.dp
                    )
                }else{
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 10.dp
                    )
                }
        ) {
            ClickableMessage(
                message = message,
            )
        }
            //TODO: если сообшение явл изображением
//        message.image?.let {
//            Spacer(modifier = Modifier.height(4.dp))
//            Surface(
//                color = backgroundBubbleColor,
//                shape = ChatBubbleShape
//            ) {
//                Image(
//                    painter = painterResource(it),
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier.size(160.dp),
//                    contentDescription = stringResource(id = R.string.attached_image)
//                )
//            }
//        }
    }
}


@Composable
fun ClickableMessage(
    message: Message,
) {

    ConstraintLayout(
        modifier = Modifier
    ) {
        val (bodyMessage, timeMessage) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(bodyMessage) {
                }

        ){
            BodyMessage(
                message = message,
                isUserMe = message.author
            )
        }
        Box(
            modifier = Modifier
                .constrainAs(timeMessage) {
                    top.linkTo(bodyMessage.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 5.dp)

        ){
            TimeMessage(
                message = message,
            )
        }

    }
}

@Composable
fun BodyMessage(
    message: Message,
    isUserMe: Boolean
){
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = message.content,
        primary = isUserMe
    )

    Text(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(
            start = 5.dp,
            end = 5.dp,
            top = 5.dp
        ),
//        onClick = {
//            styledMessage
//                .getStringAnnotations(start = it, end = it)
//                .firstOrNull()
//                ?.let { annotation ->
//                    when (annotation.tag) {
//                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
//                        else -> Unit
//                    }
//                }
//        }
    )
}

@Composable
fun TimeMessage(
    message: Message
){
    val minute = if(message.timestamp.minute.toString().length == 1){
        "0"+message.timestamp.minute.toString()
    }
    else{
        message.timestamp.minute.toString()
    }
    val hour = if(message.timestamp.hour.toString().length == 1){
        "0"+message.timestamp.hour.toString()
    }
    else{
        message.timestamp.hour.toString()
    }

    val timeMessage = "${hour}:${minute}"
    Box(
        modifier = Modifier
            .padding(end = 1.dp)
    ){
        Text(
            text = timeMessage,
            style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        )
    }
}

@Preview
@Composable
fun ChatItemBubblePreview(){
    ChatItemBubble(
        message = Message(
            author = true,
            content = "hi",
            timestamp = OffsetDateTime.now()
        ),
    )
}