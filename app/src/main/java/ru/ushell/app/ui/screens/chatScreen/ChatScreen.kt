package ru.ushell.app.ui.screens.chatScreen

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.R
import ru.ushell.app.ui.screens.SearchPanel
import ru.ushell.app.ui.screens.TopPanelScreen
import ru.ushell.app.ui.screens.backgroundImagesSmall
import ru.ushell.app.ui.theme.TimeTableText

@Composable
fun ChatScreen() {
    ChatScreenContext()
}

@Composable
fun ChatScreenContext(){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(bottom = 70.dp)
            .navigationBarsPadding()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout {
            val (backgroundImage, topPanel, messenger) = createRefs()
            val barrier = createBottomBarrier(messenger)
            val height = 200.dp

            Box(
                modifier = Modifier
                    .constrainAs(backgroundImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .backgroundImagesSmall(
                        height = height,
                        radiusShape = 0.dp
                    )
            )
            Box(
                modifier = Modifier
                    .constrainAs(topPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(bottom = 30.dp)
            ){
                TopPanel()
            }
            Box(
                modifier = Modifier
                    .constrainAs(messenger) {
                        top.linkTo(topPanel.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(barrier)
                    }
                    .navigationBarsPadding()
            ) {
                MessengerBodyContext()
            }
        }
    }
}

@Composable
fun TopPanel() {
    TopPanelScreen(
        titleContext = { TT_Chat() }
    ) {
    }
}

@Composable
fun TT_Chat() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box{
                Text(
                    text = stringResource(id = R.string.chat),
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
fun ChatScreenPreview(){
    ChatScreen()
}