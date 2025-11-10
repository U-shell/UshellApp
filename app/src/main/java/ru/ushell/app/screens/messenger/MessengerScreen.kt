package ru.ushell.app.screens.messenger

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import ru.ushell.app.R
import ru.ushell.app.screens.messenger.chat.MessengerBodyContext
import ru.ushell.app.screens.messenger.dialog.DialogScreen
import ru.ushell.app.screens.messenger.util.recipientIdShare
import ru.ushell.app.screens.profile.drawer.models.device.qrscanner.QRScannerScreen
import ru.ushell.app.screens.utils.TopPanelScreen
import ru.ushell.app.screens.utils.TypeScanner
import ru.ushell.app.screens.utils.backgroundImagesSmall
import ru.ushell.app.ui.theme.SystemBarColorTheme
import ru.ushell.app.ui.theme.TimeTableText
import ru.ushell.app.ui.theme.UshellAppTheme

@Composable
fun ChatScreen(
    bottomBarEnabled: MutableState<Boolean>,
) {
    var isRefreshing by remember { mutableStateOf(true) }
    val navController = rememberNavController()
    val nameSenderUser = remember { mutableStateOf("") }

    LaunchedEffect(isRefreshing){
        if (isRefreshing) {
            delay(2000)
            isRefreshing = false
        }
    }
    ChatNav(
        navController=navController,
        bottomBarEnabled=bottomBarEnabled,
        nameSenderUser=nameSenderUser
    )
}

@Composable
fun ChatNav(
    navController: NavHostController,
    bottomBarEnabled: MutableState<Boolean>,
    nameSenderUser: MutableState<String>,
){

    NavHost(
        navController = navController,
        startDestination = RoutesChat.ScreenChat.route
    ) {
        composable(RoutesChat.ScreenChat.route) {
            bottomBarEnabled.value = true
            UshellAppTheme {
                ChatContext(
                    navController = navController,
                    nameSenderUser = nameSenderUser
                )
            }
        }
        composable(RoutesChat.ScreenDialog.route) {
            bottomBarEnabled.value = false

            SystemBarColorTheme {
                DialogScreen(
                    nameSenderUser = nameSenderUser.value,
                    navController = navController
                )
            }
        }
        composable(RoutesChat.ScreenQRShare.route) {
            bottomBarEnabled.value = false
            recipientIdShare = nameSenderUser.value
            QRScannerScreen(
                navControllerDevice = navController,
                typeScanner = TypeScanner.SEND_FILE,
            )
        }
    }
}

@Composable
fun ChatContext(
    navController: NavHostController,
    nameSenderUser: MutableState<String>,
) {
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
                Column(){
                    MessengerBodyContext(
                        navController = navController,
                        nameSenderUser = nameSenderUser,
                    )
                }
            }
        }
    }
}

@Composable
fun TopPanel() {
    TopPanelScreen(
        titleContext = { TitleChat() }
    ) {
    }
}

@Composable
fun TitleChat() {
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
//            SearchPanel()
        }
    }
}

sealed class RoutesChat(
    val route: String,
) {
    data object ScreenChat : RoutesChat("chat_chat")
    data object ScreenDialog : RoutesChat("chat_dialog")
    data object ScreenQRShare : RoutesChat("qrshare")
}

@Preview
@Composable
fun ChatScreenPreview(){
    val bottomBarEnabled = remember { mutableStateOf(true) }
    ChatScreen(
        bottomBarEnabled=bottomBarEnabled
    )
}