package ru.ushell.app.screens.profile.drawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.ui.theme.DrawerBorderColor
@Composable
fun DrawerHeader(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Open)
) {
    Column {
        TopNav(drawerState = drawerState)
        ProfUser()
    }
}

@Composable
fun TopNav(
    drawerState: DrawerState
){
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 35.dp,
                end = 15.dp,
                top = 10.dp
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                modifier = Modifier
                    .size(15.dp),
            ){
                Icon(
                    painterResource(id = R.drawable.drawer_close),
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                )
            }
            Text(
                modifier = Modifier
                    .padding(
                        start=50.dp
                    ),
                text = stringResource(R.string.menu),
                fontSize = 20.sp
            )
        }
        Row {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(30.dp)
            ){
                Icon(
                    painterResource(id = R.drawable.drawer_theme),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun ProfUser(){
    Row(
        modifier = Modifier
            .padding(
                start=8.dp,
                top=20.dp,
                bottom = 10.dp
            )
        ,
    ) {
        Row {
            val border = 2.dp
            Image(
                painterResource(id = R.drawable.bottom_ic_profile),
                contentDescription = null,

                modifier = Modifier
                    .size(70.dp)
                    .border(
                        BorderStroke(
                            border,
                            DrawerBorderColor
                        ),
                        CircleShape
                    )
                    .padding(border)
                    .clip(CircleShape)
                    .background(Color.Black)
            )
        }
        Box(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 5.dp
                )
            ,
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
//                Text(
//                    text =
//                        if (!(_root_ide_package_.ru.ushell.app.old.models.eClass.User.getNameBrief()).equals(" ")){
//                            _root_ide_package_.ru.ushell.app.old.models.eClass.User.getNameBrief()
//                        }else{
//                            stringResource(R.string.profile_user_name)
//                        },
//                    style = _root_ide_package_.ru.ushell.app.old.ui.theme.DrawerInfoUserText,
//                    modifier = Modifier
//                        .height(30.dp),
//                )
//                Text(
//                    text =
//                        if (!(_root_ide_package_.ru.ushell.app.old.models.eClass.User.getGroupName()).equals(" ")){
//                            _root_ide_package_.ru.ushell.app.old.models.eClass.User.getGroupName()
//                        }else{
//                            stringResource(R.string.profile_user_info)
//                        },
//                    style = _root_ide_package_.ru.ushell.app.old.ui.theme.DrawerInfoUserBriefText
//                )
            }
        }
    }
}

@Preview
@Composable
fun DrawerHeaderPreview(){
    DrawerHeader()
}