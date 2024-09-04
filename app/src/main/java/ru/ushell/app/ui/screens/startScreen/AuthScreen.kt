package ru.ushell.app.ui.screens.startScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.api.request.Response.loginUser
import ru.ushell.app.ui.theme.AuthHelpTextButton
import ru.ushell.app.ui.theme.AuthScreenBodyTitle
import ru.ushell.app.ui.theme.ListColorButton

@Composable
fun AuthorizeScreen(
    navController: NavHostController
){
    AuthorizeContext(navController=navController)
}

@Composable
fun AuthorizeContext(
    navController: NavHostController,
    logsState: LogsState = rememberLogsUser(),
){
    var showForgotPassword by remember { mutableStateOf(false) }
    var statusAuth by remember { mutableStateOf(false) }

    StyleScreenBackground{
        Box(
            modifier = Modifier
        ) {
            SignInWindow(logsState=logsState)
        }
        Box(
            modifier = Modifier
                .padding(bottom = 91.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    /*TODO*/
                    ButtonAuth(
                        text = R.string.auth_enter,
                        onClick = {
                            statusAuth=true
                        }
                    )
                    TransitionToNextActivity(statusAuth, logsState, navController)
                    statusAuth=false
                }
                TextButton(
                    onClick = {
                        showForgotPassword = true
                    }
                ) {
                    Text(
                        text = "Забыл пароль?",
                        style = AuthHelpTextButton
                    )
                }
            }
        }
    }
    if(showForgotPassword) {
        ForgotPassword(onClose = { showForgotPassword = false })
    }
}

@Composable
fun SignInWindow(
    logsState: LogsState
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.auth_set_data),
            style = AuthScreenBodyTitle
        )
        TextField(
            value = logsState.email.value,
            onValueChange = {logsState.email.value = it},
            placeholder = {
                Text(
                    text = stringResource(R.string.auth_email)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.auth_email)
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedPlaceholderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.DarkGray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.8f)
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
        )
        TextField(
            value = logsState.password.value,
            onValueChange = { logsState.password.value = it },
            placeholder = {
                Text(text = stringResource(R.string.auth_password))
            },
            label = {
                Text(text = stringResource(R.string.auth_password))
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedPlaceholderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.DarkGray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.8f)
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (passwordVisible) {
                        painterResource(id = R.drawable.auth_eye_close)
                    }else {
                        painterResource(id = R.drawable.auth_eye)
                    }

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, description)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(
    onClose: () -> Unit = {},
) {
    val shape = 30.dp
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false))
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isVisible) {
                            scaffoldState.bottomSheetState.hide()
                            onClose()
                        }
                    }
                })
            }
            .navigationBarsPadding(),
        sheetPeekHeight = 250.dp, // Set peek height to 0 to show the whole sheet initially
        sheetSwipeEnabled = true,
        sheetShape = RoundedCornerShape(topStart = shape, topEnd = shape),
        sheetContainerColor = Color.Black.copy(alpha = 0.8f),
        sheetContent = {
            BottomSheetContent(onClose)
        },
    ){
    }
}

@Composable
fun BottomSheetContent(
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Печальа че скачть",
            color = Color.White,
            fontSize = 23.sp,
            modifier = Modifier.padding( bottom = 55.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            /*TODO*/
            ButtonHelp("Поддержка")
            ButtonHelp("Назад",onClose)
        }
    }
}

@Composable
fun ButtonHelp(
    text : String,
    onClick: () -> Unit = {}){

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(ListColorButton)),
        modifier = Modifier
            .clip(RoundedCornerShape(10))
            .background(color = Color(0xFF2F0342).copy(alpha = 0.45f))
    ){
        Text(
            text = text,
            fontSize = 23.sp
        )
    }
}

@Composable
fun TransitionToNextActivity(
    statusAuth: Boolean,
    logsState: LogsState,
    navController: NavHostController
) {
    if (statusAuth) {
        loginUser(
            logsState.email.value.text,
            logsState.password.value.text,
            LocalContext.current,
            {
                navController.navigate(RoutesStart.ScreenNav.route) // Navigate to the next screen
            },
            {
                // что делать если произошла ошибка
            }
        )
    }
}

@Stable
class LogsState(
    var email: MutableState<TextFieldValue>,
    val password: MutableState<TextFieldValue>
)

@Composable
fun rememberLogsUser(
    email: MutableState<TextFieldValue> =  remember { mutableStateOf(TextFieldValue("")) },
    password: MutableState<TextFieldValue> =  remember {mutableStateOf(TextFieldValue(""))}
): LogsState = remember {
    LogsState(email, password)
}

@Preview
@Composable
fun PreviewAuth(){
    val navController = rememberNavController()
    AuthorizeScreen(navController)
}

@Preview
@Composable
fun PreviewForgot(){
    ForgotPassword()
}
