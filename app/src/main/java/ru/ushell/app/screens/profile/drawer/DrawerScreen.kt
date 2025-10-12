package ru.ushell.app.screens.profile.drawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.navigation.DrawerRoutes
import ru.ushell.app.screens.profile.view.ProfileUiState
import ru.ushell.app.screens.profile.view.ProfileViewModel
import ru.ushell.app.ui.theme.DrawerBorderColor
import ru.ushell.app.ui.theme.DrawerExitButtonBackgroundColor
import ru.ushell.app.ui.theme.DrawerExitButtonText
import ru.ushell.app.ui.theme.DrawerInfoUserBriefText
import ru.ushell.app.ui.theme.DrawerInfoUserText
import ru.ushell.app.ui.theme.UshellAppTheme

@Composable
fun DrawerScreen(
    drawerState: DrawerState,
    navController: NavHostController,
    gesturesEnabled: MutableState<Boolean>,
    viewModel: ProfileViewModel = hiltViewModel(),
    content: @Composable () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    var name by rememberSaveable { mutableStateOf("") }
    var brief by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getNameUser()
    }

    if (uiState is ProfileUiState.Success) {
        name = (uiState as ProfileUiState.Success).name
        brief = (uiState as ProfileUiState.Success).brief
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        gesturesEnabled = gesturesEnabled.value,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .padding(
                        end = 30.dp
                    )
                    .navigationBarsPadding()
            ) {
                DrawerContent(
                    name = name,
                    brief = brief,
                    drawerState = drawerState,
                    navController = navController
                )
            }
        },
        content = content
    )
}

@Composable
fun DrawerContent(
    name: String,
    brief: String,
    drawerState: DrawerState,
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        DrawerHeader(name = name, brief = brief, drawerState = drawerState)

        DrawerBody(navController = navController)

        Spacer(modifier = Modifier.weight(1f)) // толкатель вниз

        ButtonExit(
            navController = navController,
            drawerState = drawerState
        )
    }
}


@Composable
fun DrawerHeader(
    name: String,
    brief: String,
    drawerState: DrawerState,
    onThemeClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        HeaderNavigation(
            drawerState = drawerState,
            onThemeClick = onThemeClick
        )
        InfoUser(
            name = name,
            brief = brief,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun HeaderNavigation(
    drawerState: DrawerState,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = { scope.launch { drawerState.close() } },
            ) {
                Icon(
                    painter = painterResource(R.drawable.drawer_close),
                    contentDescription = null,
                    tint = LocalContentColor.current
                )
            }
            Text(
                text = stringResource(R.string.menu),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(
            onClick = onThemeClick,
        ) {
            Icon(
                painter = painterResource(R.drawable.drawer_theme),
                contentDescription = null,
                tint = LocalContentColor.current
            )
        }
    }
}

@Composable
fun InfoUser(
    name: String,
    brief: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Аватар
        Image(
            painter = painterResource(R.drawable.bottom_ic_profile),
            contentDescription = "User avatar",
            modifier = Modifier
                .size(70.dp)
                .border(
                    BorderStroke(2.dp, DrawerBorderColor),
                    CircleShape
                )
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = DrawerInfoUserText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = brief,
                style = DrawerInfoUserBriefText,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DrawerBody(
    navController: NavHostController
){
    Column{
        val screens = listOf(
            Drawer.Device,
        )
        screens.forEach { screen ->
            ButtonDesign(
                screen = screen,
                navController = navController
            )
        }
    }
}

@Composable
fun ButtonDesign(
    screen: Drawer,
    navController: NavHostController
){
    Box(
        modifier = Modifier
            .padding(
                end = 6.dp,
                top = 5.dp
            )
            .shadow(
                elevation = 5.dp,
                spotColor = Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
            .drawWithContent {
                val padding = 10.dp.toPx()
                clipRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height + padding
                ) {
                    this@drawWithContent.drawContent()
                }
            }
            .background(Color.White)
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(
                    start = 5.dp,
                    end = 16.dp,
                    top = 21.dp,
                    bottom = 21.dp
                )
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = Modifier
                    .padding(
                        start = 25.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ){
                Box{
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id=screen.icon!!),
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = screen.title!!,
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(
                            start = 15.dp
                        )
                )
            }
            Box(
                Modifier
                    .padding(
                        end = 6.dp
                    )
            ){
                Icon(
                    painter = painterResource(id = R.drawable.drawer_arrow),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun ButtonExit(
    navController: NavHostController,
    drawerState: DrawerState,
) {
    val scope = rememberCoroutineScope()
    val showExit = remember { mutableStateOf(false) }
    
    Button(
        onClick = {

            scope.launch { drawerState.close() }
            navController.navigate(DrawerRoutes.StartScreen.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }

            showExit.value = true
        },
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = DrawerExitButtonBackgroundColor
        ),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.drawer_exit),
            style = DrawerExitButtonText
        )
    }
    if (showExit.value) {
        Exit()
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerContentPreview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Open)
    UshellAppTheme {
        DrawerContent(
            name = "Мешков Роман Константинович",
            brief = "ШМС-311",
            drawerState = drawerState,
            navController = navController
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerHeaderPreview() {
    DrawerHeader(
        name = "Мешков Роман Константинович",
        brief = "ШМС-311",
        drawerState = rememberDrawerState(DrawerValue.Open)
    )
}
@Preview(showBackground = true)
@Composable
fun DrawerBodyPreview(){
    DrawerBody(rememberNavController())
}


@Preview(showBackground = true)
@Composable
fun ProfUserPreview() {
    InfoUser(
        name = "Мешков Роман Константинович",
        brief = "ШМС-311"
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonDesignPreview() {
    ButtonDesign(
        Drawer.Device,
        rememberNavController()
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonExitPreview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ButtonExit(navController = navController, drawerState = drawerState)
}
