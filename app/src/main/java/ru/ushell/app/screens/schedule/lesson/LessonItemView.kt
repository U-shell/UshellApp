package ru.ushell.app.screens.schedule.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.data.features.timetable.mappers.lesson.Lesson
import ru.ushell.app.screens.schedule.attendance.AttendanceDialog
import ru.ushell.app.screens.schedule.calendar.CalendarUtils.DataNow
import ru.ushell.app.ui.theme.ColorItemGray
import ru.ushell.app.ui.theme.ColorItemGreen
import ru.ushell.app.ui.theme.ColorItemRad
import ru.ushell.app.ui.theme.TimeTableTextLessonItem
import ru.ushell.app.ui.theme.TimeTableTextLessonItemBold
import ru.ushell.app.ui.theme.UshellBackground
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
//                showAttendance.value = AccessControl()
            }
        ,
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        LessonItemContext(
            lesson = lesson,
            attendance = attendance
        )
    }

    if (showAttendance.value) {
        var idGroup: Int = -1

//        if(containsValueHead()){
//            idGroup = getIDGroup()
//        }else if (containsValueTeacher()){
//            idGroup = lesson.idGroup
//        }

        val status = remember {
            mutableStateOf(false)
        }

        if(status.value){
            AttendanceDialog(
                lesson = lesson,
                date = date,
                status = showAttendance,
                onDismiss = {
                    showAttendance.value = false
                },
            )

        }
    }
}

@Composable
fun LessonItemContext(
    lesson: Lesson,
    attendance: Int,
    modifier: Modifier = Modifier
) {
    val statusColor = when (attendance) {
        0 -> ColorItemRad
        1 -> ColorItemGreen
        2 -> ColorItemGray
        else -> Color.Gray
    }
    ConstraintLayout(
        modifier = modifier
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
                    color = statusColor,
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
                shape = RoundedCornerShape(10.dp),
                color = UshellBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 12.dp,
                            bottom = 10.dp
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(lesson.type, style = TimeTableTextLessonItem)
                        Text(lesson.classroom, style = TimeTableTextLessonItem)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(lesson.subject, style = TimeTableTextLessonItemBold)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(lesson.withWhom, style = TimeTableTextLessonItem)
                        Text(lesson.time, style = TimeTableTextLessonItem)
                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    if (lesson.subgroup != 0) {
                        Spacer(modifier = Modifier.height(4.dp))
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

@Preview
@Composable
fun LessonItemPreview(){
    val lesson = Lesson(
        1,
        "dayOfWeek",
        1,
        "subject",
        "typeLesson",
        "teacher",
        "09:08",
        "2123",
        1,
        )
    LessonItem(
        lesson=lesson,
        attendance = 0,
        date = DataNow()
    )
}
