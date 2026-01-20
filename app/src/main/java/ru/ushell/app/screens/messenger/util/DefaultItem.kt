package ru.ushell.app.screens.messenger.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ushell.app.ui.theme.StartScreenButtonText

@Composable
fun DefaultAvatar(
    nameUser: String,
    modifier: Modifier = Modifier
){
    val words = nameUser.trim().split(" ").filter { it.isNotEmpty() }

    val char =
        if (words.isEmpty()) "" else
        words.take(if (words.size == 1) 1 else 2)
            .joinToString("") { it.first().uppercaseChar().toString() }

    Row(
        modifier = modifier
            .clip(CircleShape)
            .size(55.dp)
            .background(Color.DarkGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = char,
            style = StartScreenButtonText,
            fontSize = 25.sp
        )
    }
}

@Preview
@Composable
fun DefaultAvatarPreview(){
    DefaultAvatar(" ")
}