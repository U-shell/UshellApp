package ru.ushell.app.screens.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.R
import ru.ushell.app.screens.LogsState
import ru.ushell.app.screens.rememberLogsUser
import ru.ushell.app.ui.theme.BottomBackgroundAlfa
import ru.ushell.app.ui.theme.ListColorButtonBig

@Composable
fun Further(){
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
            val barrier = createBottomBarrier(lesson)
            Box(
                modifier = Modifier
                    .constrainAs(backgroundImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .backgroundImagesSmall()
            )
            Box(
                modifier = Modifier
                    .constrainAs(topPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Скоро тут все будет",
                        fontSize = 40.sp,
                        color = Color.Black)
                }
            }
            Box(
                modifier = Modifier
                    .constrainAs(lesson) {
                        top.linkTo(backgroundImage.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(barrier, margin = 10.dp)
                    }

            )
        }
    }
}

@Composable
fun Modifier.backgroundImage() = this then
        Modifier
            .shadow(1.dp)
            .blur(30.dp)
            .paint(
                painterResource(id = R.drawable.background_app),
                contentScale = ContentScale.FillBounds,
            )
            .background(color = Color.Black.copy(alpha = 0.6f))
            .fillMaxSize()

@Composable
fun Modifier.backgroundImagesSmall(
    radiusShape: Dp = 30.dp,
    height: Dp = 290.dp
) = this then
        Modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape = RoundedCornerShape(bottomStart = radiusShape, bottomEnd = radiusShape))
            .background(
                color = Color.Black.copy(alpha = 0.85f),
                shape = RoundedCornerShape(bottomStart = radiusShape, bottomEnd = radiusShape)
            )
            .blur(
                radius = 10.dp,
                edgeTreatment = BlurredEdgeTreatment(
                    RoundedCornerShape(
                        bottomStart = radiusShape,
                        bottomEnd = radiusShape
                    )
                )
            )
            .paint(
                painterResource(id = R.drawable.background_app),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopStart,
            )
            .background(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(bottomStart = radiusShape, bottomEnd = radiusShape)
            )

@Composable
fun TopPanelScreen(
    titleContext: @Composable BoxScope.() -> Unit,
    context: @Composable ColumnScope.() -> Unit
){
    Column(
        modifier = Modifier
            .padding(
                top = 10.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        top = 20.dp,
                    )
            ) {
                titleContext()
            }
        }
        context()
    }
}

@Composable
fun SearchPanel(
    logsState: LogsState = rememberLogsUser(),
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .border(
            BorderStroke(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = ListColorButtonBig,
                    startX = -300.1f
                )
            ),
            shape = RoundedCornerShape(5.dp)
        )
        .background(
            color = BottomBackgroundAlfa,
            shape = RoundedCornerShape(5.dp)
        )
        .clip(RoundedCornerShape(5.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
            ){
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            ){
                TextField(
                    value = logsState.email.value,
                    onValueChange = {logsState.email.value = it},
                    placeholder = {
                        Text(
                            text = stringResource(R.string.Search)
                        )
                    },
                    singleLine = true,
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                )
            }
        }
    }
}


@Preview
@Composable
fun SearchPanelPreview(){
    SearchPanel(
    )
}
