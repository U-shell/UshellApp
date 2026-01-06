package ru.ushell.app.screens.schedule.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.ushell.app.data.features.attendance.remote.attendance.Status
import ru.ushell.app.data.features.attendance.mappers.AttendanceGroup
import ru.ushell.app.data.features.attendance.mappers.AttendanceStudentGroup
import ru.ushell.app.screens.schedule.calendar.CalendarUtils
import ru.ushell.app.screens.schedule.calendar.CalendarUtils.DataNow
import ru.ushell.app.screens.schedule.calendar.CalendarUtils.formattedDateDayAttendance
import ru.ushell.app.screens.schedule.view.attendance.AttendanceViewModel
import ru.ushell.app.ui.theme.AttendanceStudentGroupText
import ru.ushell.app.ui.theme.UshellBackground


@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    attendanceGroup: AttendanceGroup,
    dataUser: AttendanceStudentGroup,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 5.dp,
                    end = 15.dp,
                    bottom = 5.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextUser(attendanceGroup.nameStudent)
            AttendanceButton(
                student = dataUser,
                state = statusUser(
                    attendance = attendanceGroup.attendance
                )
            )
        }
    }
}

@Composable
fun TextUser(
    text: String,
){
    Box(
        modifier = Modifier
            .padding(start = 15.dp)
    ){
        Text(
            text=text,
            style = AttendanceStudentGroupText
        )
    }

}
@Composable
fun AttendanceButton(
    state: ButtonState,
    student: AttendanceStudentGroup,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    var buttonState by remember { mutableStateOf(state) }
    var status by remember { mutableStateOf(Status.NOT_EXISTS) }

    LaunchedEffect(status) {
        student.attendance = status
        viewModel.putAttendanceGroup(student)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                buttonState = when (buttonState) {
                    ButtonState.UNKNOWN -> ButtonState.EXISTS
                    ButtonState.EXISTS -> ButtonState.NOT_EXISTS
                    ButtonState.NOT_EXISTS -> ButtonState.UNKNOWN
                }


                status = when (buttonState) {
                    ButtonState.UNKNOWN -> Status.UNKNOWN
                    ButtonState.EXISTS -> Status.EXISTS
                    ButtonState.NOT_EXISTS -> Status.NOT_EXISTS
                }

            }
            .border(
                width = 2.dp,
                color = when (buttonState) {
                    ButtonState.UNKNOWN -> Color(0xFFE6EDF1)
                    ButtonState.EXISTS -> UshellBackground
                    ButtonState.NOT_EXISTS -> UshellBackground
                },
                shape = RoundedCornerShape(5.dp)
            )
            .background(
                when (buttonState) {
                    ButtonState.UNKNOWN -> Color(0xFFE6EDF1)
                    ButtonState.EXISTS -> UshellBackground
                    ButtonState.NOT_EXISTS -> Color.White
                }
            ),
        contentAlignment = Alignment.Center,
    ){
        Box(
            modifier = Modifier
                .padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 5.dp,
                    bottom = 5.dp
                ),
            ){
            Text(
                text = when (buttonState) {
                    ButtonState.UNKNOWN -> "N"
                    ButtonState.EXISTS -> "П"
                    ButtonState.NOT_EXISTS -> "H"
                },
                fontSize = 15.sp,
                color = when (buttonState) {
                    ButtonState.UNKNOWN -> Color(0xFFE6EDF1)
                    ButtonState.EXISTS -> Color.White
                    ButtonState.NOT_EXISTS -> UshellBackground
                }
            )
        }
    }
}

enum class ButtonState {
    EXISTS,
    NOT_EXISTS,
    UNKNOWN
}

fun statusUser(
    attendance: Status,
):ButtonState{

    when (attendance) {
        Status.EXISTS -> ButtonState.EXISTS
        Status.NOT_EXISTS -> ButtonState.NOT_EXISTS
        Status.UNKNOWN -> ButtonState.UNKNOWN
    }

    return ButtonState.UNKNOWN
}

@Preview
@Composable
fun ItemListPreview(){
    val attendance =
        AttendanceGroup(
            "namestydent",
            "Мешков Роман",
            1,
            attendance = Status.EXISTS
        )
    ItemList(
        modifier = Modifier.background(Color.White),
        attendanceGroup = attendance,
        dataUser = AttendanceStudentGroup(
            id = "1",
            date = formattedDateDayAttendance(DataNow()),
            numLesson = 1,
            attendance = Status.EXISTS
        )
    )
}

@Preview
@Composable
fun AttendanceButtonPreview(){

    AttendanceButton(
        state = ButtonState.EXISTS,
        student = AttendanceStudentGroup(
            id = "1",
            date = CalendarUtils.formattedDateDayAttendance(CalendarUtils.DataNow()),
            numLesson = 1,
            attendance = Status.EXISTS

        ))
}
