package ru.ushell.app.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import ru.ushell.app.R
import ru.ushell.app.screens.theme.SplashScreenBackground

private const val SplashWaitTime: Long = 1000

@Composable
fun SplashScreen(
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier
    	.fillMaxSize()
        .background(SplashScreenBackground)
    	.wrapContentSize(),
    onTimeout: () -> Unit
){
    LaunchedEffect(Unit) {
        delay(SplashWaitTime)
        onTimeout()
    }

    Image(
        painterResource(id = R.drawable.splash_screen_logo),
        contentDescription ="splash_screen_logo",
        modifier
            .size(158.dp)
    )
}

@Preview
@Composable
private fun ActivityPreview() {
    val navController = rememberNavController()
    SplashScreen(onTimeout = {
        navController.navigate(Routes.ScreenNav.route) {
            popUpTo(Routes.ScreenNav.route){
                inclusive = true
            }
        }
    })
}