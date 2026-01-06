package ru.ushell.app.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.screens.profile.diagram.ProgressItem
import ru.ushell.app.screens.profile.diagram.allLearnedProgress
import ru.ushell.app.screens.profile.view.ProfileUiState
import ru.ushell.app.screens.profile.view.ProfileViewModel
import ru.ushell.app.screens.utils.backgroundImagesSmall
import ru.ushell.app.ui.theme.ContextBackground
import ru.ushell.app.ui.theme.DrawerBorderColor
import ru.ushell.app.ui.theme.ProfileTextUserInfo


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by rememberSaveable { mutableStateOf("") }
    var brief by rememberSaveable { mutableStateOf("") }
    var presentAttendance by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getNameUser()
    }

    if (uiState is ProfileUiState.Success) {
        name = (uiState as ProfileUiState.Success).name
        brief = (uiState as ProfileUiState.Success).brief
        presentAttendance = (uiState as ProfileUiState.Success).presentAttendance
    }

    ProfileContent(
        name = name,
        brief = brief,
        presentAttendance = presentAttendance
    )
}


@Composable
fun ProfileContent(
    name: String,
    brief: String,
    presentAttendance: Int
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(0.dp))
            .navigationBarsPadding()
            .background(color = ContextBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .navigationBarsPadding()
        ) {

            val (topPanel, profile, backgroundImage) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(backgroundImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .backgroundImagesSmall()
            )

            Box(
                modifier = Modifier
                    .constrainAs(topPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            ) {
                TopPanel(
                    name = name,
                    brief = brief,
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(profile) {
                        top.linkTo(topPanel.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                InfoPanel(presentAttendance=presentAttendance)
            }
        }
    }
}

@Composable
fun TopPanel(
    name: String,
    brief: String,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(
            top = 25.dp,
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 20.dp,
                    top = 30.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(R.drawable.timetable_mini_logo),
                contentDescription = "App logo",
                tint = Color.White
            )
        }

        // Avatar
        Image(
            painter = painterResource(R.drawable.bottom_ic_profile),
            contentDescription = "User avatar",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(70.dp)
                .border(
                    BorderStroke(2.dp, DrawerBorderColor),
                    CircleShape
                )
                .padding(2.dp)
                .clip(CircleShape)
        )

        // Name & group
        Text(
            text = name,
            style = ProfileTextUserInfo,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = brief,
            style = ProfileTextUserInfo,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Composable
fun InfoPanel(presentAttendance: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-15).dp)
            .background(
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp) // внутренний отступ
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Diagram(presentAttendance = presentAttendance)

            InfoPerson()
        }
    }
}

@Composable
fun Diagram(presentAttendance: Int) {
    val courses = allLearnedProgress(presentAttendance)

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(courses.size) { index ->
            ProgressItem(
                progress = courses[index],
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun InfoPerson(){
    Column(
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp,
                bottom = 20.dp
            )
    ){
        InfoPersonItem()
    }
}

@Composable
fun InfoPersonItem(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = 10.dp,
            end = 10.dp,
            top = 10.dp,
            bottom = 10.dp
        )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_info_group),
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Box(
                        modifier = Modifier
                                .padding(start = 10.dp)
                    ) {
                        Text(
                            text = "Инфо",
                            color = Color.Gray,
                        )
                    }
                }
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_arrow),
                        contentDescription = null
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileContent(
        name = "Мешков Роман Константинович",
        brief = "ШМС-311",
        presentAttendance = 30,
    )
}

@Preview
@Composable
fun TopPanelPreview(){
    TopPanel(
        name = "Мешков Роман Константинович",
        brief = "ШМС-311",
    )
}

@Preview
@Composable
fun InfoPanelPreview(){
    InfoPanel(presentAttendance = 20)
}

@Preview
@Composable
fun DiagramPreview() {
    Diagram(presentAttendance = 80)
}

@Preview
@Composable
fun InfoPersonPreview() {
    InfoPerson()
}
