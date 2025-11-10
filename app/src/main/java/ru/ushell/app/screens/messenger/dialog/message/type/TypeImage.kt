package ru.ushell.app.screens.messenger.dialog.message.type

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter

@Composable
fun ImageMessage(
    timeMessage: String,
    imageUri: Uri,
    message: String = "",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    TextMessage(
        timeMessage = timeMessage,
        message = message
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            var painterState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

            AsyncImage(
                model = imageUri,
                contentDescription = "Отправленное изображение",
                contentScale = ContentScale.Crop,
                onState = { state ->
                    painterState = state
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
            )

            when (painterState) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.Center)
                    )
                }
                else -> {
                }
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