package ru.ushell.app.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val ContextBackground = Color(0xFFE7E7E7)

val DiagramBaseColor = Color(0xFFA6A1AA)
val DiagramBackgroundColor = Color(0xFFF1F1F1)

val DrawerBorderColor = Color(0xFFC3BABA)
val DrawerExitButtonBackgroundColor = Color(0xFFB7A6BE).copy(alpha = 0.45f)
val DrawerInfoDescriptionColor = Color(0xFF827272)
val DrawerExitButtonTextColor = Color(0xFF6A6767)

val ChatNotingBackground = Color(0xFFA56EDC)
val ChatIFBackground = Color(0xFF461576)
val UshellBackground = Color(0xFF3B085F)
//LessonItemBackground
//ChatBackground
//BottomNavBackground
//ItemChatListBackground

//NavigationBarColor
val SplashScreenBackground = Color(0xFF31094D)
val BottomBackground = Color(0xFF2F0342)
val BottomBackgroundAlfa = Color(0xFF2F0342).copy(alpha = 0.45f)

val LightBackgroundColor = Color.Black.copy(alpha = 0.8f)

val ColorItemRad = Color(0xFFB44242)
val ColorItemGray = Color(0xFF7E7979)
val ColorItemGreen = Color(0xFF3B9760)
val ColorItemYellow = Color(0xFFCEC830)

val ListColorButton = listOf(
    Color.White,
    Color.Transparent,
    Color.White,
)
val ListColorButtonBig = listOf(
    Color.White,
    Color.Transparent,
    Color.White,
)

val ListColorRing = listOf(
    Color(0xFFA56EDC),
    Color(0xFF4F46A4),
)

val ushellColor = lightColorScheme(
    primary = SplashScreenBackground,
    secondary = UshellBackground
)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
