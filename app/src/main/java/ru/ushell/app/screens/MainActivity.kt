package ru.ushell.app.screens

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.ushell.app.screens.auth.view.AuthViewModel
import ru.ushell.app.screens.navigation.ScreenNav
import ru.ushell.app.ui.theme.UshellAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        //       setContent {
        //            UshellAppTheme {
        //                MainApp()
        //            }
        //        }

        setContent {
//            User.getInstance(LocalContext.current)

            val navController = rememberNavController()
//            val isLoggedIn = remember { mutableStateOf(SaveUser.isLogin(applicationContext)) }
            val isLoggedIn = false

            UshellAppTheme {
                SplashScreen(
                    onTimeout = {
                        setContent {
                            val navController = rememberNavController()

                            AuthorizeScreen(navController)
                            MainNavScreen(
                                isLoggedIn = isLoggedIn,
                                navController = navController
                            )
                        }
                    }
                )
            }
        }

    }


//    override fun onStart() {
//        super.onStart()
//        User.getInstance(this)
//        _root_ide_package_.ru.ushell.app.old.api.Service(this).updateData()
//        _root_ide_package_.ru.ushell.app.old.api.Service(this).updateToken()
//        _root_ide_package_.ru.ushell.app.old.api.websocket.chat.ChatConnect().connect()
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        val existingChannel = notificationManager.getNotificationChannel("CHANNEL_ID")
//
//        if (existingChannel == null) {
//            PushNotifications.createNotificationChannel(this)
//        }


//        TODO:"подумать какие функции необходимо вызывать при первом заходе пользователя "
//        TODO: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/screens/chatScreen/message/MessageItem.kt:172:5 '@Deprecated(...) @Composable() @ComposableTarget(...) fun ClickableText(text: AnnotatedString, modifier: Modifier = ..., style: TextStyle = ..., softWrap: Boolean = ..., overflow: TextOverflow = ..., maxLines: Int = ..., onTextLayout: (TextLayoutResult) -> Unit = ..., onClick: (Int) -> Unit): Unit' is deprecated. Use Text or BasicText and pass an AnnotatedString that contains a LinkAnnotation.
// w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/screens/drawer/models/device/qrscanner/CameraScreen.kt:61:26 '@property:Deprecated(...) val LocalLifecycleOwner: ProvidableCompositionLocal<LifecycleOwner>' is deprecated. Moved to lifecycle-runtime-compose library in androidx.lifecycle.compose package.
//w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/theme/Theme.kt:56:20 'var statusBarColor: Int' is deprecated. Deprecated in Java.
//w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/theme/Theme.kt:57:20 'var navigationBarColor: Int' is deprecated. Deprecated in Java.
//w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/theme/Theme.kt:92:20 'var navigationBarColor: Int' is deprecated. Deprecated in Java.
//w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/theme/Theme.kt:125:20 'var navigationBarColor: Int' is deprecated. Deprecated in Java.
//w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/theme/Theme.kt:126:20 'var statusBarColor: Int' is deprecated. Deprecated in Java.
//w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/theme/Theme.kt:159:20 'var navigationBarColor: Int' is deprecated. Deprecated in Java.
//w: file:///D:/Ushell/UshellApp/app/src/main/java/ru/ushell/app/ui/theme/Theme.kt:160:20 'var statusBarColor: Int' is deprecated. Deprecated in Java.
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        _root_ide_package_.ru.ushell.app.old.api.websocket.chat.ChatConnect().disconnect()
//        TODO("какие функции надо вызвать при выходе их приложения")
//    }
//}

@Composable
fun MainApp() {
    var isSplashShowing by remember { mutableStateOf(true) }
    val navController = rememberNavController()
    if (isSplashShowing) {
        Log.d("DEBUG","isSplashShowing")
        SplashScreen(
            onTimeout = {
                isSplashShowing = false
            }
        )
    } else {
        Log.d("DEBUG"," NO isSplashShowing")

        NavHost(
            navController = navController,
            startDestination = Routes.ScreenAuth.route
        ) {
            composable(Routes.ScreenAuth.route) {
                StartScreen()
            }
            composable(Routes.ScreenNav.route) {
                ScreenNav()
            }
        }
    }
}

@Composable
private fun MainNavScreen(
    isLoggedIn: Boolean,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.ScreenNav.route else Routes.ScreenAuth.route) {
        composable(Routes.ScreenAuth.route) {
            StartScreen()
        }
        composable(Routes.ScreenNav.route) {
            ScreenNav()
        }
    }
}


sealed class Routes(
    val route: String,
) {
    data object ScreenAuth : Routes("auth_screen")
    data object ScreenNav : Routes("nav_screen")
}
