package ru.ushell.app.ui.screens.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.models.User
import ru.ushell.app.ui.navigation.DrawerRoutes
import ru.ushell.app.ui.theme.DrawerExitButtonBackgroundColor
import ru.ushell.app.ui.theme.DrawerExitButtonText
import ru.ushell.app.ui.theme.UshellAppTheme

@Composable
fun DrawerScreen(
    drawerState: DrawerState,
    navController: NavHostController,
    content: @Composable () -> Unit,
    gesturesEnabled: MutableState<Boolean>,
){
    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        gesturesEnabled = gesturesEnabled.value,
        content = content,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(320.dp)
                    .navigationBarsPadding(),
            ) {
                DrawerContext(
                    drawerState = drawerState,
                    navController = navController,
                )
            }
        },
    )
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DrawerContext(
    navController: NavHostController,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Open),
) {
    Scaffold(
        bottomBar = {
            ButtonExit(
                navController=navController,
                drawerState=drawerState
            )
        },
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            DrawerHeader(drawerState)
            DrawerBody(navController)
        }
    }
}

@Composable
fun ButtonExit(
    drawerState: DrawerState,
    navController: NavHostController,
) {
    val showExit = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box {
        Button(
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate(DrawerRoutes.StartScreen.route){
                    popUpTo(DrawerRoutes.DialogScreen.route)
                    launchSingleTop = true
                }
                showExit.value = true
            },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DrawerExitButtonBackgroundColor
            ),
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.drawer_exit),
                style = DrawerExitButtonText
            )
        }
    }
    if (showExit.value) {
        Exit()
//        ElevateWindowDialog(
//            scope=scope,
//            showExit=showExit,
//            drawerState=drawerState,
//            navController=navController,
//            onDismiss = {
//                showExit.value = false
//            },
//        )
    }
}

@Preview
@Composable
fun TopNavPreview(){
    val navController = rememberNavController()

    ButtonExit(
        drawerState=rememberDrawerState(DrawerValue.Open),
        navController=navController
    )
}

@Preview
@Composable
fun CraneDrawerPreview() {
    User.getInstance(LocalContext.current)
    val showExit = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    UshellAppTheme {
        DrawerContext(
            navController=navController,
        )
    }
}
