package ru.ushell.app.screens.schedule.attendance

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.ushell.app.R
import ru.ushell.app.data.features.attendance.mappers.AttendanceGroup
import ru.ushell.app.data.features.attendance.mappers.AttendanceStudentGroup
import ru.ushell.app.data.features.timetable.mappers.lesson.Lesson
import ru.ushell.app.screens.schedule.calendar.CalendarUtils.DataNow
import ru.ushell.app.screens.schedule.calendar.CalendarUtils.formattedDateDayAttendance
import ru.ushell.app.screens.schedule.view.attendance.AttendanceUiState
import ru.ushell.app.screens.schedule.view.attendance.AttendanceViewModel
import ru.ushell.app.ui.theme.AttendanceDialogBrief
import ru.ushell.app.ui.theme.AttendanceDialogDes
import ru.ushell.app.ui.theme.AttendanceDialogTitle
import ru.ushell.app.ui.theme.UshellBackground
import java.time.LocalDate

@Composable
fun AttendanceDialog(
    lesson: Lesson,
    date: LocalDate,
    status: MutableState<Boolean>,
    onDismiss: () -> Unit,
){
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color.Transparent,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            AttendanceDialogContext(
                lesson=lesson,
                date=date,
                status=status
            )
        }
    }
}

@Composable
fun AttendanceDialogContext(
    lesson: Lesson,
    date: LocalDate,
    status: MutableState<Boolean>,
){

    Box{
        Column {
            TopPanel(
                lesson=lesson,
                status=status
            )
            BodyDialog(
                numLesson = lesson.numLesson,
                subgroup = lesson.subgroup,
                date = date
            )
        }
    }
}

@Composable
fun TopPanel(
    lesson: Lesson,
    status: MutableState<Boolean>,
){
    val shape = 10.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(shape))
            .background(
                color = UshellBackground,
                shape = RoundedCornerShape(shape)
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 25.dp,
                    bottom = 20.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                verticalArrangement = Arrangement.Center
            ){
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ){

                            Text(
                                text = lesson.subject,
                                style = AttendanceDialogTitle,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        IconButton(
                            onClick = { status.value = false },
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .size(20.dp),
                        ){
                            Icon(
                                painterResource(id = R.drawable.dialog_clouse),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                            )
                        }
                    }
                }
                Box(Modifier.padding(top=5.dp)){
                    Text(
                        text = lesson.time,
                        style = AttendanceDialogDes
                    )
                }
                Box(Modifier.padding(top=5.dp)){
                    Text(
                        text = lesson.type,
                        style = AttendanceDialogDes
                    )
                }
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun BodyDialog(
    modifier: Modifier = Modifier,
    numLesson: Int,
    subgroup: Int,
    date: LocalDate,
    viewModel: AttendanceViewModel = hiltViewModel()
){
    val formattedDate = formattedDateDayAttendance(date)

    var attendanceList by remember { mutableStateOf<List<AttendanceGroup>>(emptyList()) }

    Log.d("attendanceGroupList","r")

    LaunchedEffect(date, numLesson) {
        viewModel.loadAttendanceGroup(formattedDate, numLesson, subgroup)
    }

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is AttendanceUiState.Empty,
        is AttendanceUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AttendanceUiState.Success -> {
            attendanceList = (uiState as AttendanceUiState.Success).attendance
            BodyDialogContext(
                attendanceList = attendanceList,
                formattedDate = formattedDate,
                numLesson = numLesson
            )
        }

        is AttendanceUiState.Error -> {
            TODO()
        }
    }

    Log.d("attendanceGroupList",attendanceList.size.toString())

}

@Composable
fun BodyDialogContext(
    modifier: Modifier = Modifier,
    attendanceList: List<AttendanceGroup>,
    formattedDate: String,
    numLesson: Int,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {

        Column {
            TitleDescriptor()

            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(attendanceList) { attendanceGroup ->
                    ItemList(
                        attendanceGroup = attendanceGroup,
                        dataUser = AttendanceStudentGroup(
                            id = attendanceGroup.id,
                            date = formattedDate,
                            numLesson = numLesson
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun TitleDescriptor() {
    Box(modifier = Modifier
        .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    end = 5.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box{
                Text(
                    text = stringResource(id = R.string.FLMname),
                    style = AttendanceDialogBrief
                )
            }

            Box{
                Text(
                    text = stringResource(id = R.string.Attend),
                    style = AttendanceDialogBrief
                )
            }
        }
    }
}

@Preview
@Composable
fun AttendanceDialogPreview(){

    val lesson = Lesson(
        1,
        "dayOfWeek",
        1,
        "subject",
        "typeLesson",
        "teacher",
        "09:08",
        "923",
        1,
    )

    val status = remember {
        mutableStateOf(false)
    }
    AttendanceDialog(
        lesson=lesson,
        date = DataNow(),
        onDismiss = {true},
        status = status
    )
}
