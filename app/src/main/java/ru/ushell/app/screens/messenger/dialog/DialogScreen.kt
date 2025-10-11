package ru.ushell.app.screens.messenger.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.data.features.messanger.dto.Message
import ru.ushell.app.data.features.messanger.dto.MessageList
import ru.ushell.app.old.ui.screens.chatScreen.RoutesChat
import ru.ushell.app.old.ui.screens.chatScreen.message.Messages
import ru.ushell.app.ui.theme.BrefDialog
import ru.ushell.app.ui.theme.TitleDialog
import ru.ushell.app.ui.theme.UshellBackground
import java.time.OffsetDateTime


@Composable
fun DialogScreen(
    navController: NavHostController,
    nameSenderUser: String,
) {
    val message = MessageList()

    DialogScreenContext(
        navController=navController,
        messageList=message,
        nameSenderUser=nameSenderUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogScreenContext(
    navController: NavHostController,
    messageList: MessageList,
    nameSenderUser: String,
){

    val scope = rememberCoroutineScope()
    val timeNow = OffsetDateTime.now();

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopPanelDialog(
                chatName = nameSenderUser,
                navController = navController
            )
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Messages(
                messages = messageList.messageList,
                scrollState = scrollState,
                modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.End)
            )
            UserInput(
                onMessageSent = { content ->
                    messageList.addMessage(
                        Message(
                            true,
                            content,
                            timeNow
                        )
                    )

//                    val chatDeliver =
//                        ChatDeliver(
//                            getKeyIdUserChat(),
//                            messageList.channelName,
//                            content
//                        )
//                    chatDeliver.connect(webSocketAddress)
//                    chatDeliver.disconnect()
                },

                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
//                    .imePadding()
            )
        }
    }
}

@Composable
fun TopPanelDialog(
    chatName: String,
    navController: NavHostController
){
    val shape = 25.dp

    Box(
        modifier = Modifier
            .background(
                color = UshellBackground,
                shape = RoundedCornerShape(
                    bottomStart = shape,
                    bottomEnd = shape
                )
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Box(){
                IconButton(
                    onClick = {
                        navController.navigate(RoutesChat.ScreenChat.route)
                    },
                    modifier = Modifier
                        .size(20.dp)
                ) {
                    Icon(
                        painterResource(id= R.drawable.chat_arrow),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 30.dp)

                ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                color = Color.White,
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.bottom_ic_profile_focused),
                            contentDescription = null,
                            modifier = Modifier
                                .size(55.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 10.dp
                            )
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                            ) {
                                Text(
                                    text = chatName,
                                    style = TitleDialog
                                )
                            }
                            Row {
                                Text(
                                    text = "lastUser",
                                    style = BrefDialog
                                )
                            }

                        }
                    }
                }
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
    }
}

@Composable
fun BottomPanel(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Gray
            )
    ){
        Row(
            modifier = Modifier
                .padding(
                    start = 5.dp,
                    end = 5.dp
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painterResource(id = R.drawable.icon_cercal_menu),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Box(

            ){
                TextField(
                    value = "s",
                    onValueChange = {},
                    placeholder = {
                        "d"
                    },
                    label = {
                        "f"
                    },
                    colors = TextFieldDefaults.colors(
                        focusedPlaceholderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.DarkGray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White.copy(alpha = 0.8f)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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
    }
}

@Preview
@Composable
fun DialogScreenPreview(){
    val navController = rememberNavController()

    DialogScreen(
        navController=navController,
        nameSenderUser="nameSenderUser"
    )
}

@Preview
@Composable
fun TopPanelDialogPreview(){
    val navController = rememberNavController()

    TopPanelDialog(
        navController=navController,
        chatName="titel"
    )
}
@Preview
@Composable
fun BottomPanelPreview(){
    BottomPanel()
}