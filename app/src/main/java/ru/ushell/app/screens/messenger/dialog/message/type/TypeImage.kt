package ru.ushell.app.screens.messenger.dialog.message.type

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ImageMessage(
    timeMessage: String,
    imageUri: Uri,
    message: String = "",
    modifier: Modifier = Modifier,
) {

    TextMessage(
        timeMessage = timeMessage,
        message = message
    ) {
        Box(
            modifier = modifier
        ) {
            Column {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Отправленное изображение",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@SuppressLint("UseKtx")
@Preview
@Composable
fun ImageMessagePreview(){
    val context = LocalContext.current
    val imageUri = Uri.parse("android.resource://" + context.packageName + "/res/drawable/background_app")

    ImageMessage(
        timeMessage = "12:30",
        imageUri = imageUri
    )
}