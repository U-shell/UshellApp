package ru.ushell.app.ui.screens.chatScreen.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.models.modelChat.chat.Chat.ChatList
import ru.ushell.app.models.modelChat.chat.Chat.getChatPopulation
import ru.ushell.app.ui.theme.ChatIFBackground
import ru.ushell.app.ui.theme.UshellBackground

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MessengerBodyContext(
    navController: NavHostController,
    nameSenderUser: MutableState<String>,
){
    Column(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
            .navigationBarsPadding()
            .background(UshellBackground)
    ) {
//        TopPanelBat()
        BodyContext(
            navController = navController,
            nameSenderUser = nameSenderUser,
        )
    }
}

@Composable
fun TopPanelBat(
) {
    Row(
        modifier = Modifier
        .fillMaxWidth()
            .padding(
                start = 30.dp,
                end = 30.dp,
                top = 5.dp
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        TextButton(
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.your),
                color = Color.White
            )
        }
        TextButton(
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.elected),
                color = Color.White
            )
        }
        TextButton(
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.group),
                color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContext(
    navController: NavHostController,
    nameSenderUser: MutableState<String>,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val density = LocalDensity.current
    var sheetPeekHeight by remember { mutableStateOf(0.dp) }
    var bottomSheetHeight by remember { mutableStateOf(0.dp) }

    // TODO(пересмотерть построение, почему черный меняеться на белый)
    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            ),
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .onPlaced {
                        if (bottomSheetHeight == 0.dp) {
                            bottomSheetHeight = with(density) {
                                    it.size.height.toDp()
                            }
                        }
                    }
                    .padding(bottom = 200.dp)
                    .fillMaxSize()
                    .navigationBarsPadding()
            ){
//                Scaffold(
//                    contentColor = Color.Black,
//                    containerColor = Color.Transparent,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.White)
////                        .padding(bottom = 100.dp)
//                        .navigationBarsPadding()
//                ){ innerPaddingModifier ->
                    ListChats(
                        modifier = Modifier
                            .fillMaxSize()
//                            .padding(innerPaddingModifier)
                            .navigationBarsPadding()
                        ,
                        navController = navController,
                        nameSenderUser=nameSenderUser

                    )
//                }
            }
        },
        topBar={TopPanelBat()},
        sheetPeekHeight = (bottomSheetHeight - sheetPeekHeight),
        containerColor = ChatIFBackground,
        contentColor = Color.Black,
        sheetContainerColor=Color.White
    ) {
        innerPadding ->
        LazyRow(
            modifier = Modifier
                .onPlaced {
                    if (sheetPeekHeight == 0.dp) {
                        sheetPeekHeight = with(density) {
                            it.size.height.toDp()
                        }
                    }
                }
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(top=10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
//            TODO: сделать pin
            items(0) {
                index ->
                    ChatItemElected()
            }
        }
    }
}


@Composable
fun ListChats(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    nameSenderUser: MutableState<String>,
){
    val chats = getChatPopulation()

    if(chats.isNotEmpty()){
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(chats.size) { index ->
                Box() {
                    //TODO: написать функцию нажатия на элемент
                    ChatItemList(
                        navController = navController,
                        nameSenderUser = nameSenderUser,
                        titleChat = chats[index].name,
                        noise = chats[index].countNewMessage.toString(),
                        statusNoise = chats[index].countNewMessage != 0,
                        lastMessage = chats[index].recipientId
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MessengerBodyContextPreview(){
    val navController = rememberNavController()
    val density = LocalDensity.current

    MessengerBodyContext(
        navController = navController,
        nameSenderUser = remember { mutableStateOf("") },
    )
}