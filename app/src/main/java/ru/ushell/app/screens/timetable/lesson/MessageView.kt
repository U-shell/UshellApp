package ru.ushell.app.screens.timetable.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ushell.app.R
import ru.ushell.app.ui.theme.TimeTableTextMessage
import ru.ushell.app.ui.theme.UshellBackground


@Composable
fun MessageView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 5.dp,
                end = 5.dp,
                top = 2.dp,
                bottom = 2.dp,
            )
            .background(
                color = UshellBackground,
                shape = RoundedCornerShape(10.dp)
            )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 10.dp,
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(id = R.string.timetable_no_lesson),
                style = TimeTableTextMessage
            )
        }
    }
}

@Preview
@Composable
fun MessageViewPreview(){
    MessageView()
}