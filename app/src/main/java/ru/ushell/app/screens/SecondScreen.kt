package ru.ushell.app.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.screens.auth.view.AuthUiState
import ru.ushell.app.screens.auth.view.AuthViewModel
import ru.ushell.app.ui.theme.AuthHelpTextButton
import ru.ushell.app.ui.theme.AuthScreenBodyTitle
import ru.ushell.app.ui.theme.ListColorButton


@Composable
fun AuthorizeScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showForgotPassword by rememberSaveable { mutableStateOf(false) }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    // Реакция на успешный вход
    if (uiState is AuthUiState.Success) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.Main.route) {
                popUpTo(Routes.Start.route) { inclusive = true }
            }
        }
    }

    // Показ ошибки
    if (uiState is AuthUiState.Error) {
        LaunchedEffect(uiState) {
            Toast.makeText(context, (uiState as AuthUiState.Error).message, Toast.LENGTH_SHORT).show()
        }
        viewModel.clearError()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        StyleScreenBackground {
            Box(modifier = Modifier.fillMaxWidth()) {
                SignInWindow(
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it }
                )
            }
            Box(modifier = Modifier.padding(bottom = 91.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ButtonAuth(
                        text = R.string.auth_enter,
                        onClick = {
                            viewModel.login(email, password)
                        },
                        enabled = uiState !is AuthUiState.Loading // блокируем кнопку при загрузке
                    )
                    TextButton(onClick = { showForgotPassword = true }) {
                        Text("Забыл пароль?", style = AuthHelpTextButton)
                    }
                }
            }
        }
        if (uiState is AuthUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }

    if (showForgotPassword) {
        ForgotPassword { showForgotPassword = false }
    }
}

@Composable
fun AuthorizeScreenContext(){

}

@Composable
fun SignInWindow(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.auth_set_data), style = AuthScreenBodyTitle)

        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.auth_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = textFieldColors(),
            singleLine = true
        )

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.auth_password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) R.drawable.auth_eye_close else R.drawable.auth_eye
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            colors = textFieldColors(),
            singleLine = true
        )
    }
}

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.Gray,
    unfocusedIndicatorColor = Color.Gray,
    focusedLabelColor = Color.DarkGray,
    cursorColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White.copy(alpha = 0.8f),
    focusedPlaceholderColor = Color.Transparent
)

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
            brush = Brush.Companion.horizontalGradient(
                ListColorButton
            )),
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

@Preview
@Composable
fun AuthorizeScreenPreview(){
    val navController = rememberNavController()

    AuthorizeScreenContext()
}

@Preview
@Composable
fun PreviewForgot(){
    ForgotPassword()
}
