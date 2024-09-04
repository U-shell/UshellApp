package ru.ushell.app.ui.screens.timeTable.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.ushell.app.R
import ru.ushell.app.models.ModelTimeTable.attendance.AttendanceGroup
import ru.ushell.app.models.ModelTimeTable.lesson.Lesson
import ru.ushell.app.ui.theme.AttendanceDialogBrief
import ru.ushell.app.ui.theme.AttendanceDialogDes
import ru.ushell.app.ui.theme.AttendanceDialogTitle
import ru.ushell.app.ui.theme.UshellBackground
import ru.ushell.app.ui.utils.calendar.CalendarUtils
import java.time.LocalDate

@Composable
fun AttendanceDialog(
    lesson: Lesson,
    idGroup: Int,
    date: LocalDate,
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
                idGroup=idGroup,
                date=date,
            )
        }
    }
}

@Composable
fun AttendanceDialogContext(
    lesson: Lesson,
    idGroup: Int,
    date: LocalDate,
){

    Box{
        Column {
            TopPanel(
                lesson=lesson,
            )
            BodyDialog(
                numLesson = lesson.numLesson,
                idGroup = idGroup,
                subgroup = lesson.subgroup,
                date = date
            )
        }
    }
}

@Composable
fun TopPanel(
    lesson: Lesson,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    top = 25.dp,
                    bottom = 20.dp
                )
        ) {
            Box {
                Text(
                    text = lesson.subject,
                    style = AttendanceDialogTitle
                )
            }
            Box(Modifier.padding(top=5.dp)){
                Text(
                    text = lesson.timeLesson,
                    style = AttendanceDialogDes
                )
            }
            Box(Modifier.padding(top=5.dp)){
                Text(
                    text = lesson.typeLesson,
                    style = AttendanceDialogDes
                )
            }
        }
    }
}

@Composable
fun BodyDialog(
    numLesson: Int,
    idGroup: Int,
    subgroup: Int,
    date: LocalDate,
){
    val attendanceGroupList: ArrayList<AttendanceGroup> =
        AttendanceGroup.AttendanceGroupForData(
            numLesson,
            subgroup
        )

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
    ){

        Column {
            TitleDescriptor()

            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(attendanceGroupList.size){ index ->

                    ItemList(
                        numLesson=numLesson,
                        attendanceGroup = attendanceGroupList[index],
                        dataUser= DataPut(
                            idStudent = attendanceGroupList[index].idStudent.toString(),
                            idGroup = idGroup,
                            data = CalendarUtils.formattedDateDayAttendance(date),
                            numLesson = numLesson.toString()
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
        null,
        "dayOfWeek",
        1,
        "timeLesson",
        "typeLesson",
        "Subject",
        1,
        "Teacher",
        1,
        999,
        )
    AttendanceDialog(
        lesson=lesson,
        idGroup=1,
        onDismiss = {true},
        date = CalendarUtils.DataNow()
    )
}
