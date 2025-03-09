package ru.ushell.app.ui.screens.roomScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.R
import ru.ushell.app.system.notifications.Push
import ru.ushell.app.ui.screens.SearchPanel
import ru.ushell.app.ui.screens.TopPanelScreen
import ru.ushell.app.ui.screens.backgroundImagesSmall
import ru.ushell.app.ui.theme.TimeTableText

@Composable
fun RoomScreen() {
    Push().sendNotification(LocalContext.current)
    RoomScreenContext()
}

@Composable
fun RoomScreenContext(){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(color = Color(0xFFE7E7E7))
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout {
            val (topPanel, lesson, backgroundImage) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(backgroundImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .backgroundImagesSmall(height = 160.dp)
            )
            Box(
                modifier = Modifier
                    .constrainAs(topPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                TopPanel()
            }
        }
    }
}

@Composable
fun TopPanel() {
    TopPanelScreen(
        titleContext = { TT_Room() }
    ) {

    }
}

@Composable
fun TT_Room() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box{
                Text(
                    text = stringResource(id = R.string.room),
                    style = TimeTableText
                )
            }
            Box {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painterResource(id = R.drawable.icon_cercal_menu),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
        Box(modifier = Modifier
            .padding(top = 5.dp)
        ) {
            SearchPanel()
        }
    }
}

@Preview
@Composable
fun RoomScreenPreview(){
    RoomScreen()
}