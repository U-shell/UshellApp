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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.data.features.messenger.mappers.Message
import ru.ushell.app.screens.messenger.RoutesChat
import ru.ushell.app.screens.messenger.dialog.panel.InputPanel
import ru.ushell.app.screens.messenger.message.Messages
import ru.ushell.app.screens.messenger.view.MessengerUiState
import ru.ushell.app.screens.messenger.view.MessengerViewModel
import ru.ushell.app.ui.theme.BriefDialog
import ru.ushell.app.ui.theme.TitleDialog
import java.time.OffsetDateTime

@Composable
fun DialogScreen(
    navController: NavHostController,
    nameSenderUser: String,
    viewModel: MessengerViewModel = hiltViewModel()
) {

    LaunchedEffect(nameSenderUser) {
        viewModel.loadChat(nameSenderUser)
    }

    // Подключаемся при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    // Отключаемся при закрытии
    DisposableEffect(Unit) {
        onDispose {
            viewModel.disconnect()
        }
    }

    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    DialogContext(
        navController=navController,
        nameSenderUser=nameSenderUser,
        uiState=uiState,
        scrollState=scrollState,
        onMessageSent = { content ->
            viewModel.sendMessage(recipientId = nameSenderUser, message = content)
            scope.launch {
                if (scrollState.firstVisibleItemIndex > 0) {
                    scrollState.scrollToItem(0)
                }
            }
        },
        onResetScroll = {
            scope.launch {
                scrollState.scrollToItem(0)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogContext(
    navController: NavHostController,
    nameSenderUser: String,
    uiState: MessengerUiState,
    scrollState: LazyListState,
    onMessageSent: (String) -> Unit,
    onResetScroll: () -> Unit,
){

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TitlePanel(
                nameSenderUser,
                navController
            )
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime)
    ) { paddingValues ->
        when(uiState){
            is MessengerUiState.Empty -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Нет данных")
                }
            }
            is MessengerUiState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MessengerUiState.Error -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Ошибка: ${uiState.message}")
                }
            }
            is MessengerUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Messages(
                        messages = uiState.messageList,
                        scrollState = scrollState,
                        modifier = Modifier.weight(1f)
                    )
                    InputPanel(
                        onMessageSent = onMessageSent,
                        resetScroll = onResetScroll
                    )
                }
            }
        }
    }
}

@Composable
fun TitlePanel(
    nameSenderUser: String,
    navController: NavHostController
){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF2C0646),
                shape = RoundedCornerShape(
                    bottomStart = 25.dp,
                    bottomEnd = 25.dp
                )
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
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

            InfoPerson(
                nameSenderUser,
                Modifier
                    .weight(1f)
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(20.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.icon_cercal_menu),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun InfoPerson(
    nameSenderUser: String,
    modifier: Modifier = Modifier
){

    Row(
        modifier = modifier
            .padding(start = 30.dp)
            .fillMaxWidth()
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Icon(
            painter = painterResource(R.drawable.bottom_ic_profile_focused),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = nameSenderUser,
                style = TitleDialog,
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        bottom = 5.dp
                    )
            )

            Text(
                text = "online",
                style = BriefDialog
            )
        }

    }
}

@Preview
@Composable
fun DialogScreenPreview(){
    val navController = rememberNavController()

    val mockMessages = listOf(
        Message(
            author = false,
            content = "Привет! Как дела?",
            timestamp = OffsetDateTime.now().minusMinutes(5)
        ),
        Message(
            author = true,
            content = "Всё отлично! А у тебя?",
            timestamp = OffsetDateTime.now().minusMinutes(4)
        ),
        Message(
            author = false,
            content = "Тоже нормально 😊",
            timestamp = OffsetDateTime.now().minusMinutes(3)
        )
    )

    val mockUiState = MessengerUiState.Success(mockMessages)

    DialogContext(
        navController=navController,
        nameSenderUser ="titel",
        uiState = mockUiState,
        scrollState = rememberLazyListState(),
        onMessageSent = {},
        onResetScroll = {}
    )
}

@Preview
@Composable
fun TopPanelDialogPreview(){
    val navController = rememberNavController()

    TitlePanel(
        nameSenderUser ="titel",
        navController=navController
    )
}
