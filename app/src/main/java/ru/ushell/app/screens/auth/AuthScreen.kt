package ru.ushell.app.screens.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.ushell.app.R
import ru.ushell.app.navigation.Destination
import ru.ushell.app.screens.auth.view.AuthUiState
import ru.ushell.app.screens.auth.view.AuthViewModel
import ru.ushell.app.screens.utils.backgroundImage
import ru.ushell.app.ui.theme.AuthScreenBodyTitle
import ru.ushell.app.ui.theme.BottomBackgroundAlfa
import ru.ushell.app.ui.theme.ListColorButton
import ru.ushell.app.ui.theme.StartScreenButtonText
import ru.ushell.app.ui.theme.StartScreenTitleText

@Composable
fun AuthorizeScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

    when(uiState){
        is AuthUiState.Empty -> {}

        is AuthUiState.Success -> {
            if((uiState as AuthUiState.Success).status) {
                LaunchedEffect(Unit) {
                    navController.navigate(Destination.Inside.route)
                }
            }
        }

        is AuthUiState.Loading -> {}

        is AuthUiState.Error -> {
            LaunchedEffect(uiState) {
                Toast.makeText(
                    context,
                    (uiState as AuthUiState.Error).message,
                    Toast.LENGTH_SHORT
                )
                .show()
                viewModel.clearError()
            }
        }
    }

    AuthorizeContent(
        emailState = emailState,
        passwordState = passwordState,
        uiState = uiState,
        onLoginClick = {
            viewModel.login(emailState.text.toString(), passwordState.text.toString(), context)
        }
    )
}

@Composable
fun AuthorizeContent(
    emailState: TextFieldState,
    passwordState: TextFieldState,
    uiState: AuthUiState,
    onLoginClick: () -> Unit,
){

    StyleScreenBackground{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SignInWindow(
                emailState = emailState,
                passwordState = passwordState,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            ButtonAuth(
                text = R.string.auth_enter,
                onClick = onLoginClick,
                enabled = uiState !is AuthUiState.Loading && emailState.text.isNotBlank() && passwordState.text.isNotBlank()
            )
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInWindow(
    emailState: TextFieldState,
    passwordState: TextFieldState,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable { mutableStateOf(true) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.auth_set_data), style = AuthScreenBodyTitle)

        TextField(
            state = emailState,
            label = { Text(stringResource(R.string.auth_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = textFieldColors(),
        )

        SecureTextField(
            state = passwordState,
            label = { Text(stringResource(R.string.auth_password)) },
            textObfuscationMode =
                if (passwordVisible) TextObfuscationMode.RevealLastTyped
                else TextObfuscationMode.Visible,
            trailingIcon = {
                val description = if (passwordVisible) "Show password" else "Hide password"
                TooltipBox(
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                    tooltip = { PlainTooltip { Text(description) } },
                    state = rememberTooltipState(),
                ) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.auth_eye_close else R.drawable.auth_eye
                            ),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            },
            colors = textFieldColors(),

        )
    }
}

@Composable
fun StyleScreenBackground(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.backgroundImage())

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
    }

    content()

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
    enabled: Boolean = true
){
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BottomBackgroundAlfa
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                ListColorButton
            )
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

@Preview
@Composable
fun AuthorizePreview() {
    AuthorizeContent(
        emailState = rememberTextFieldState("user@example.com"),
        passwordState = rememberTextFieldState("123456"),
        uiState = AuthUiState.Empty,
        onLoginClick = {}
    )
}

@Preview
@Composable
fun SignInWindowPreview(){
    SignInWindow(
        emailState = rememberTextFieldState("user@example.com"),
        passwordState = rememberTextFieldState("123456"),
    )
}