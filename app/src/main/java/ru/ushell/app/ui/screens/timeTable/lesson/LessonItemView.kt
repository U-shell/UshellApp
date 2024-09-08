package ru.ushell.app.ui.screens.timeTable.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.api.Service
import ru.ushell.app.models.ModelTimeTable.lesson.Lesson
import ru.ushell.app.models.User
import ru.ushell.app.models.e_class.ERoleClass.AccessControl
import ru.ushell.app.models.e_class.ERoleClass.containsValueHead
import ru.ushell.app.models.e_class.ERoleClass.containsValueTeacher
import ru.ushell.app.ui.screens.timeTable.attendance.AttendanceDialog
import ru.ushell.app.ui.theme.ColorItemGray
import ru.ushell.app.ui.theme.ColorItemGreen
import ru.ushell.app.ui.theme.ColorItemRad
import ru.ushell.app.ui.theme.Darkteam
import ru.ushell.app.ui.theme.TimeTableTextLessonItem
import ru.ushell.app.ui.theme.TimeTableTextLessonItemBold
import ru.ushell.app.ui.theme.UshellBackground
import ru.ushell.app.ui.utils.calendar.CalendarUtils
import java.time.LocalDate

@Composable
fun LessonItem(
    lesson: Lesson,
    attendance: Int,
    date: LocalDate
){
    val showAttendance = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                showAttendance.value = AccessControl()
            },
        shape= RoundedCornerShape(0.dp),
        colors= CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ){
        LessonItemContext(
            lesson = lesson,
            attendance = attendance
        )
    }

    if (showAttendance.value) {
        var idGroup: Int = -1

        if(containsValueHead()){
            idGroup = User.getIDGroup()
        }else if (containsValueTeacher()){
            idGroup = lesson.idGroup
        }

        val status = remember {
            mutableStateOf(false)
        }

        Service(LocalContext.current).getAttendanceGroup(
            date,
            idGroup
        ) {
            status.value = true
        }

        if(status.value){
            Darkteam {
                AttendanceDialog(
                    lesson = lesson,
                    date = date,
                    idGroup = idGroup,
                    status = showAttendance,
                    onDismiss = {
                        showAttendance.value = false
                    },
                )
            }
        }
    }
}

@Composable
fun LessonItemContext(
    lesson: Lesson,
    attendance: Int
){
    val status = mapOf(0 to ColorItemRad, 1 to ColorItemGreen, 2 to ColorItemGray)

    ConstraintLayout(
        modifier = Modifier
            .padding(
                top = 5.dp,
                bottom = 5.dp
            )
    ){
        val (favicon) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(favicon) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .zIndex(1f)
                .size(12.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .background(
                    color = status[attendance]!!,
                    shape = CircleShape
                ),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 5.dp,
                    end = 5.dp,
                    top = 2.dp,
                    bottom = 2.dp,
                )
                .background(
                    color = UshellBackground,
                    shape = RoundedCornerShape(10.dp)
                )
        ){
            Surface(
                modifier = Modifier
                    .padding(
                        start = 25.dp,
                        end = 25.dp,
                        bottom = 10.dp
                    ),
                color = Color.Transparent,
                contentColor = Color.White,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    Box(modifier = Modifier
                        .paddingText()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = lesson.timeLesson,
                                style = TimeTableTextLessonItem
                            )
                            Text(
                                text = lesson.classroom.toString(),
                                style = TimeTableTextLessonItem
                            )
                        }
                    }
                    Box(modifier = Modifier
                        .paddingText()
                    ) {
                        Text(
                            text = lesson.subject,
                            style = TimeTableTextLessonItemBold,
                        )
                    }
                    Box(modifier = Modifier
                        .paddingText()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = lesson.whitWhom,
                                style = TimeTableTextLessonItem
                            )
                            Text(
                                text = lesson.timeLesson,
                                style = TimeTableTextLessonItem
                            )
                        }
                    }
                    if(lesson.subgroup != 0) {
                        Box(
                            modifier = Modifier
                                .paddingText()
                        ) {
                            Text(
                                text = "Подгруппа ${lesson.subgroup}",
                                style = TimeTableTextLessonItem
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Modifier.paddingText() = this then
    Modifier
        .padding(
            top = 10.dp
        )

@Preview
@Composable
fun LessonItemPreview(){
    val lesson = Lesson(
        null,
        "dayOfWeek",
        1,
        "timeLesson",
        "typeLesson",
        "Subject",
        1,
        "Teacher",
        8888,
        8888,

    )
    LessonItem(
        lesson=lesson,
        attendance = 2,
        date = CalendarUtils.DataNow()
    )
}
