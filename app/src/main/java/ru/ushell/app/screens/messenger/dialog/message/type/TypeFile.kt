package ru.ushell.app.screens.messenger.dialog.message.type

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ushell.app.R
import ru.ushell.app.screens.messenger.util.parameterFile

@Composable
fun FileMessage(
    timeMessage: String,
    fileUri: Uri,
    message: String = "",
    modifier: Modifier = Modifier,
){
    if(fileUri ==  Uri.EMPTY) return

    val parameter = parameterFile(fileUri)
    val fileName = parameter[0]
    val fileSize = parameter[1]
    val fileExtension = parameter[2]

    TextMessage(
        timeMessage = timeMessage,
        message = message
    ) {
        Row(
            modifier = modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                contentPadding = PaddingValues(
                    start = 0.dp,
                    top = 0.dp,
                    end = 0.dp,
                    bottom = 0.dp,
                ),
                modifier = Modifier
                    .padding(end = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color.White)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.message_icon_files),
                            tint = Color.Black,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(
                                start = 10.dp
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = fileName,
                            color = Color.Black,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = fileSize,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = fileExtension,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.Top)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_cercal_menu),
                    tint = Color.Black,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 1.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun FileMessagePreview(){
    FileMessage(
        timeMessage = "12:30",
        fileUri =  Uri.EMPTY
    )
}