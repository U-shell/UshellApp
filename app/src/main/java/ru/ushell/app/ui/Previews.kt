package ru.ushell.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.ushell.app.ui.screens.startScreen.StartScreen
import ru.ushell.app.ui.theme.UshellAppTheme

/*TODO*/
// Подумать какие форматы и какие параметры надо отслеживать перед релизом

@Preview(widthDp = 340, name = "340 width - Me")
@Composable
fun ProfilePreview340() {
    UshellAppTheme {
        StartScreen()
    }
}

@Preview(widthDp = 480, name = "480 width - Me")
@Composable
fun ProfilePreview480Me() {
    UshellAppTheme {
        StartScreen()
    }
}

@Preview(widthDp = 480, name = "480 width - Other")
@Composable
fun ProfilePreview480Other() {
    UshellAppTheme {
        StartScreen()
    }
}
@Preview(widthDp = 340, name = "340 width - Me - Dark")
@Composable
fun ProfilePreview340MeDark() {
    UshellAppTheme() {
        StartScreen()
    }
}

@Preview(widthDp = 480, name = "480 width - Me - Dark")
@Composable
fun ProfilePreview480MeDark() {
    UshellAppTheme() {
        StartScreen()
    }
}

@Preview(widthDp = 480, name = "480 width - Other - Dark")
@Composable
fun ProfilePreview480OtherDark() {
    UshellAppTheme(){
        StartScreen()
    }
}
