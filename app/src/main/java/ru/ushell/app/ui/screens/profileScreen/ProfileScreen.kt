package ru.ushell.app.ui.screens.profileScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch
import lanltn.com.circleprogresscustom.data.DataSources
import ru.ushell.app.R
import ru.ushell.app.models.User
import ru.ushell.app.ui.screens.backgroundImagesSmall
import ru.ushell.app.ui.screens.profileScreen.diagram.ProgressItem
import ru.ushell.app.ui.theme.DrawerBorderColor
import ru.ushell.app.ui.theme.ProfileTextUserInfo

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
) {
    ProfileContent(
        drawerState=drawerState
    )
}

@Composable
fun ProfileContent(
    drawerState:DrawerState
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(0.dp))
            .navigationBarsPadding()
            .background(color = Color(0xFFE7E7E7))
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .navigationBarsPadding()
        ){
            val (topPanel, profile, backgroundImage) = createRefs()

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
                    }
            ){
                TopPanel(drawerState=drawerState)
            }

            Box(
                modifier = Modifier
                    .constrainAs(profile) {
                        top.linkTo(topPanel.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                InfoPanel()
            }
        }
    }
}

@Composable
fun TopPanel(
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(
                top = 10.dp,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 20.dp,
                    top = 30.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    scope.launch { drawerState.open() }
                }
            ){
                Icon(
                    painterResource(id = R.drawable.profile_drawer),
                    contentDescription =null,
                    tint = Color.White
                )
            }
            Icon(
                painterResource(id = R.drawable.timetable_mini_logo),
                contentDescription = null,
                tint = Color.White
            )
        }

        Box(modifier =  Modifier){
            val border = 2.dp
            Image(
                painter = painterResource(id = R.drawable.bottom_ic_profile),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier
                    .size(70.dp)
                    .border(
                        BorderStroke(border, DrawerBorderColor),
                        CircleShape
                    )
                    .padding(border)
                    .clip(CircleShape)
            )
        }

        Box(modifier = Modifier
            .padding(top = 10.dp)
        ){
            Text(
                text =
                    if (!(User.getFullName()).equals(" ")){
                            User.getFullName()
                    }else{
                        stringResource(R.string.profile_user_name)
                    },
                style = ProfileTextUserInfo,
                modifier = Modifier
                    .height(30.dp),
            )
        }

        Box{
            Text(
                text =
                /*TODO*/
                // зменить размерм и заменить название переменной
                    if (!(User.getGroupName()).equals(" ")){
                        User.getGroupName()
                    }else{
                        stringResource(R.string.profile_user_info)
                    },
                style = ProfileTextUserInfo,
                modifier = Modifier
                    .height(30.dp),
            )
        }
    }
}

@Composable
fun InfoPanel() {
    val loc = LocalDensity.current
    var col by remember { mutableStateOf(0.dp) }

    Box(
        modifier = Modifier
            .padding(
                start = 7.dp,
                end = 30.dp,
                bottom = 17.dp
            )
            .fillMaxWidth()
            .offset(y = (-15).dp)
            .height(col)
            .background(
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
            )
    )
    Box(
        modifier = Modifier
            .padding(start = 15.dp, end = 18.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .heightIn(min = 525.dp)
                .onGloballyPositioned { coordinate ->
                    col = with(loc) { coordinate.size.height.toDp() }
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressUI()
            Spacer(modifier = Modifier.height(10.dp))
            InfoPerson()
        }
    }
}

@Composable
fun ProgressUI() {
    val courses = DataSources.allLearnedProgresss

    LazyRow(
        modifier = Modifier
            .padding(
                top = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(courses.size) { index ->
            ProgressItem(
                course = courses[index],
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun InfoPerson(){
    Column(
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp,
                bottom = 20.dp
            )
    ){
        InfoPersonItem()
    }
}

@Composable
fun InfoPersonItem(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = 10.dp,
            end = 10.dp,
            top = 10.dp,
            bottom = 10.dp
        )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_info_group),
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Box(
                        modifier = Modifier
                                .padding(start = 10.dp)
                    ) {
                        Text(
                            text = "Инфо",
                            color = Color.Gray,
                        )
                    }
                }
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_arrow),
                        contentDescription = null
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    User.getInstance(LocalContext.current)
    ProfileScreen()
}

@Preview
@Composable
fun ProfileUIPreview() {
    ProgressUI()
}
