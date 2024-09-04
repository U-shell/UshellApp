package ru.ushell.app.ui.screens.startScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.ui.navigation.ScreenNav
import ru.ushell.app.ui.screens.backgroundImage
import ru.ushell.app.ui.theme.BottomBackgroundAlfa
import ru.ushell.app.ui.theme.ListColorButton
import ru.ushell.app.ui.theme.NoNavigationBarColorTheme
import ru.ushell.app.ui.theme.StartScreenButtonText
import ru.ushell.app.ui.theme.StartScreenTitleText
import ru.ushell.app.ui.theme.UshellAppTheme

@Composable
fun StartScreen(){
    val navController = rememberNavController()
    AuthNav(navController)
}

@Composable
fun AuthNav(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RoutesStart.ScreenStart.route
    ) {
        composable(RoutesStart.ScreenStart.route) {
            NoNavigationBarColorTheme {
                StartContext(navController)
            }
        }
        composable(RoutesStart.ScreenAuth.route) {
            NoNavigationBarColorTheme {
                AuthorizeScreen(navController)
            }
        }
        composable(RoutesStart.ScreenNav.route){
            UshellAppTheme {
                ScreenNav()
            }
        }
    }
}

@Composable
fun StartContext(
    navController: NavHostController
){
    StyleScreenBackground{
        Box(modifier = Modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.start_activity_image),
                contentDescription = null,
                alignment = Alignment.Center,
            )
        }
        Box(
            modifier = Modifier
                .padding(
                    bottom = 91.dp
                )
        ) {
            ButtonAuth(
                text = R.string.start_auth,
                onClick = {
                    navController.navigate(RoutesStart.ScreenAuth.route)
                }
            )
        }
    }
}

@Composable
fun StyleScreenBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.backgroundImage())
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
            ) {
                WelcomeText(text = R.string.start_welcome_text)
            }
            content()
        }
    }
}

@Composable
fun WelcomeText(text: Int){
    Box(
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 55.dp,
            )
            .height(115.dp)
    ) {
        Text(
            text = stringResource(text),
            style = StartScreenTitleText,
        )
    }
}

@Composable
fun ButtonAuth(
    text: Int,
    onClick: () -> Unit = {},
){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BottomBackgroundAlfa
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(ListColorButton)
        ),
    ){
        Row(
            modifier = Modifier
                .padding(
                    start = 45.dp,
                    end = 45.dp,
                    top = 7.dp,
                    bottom = 7.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(text),
                style = StartScreenButtonText
            )
        }
    }
}

sealed class RoutesStart(
    val route: String,
) {
    data object ScreenStart : RoutesStart("start_start")
    data object ScreenAuth : RoutesStart("start_auth")
    data object ScreenNav : RoutesStart("start_nav")
}

@Preview
@Composable
fun StartScreenPreview(){
    StartScreen()
}

@Preview
@Composable
fun WelcomeTextPreview(){
    WelcomeText(R.string.start_welcome)
}

@Preview
@Composable
fun ButtonAuthPreview(){
    ButtonAuth(R.string.start_auth_button)
}
