package ru.ushell.app.ui.screens.chatScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.R
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
    Column(
        modifier = Modifier
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
    titleChat: String = "ChatTitle",
    lastUser: String = "User",
    lastMessage: String = "MessageMessageMessageMessag",
    noise: String = "88",
    statusNoise: Int = 1
){
    val status = mapOf(0 to Color.Transparent, 1 to ChatNotingBackground)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 10.dp
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
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(25.dp)
                        .background(color = status[statusNoise]!!)
                        .align(Alignment.CenterVertically)
                ){
                    Row(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = noise,
                            fontSize = 18.sp,
                            color = Color.White
                        )
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

@Preview
@Composable
fun ChatItemListPreview(){
    ChatItemList()
}
@Preview
@Composable
fun ChatItemElectedPreview(){
    ChatItemElected()
}