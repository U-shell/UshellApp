package ru.ushell.app.screens.messenger.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ushell.app.R
import ru.ushell.app.screens.messenger.RoutesChat
import ru.ushell.app.screens.messenger.view.MessengerViewModel
import ru.ushell.app.ui.theme.ChatNotingBackground
import ru.ushell.app.ui.theme.NameChatDes
import ru.ushell.app.ui.theme.NameChatElected
import ru.ushell.app.ui.theme.NameChatTitle
import ru.ushell.app.ui.theme.UshellBackground

@Composable
fun ChatItemElected(
    nameChat: String = "Name",
    noise: String = "88",
    status: Int = 1,
){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .timedClick(
                timeInMillis = 1000,
            ) { passed: Boolean ->
                Toast
                    .makeText(
                        context,
                        "Pressed longer than 1000 $passed",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
            .padding(
                start = 10.dp)
            .clip(RoundedCornerShape(15.dp))
            .height(120.dp)
            .width(90.dp)
            .background(
                color = UshellBackground
            )
        ,
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            Modifier.padding(
                top=15.dp
            )
        ) {
            ImageItem(
                noise,
                status
            )
        }
        Text(
            text = nameChat,
            style = NameChatElected
        )
    }
}
@Composable
fun ImageItem(
    noise:String,
    statusNoise: Int
){
    val status = mapOf(0 to Color.Transparent, 1 to ChatNotingBackground)

    ConstraintLayout(
        modifier = Modifier
            .padding(
                start = 5.dp,
                end = 5.dp,
                top = 5.dp,
                bottom = 5.dp
            )
    ){
        val (favicon) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(favicon) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .zIndex(1f)
                .offset(
                    x=5.dp,
                    y=2.dp
                )
                .size(15.dp)
                .clip(shape = CircleShape)
                .background(
                    color = status[statusNoise]!!,
                    shape = CircleShape
                ),
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = noise,
                    fontSize = 9.sp,
                    color = Color.White
                )
            }
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .size(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(
                painter = painterResource(R.drawable.bottom_ic_profile_focused),
                contentDescription = null,
                modifier = Modifier
                    .size(66.dp)
                    .background(color = Color.White)
            )
        }
    }
}

@Composable
fun ChatItemList(
    navController: NavHostController,
    nameSenderUser: MutableState<String>,
    titleChat: String = "ChatTitle",
    lastUser: String = "User",
    lastMessage: String = "MessageMessageMessageMessag",
    noise: String = "88",
    statusNoise: Boolean = true,
    viewModule: MessengerViewModel = hiltViewModel()

){
    var status by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                status = true
            },
        shape= RoundedCornerShape(0.dp),
        colors= CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ){
        ChatItemListContext(
            titleChat = titleChat,
            lastUser = lastUser,
            lastMessage = lastMessage,
            noise = noise,
            statusNoise = statusNoise
        )
    }
    if(status){
        var isRefreshing by remember { mutableStateOf(true) }

        LaunchedEffect(isRefreshing){
            if (isRefreshing) {
                delay(2000)
                isRefreshing = false
            }
        }

        navController.navigate(RoutesChat.ScreenDialog.route)
        nameSenderUser.value = lastMessage
        status = false
    }
}

@Composable
fun ChatItemListContext(
    titleChat: String,
    lastUser: String,
    lastMessage: String,
    noise: String,
    statusNoise: Boolean
){
    val status = mapOf(false to Color.Transparent, true to ChatNotingBackground)
    //TODO: переделать в card как в LessonItemView в LessonItem
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 5.dp
            ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(R.drawable.bottom_ic_profile_focused),
                        contentDescription = null,
                        modifier = Modifier
                            .size(55.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
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
                                    text = titleChat,
                                    style = NameChatTitle
                                )
                            }
                            Row {
                                Text(
                                    text = lastUser,
                                    style = NameChatDes
                                )
                                Text(
                                    text = ": ",
                                    style = NameChatDes
                                )
                                Text(
                                    text = lastMessage,
                                    style = NameChatDes,
                                    maxLines = 1
                                )
                            }

                        }
                    }
                }

                if(statusNoise) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(25.dp)
                            .background(color = status[statusNoise]!!)
                            .align(Alignment.CenterVertically)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = noise,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(2.dp)
                    .background(Color.LightGray)
            )
        }
    }
}
// TODO: длительное зажатие
@Composable
fun Modifier.timedClick(
    timeInMillis: Long,
    interactionSource: MutableInteractionSource = remember {MutableInteractionSource()},
    onClick: (Boolean) -> Unit
) = composed {

    var timeOfTouch = -1L
    LaunchedEffect(key1 = timeInMillis, key2 = interactionSource) {
        interactionSource.interactions
            .onEach { interaction: Interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        timeOfTouch = System.currentTimeMillis()
                    }
                    is PressInteraction.Release -> {
                        val currentTime = System.currentTimeMillis()
                        onClick(currentTime - timeOfTouch > timeInMillis)
                    }
                    is PressInteraction.Cancel -> {
                        onClick(false)
                    }
                }

            }
            .launchIn(this)
    }

    Modifier.clickable(
//        interactionSource = interactionSource,
//        indication = rememberRipple(),
        onClick = {}
    )
}
@Preview
@Composable
fun ChatItemListPreview(){
    val navController = rememberNavController()
    ChatItemList(
        navController = navController,
        nameSenderUser = remember { mutableStateOf("") }
    )
}
@Preview
@Composable
fun ChatItemElectedPreview(){
    ChatItemElected()
}