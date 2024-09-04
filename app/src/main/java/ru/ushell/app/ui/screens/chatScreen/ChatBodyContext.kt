package ru.ushell.app.ui.screens.chatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ushell.app.R
import ru.ushell.app.ui.theme.ChatIFBackground
import ru.ushell.app.ui.theme.UshellBackground

@Composable
fun MessengerBodyContext(){
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            ))
            .navigationBarsPadding()
            .background(UshellBackground)
    ){
        TopPanelBat()
        BodyContext()
    }
}

@Composable
fun TopPanelBat() {
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
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    var size by remember { mutableStateOf(1.dp) }
    // TODO(пересмотерть построение, почему черный меняеться на белый)
    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
        ,
        sheetPeekHeight = 350.dp,
        sheetContent = {
            Scaffold(
                contentColor = Color.Black,
                containerColor = Color.Transparent,
                modifier = Modifier
                    .padding(bottom = 140.dp)
                    .navigationBarsPadding()

            ){
                innerPaddingModifier ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPaddingModifier)
                        .background(Color.Transparent)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    items(20) {
//                        index ->
//                            ChatItemList()
//                    }
//                    items(3) {
//                        index ->
//                        ChatItemList(titleChat = "$index")
//                    }
                }
            }
        },
        scaffoldState = scaffoldState,
        containerColor = ChatIFBackground,
        contentColor = Color.Black,
        sheetContainerColor=Color.White
    ) {
        innerPadding ->
        LazyRow(
            modifier = Modifier
                .padding(top=20.dp)
                .padding(innerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
//            items(5) {
//                index ->
//                ChatItemElected()
//            }
        }
    }
}

@Preview
@Composable
fun MessengerBodyContextPreview(){
    MessengerBodyContext()
}