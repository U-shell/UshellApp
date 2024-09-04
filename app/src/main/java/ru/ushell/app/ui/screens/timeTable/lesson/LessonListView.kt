package ru.ushell.app.ui.screens.timeTable.lesson

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.ushell.app.models.ModelTimeTable.attendance.AttendanceStudent
import ru.ushell.app.models.ModelTimeTable.lesson.Lesson
import ru.ushell.app.models.e_class.ERoleClass.containsValueGroup
import ru.ushell.app.ui.utils.calendar.CalendarUtils
import java.time.LocalDate
import kotlin.Int

@Composable
fun ListLesson(
    modifier :Modifier = Modifier,
    date: LocalDate
) {

    val lesson: ArrayList<Lesson> = Lesson.LessonsForDataWeek(date)

    var attendance: ArrayList<AttendanceStudent> = ArrayList()

    if (containsValueGroup()){
        attendance  =
            AttendanceStudent.AttendanceForData(
                CalendarUtils.formattedDateDayAttendance(date)
            )
    }

    if(lesson.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(lesson.size) { index ->
                Box(modifier=modifier) {
                    LessonItem(
                        lesson = lesson[index],
                        date = date,
                        attendance = attendanceStatus(
                            attendanceList = attendance,
                            numLesson = lesson[index].numLesson,
                            indexes = index
                        )
                    )
                }
            }
        }
    }else {
        Box(modifier = modifier) {
            MessageView()
        }
    }
}

/**
 * 0 = false
 * 1 = true
 * 2 = null
 */
fun attendanceStatus(
    attendanceList: ArrayList<AttendanceStudent>,
    numLesson: Int,
    indexes: Int,
): Int {
    var status = 2

    /*TODO*/
    // пересмотреть второе условие
    if(attendanceList.size == 0 || attendanceList.size < indexes ){
        return status
    }

    for(attendance in attendanceList){
        if(attendance.numLesson.equals(numLesson) && attendance.status.equals(false)){
            status = 0
        }
        else if (attendance.numLesson.equals(numLesson) && attendance.status.equals(true)){
            status = 1
        }
    }

    return status
}
