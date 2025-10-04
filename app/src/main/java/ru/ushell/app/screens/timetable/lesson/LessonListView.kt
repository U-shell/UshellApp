package ru.ushell.app.screens.timetable.lesson

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.ushell.app.screens.utils.calendar.CalendarUtils.formattedDateDayAttendance

import java.time.LocalDate
import kotlin.Int

@Composable
fun ListLesson(
    modifier: Modifier = Modifier,
    date: LocalDate
) {

//    val lesson: ArrayList<Lesson> = LessonsForDataWeek(date)

//    var attendance: ArrayList<AttendanceStudent> = ArrayList()

//    if (ERoleClass.containsValueGroup()){
//        attendance  =
//            AttendanceForData(
//               formattedDateDayAttendance(date)
//            )
//    }

//    if(lesson.isNotEmpty()) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//            ,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            items(lesson.size) { index ->
//                Box(modifier=modifier) {
//                    LessonItem(
//                        lesson = lesson[index],
//                        date = date,
//                        attendance = attendanceStatus(
//                            attendanceList = attendance,
//                            numLesson = lesson[index].numLesson,
//                            indexes = index
//                        )
//                    )
//                }
//            }
//        }
//    }else {
//        Box(modifier = modifier) {
//            MessageView()
//        }
//    }
}

/**
 * 0 = false
 * 1 = true
 * 2 = null
 */
//fun attendanceStatus(
//    attendanceList: ArrayList<AttendanceStudent>,
//    numLesson: Int,
//    indexes: Int,
//): Int {
//    var status = 2
//
//    /*TODO*/
//    // пересмотреть второе условие
//    if(attendanceList.size == 0 || attendanceList.size < indexes ){
//        return status
//    }
//
//    for(attendance in attendanceList){
//        if(attendance.numLesson.equals(numLesson) && attendance.status.equals(false)){
//            status = 0
//        }
//        else if (attendance.numLesson.equals(numLesson) && attendance.status.equals(true)){
//            status = 1
//        }
//    }
//
//    return status
//}
