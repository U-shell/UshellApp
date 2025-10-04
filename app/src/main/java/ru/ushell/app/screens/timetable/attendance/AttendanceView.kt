//package ru.ushell.app.screens.timetable.lesson
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import org.json.JSONObject
//import ru.ushell.app.screens.theme.AttendanceStudentGroupText
//import ru.ushell.app.screens.theme.UshellBackground
//import ru.ushell.app.screens.utils.calendar.CalendarUtils
//import ru.ushell.app.screens.utils.calendar.CalendarUtils.DataNow
//import ru.ushell.app.screens.utils.calendar.CalendarUtils.formattedDateDayAttendance
//
//
//@Composable
//fun ItemList(
//    numLesson:Int,
//    attendanceGroup: AttendanceGroup,
//    dataUser:DataPut,
//) {
//    Box{
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(
//                    start = 5.dp,
//                    end = 15.dp,
//                    bottom = 5.dp
//                ),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            TextUser(attendanceGroup.nameStudent)
//            AttendanceButton(
//                dataUser=dataUser,
//                state=statusUser(
//                    numLesson=numLesson,
//                    attendance=attendanceGroup.attendance
//                )
//            )
//        }
//    }
//}
//
//@Composable
//fun TextUser(
//    text: String,
//){
//    Box(
//        modifier = Modifier
//            .padding(start = 15.dp)
//    ){
//        Text(
//            text=text,
//            style = AttendanceStudentGroupText
//        )
//    }
//
//}
//@Composable
//fun AttendanceButton(
//    state: ButtonState,
//    dataUser: DataPut,
//) {
//    var buttonState by remember { mutableStateOf(state) }
//
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(5.dp))
//            .clickable {
//                buttonState = when (buttonState) {
//                    ButtonState.UNKNOWN -> ButtonState.ABSENT
//                    ButtonState.ABSENT -> ButtonState.PRESENT
//                    ButtonState.PRESENT -> ButtonState.UNKNOWN
//                }
//
//                putAttendance(
//                    dataUser = dataUser,
//                    status = when (buttonState) {
//                        ButtonState.UNKNOWN -> "null"
//                        ButtonState.ABSENT -> true
//                        ButtonState.PRESENT -> false
//                    }
//                )
//            }
//            .border(
//                width = 2.dp,
//                color = when (buttonState) {
//                    ButtonState.UNKNOWN -> Color(0xFFE6EDF1)
//                    ButtonState.ABSENT -> UshellBackground
//                    ButtonState.PRESENT -> UshellBackground
//                },
//                shape = RoundedCornerShape(5.dp)
//            )
//            .background(
//                when (buttonState) {
//                    ButtonState.UNKNOWN -> Color(0xFFE6EDF1)
//                    ButtonState.ABSENT -> UshellBackground
//                    ButtonState.PRESENT -> Color.White
//                }
//            ),
//        contentAlignment = Alignment.Center,
//    ){
//        Box(
//            modifier = Modifier
//                .padding(
//                    start = 25.dp,
//                    end = 25.dp,
//                    top = 5.dp,
//                    bottom = 5.dp
//                ),
//            ){
//            Text(
//                text = when (buttonState) {
//                    ButtonState.UNKNOWN -> "N"
//                    ButtonState.ABSENT -> "П"
//                    ButtonState.PRESENT -> "H"
//                },
//                fontSize = 15.sp,
//                color = when (buttonState) {
//                    ButtonState.UNKNOWN -> Color(0xFFE6EDF1)
//                    ButtonState.ABSENT -> Color.White
//                    ButtonState.PRESENT -> UshellBackground
//                }
//            )
//        }
//    }
//}
//
//class DataPut(
//    val idStudent: String,
//    val idGroup: Int,
//    val data: String,
//    val numLesson: String,
//)
//
//fun putAttendance(
//    dataUser: DataPut,
//    status: Any,
//) {
//    updateAttendanceStudents(
//        getAccessToken(),
//        dataUser.idGroup,
//        BodyRequestPutStudentAttendance(
//            dataUser.data,
//            dataUser.numLesson,
//            object : HashMap<String?, Any?>() {
//                init {
//                    put(dataUser.idStudent, status)
//                }
//            }
//        )
//    )
//}
//
//enum class ButtonState {
//    ABSENT,
//    PRESENT,
//    UNKNOWN
//}
//
//fun statusUser(
//    numLesson: Int,
//    attendance: JSONObject,
//):ButtonState{
//    val attendanceStudent: Iterator<String> = attendance.keys()
//
//    while (attendanceStudent.hasNext()){
//        val num = Integer.parseInt(attendanceStudent.next())
//        if (num == numLesson) {
//            return if(attendance.get(num.toString())==true){
//                ButtonState.ABSENT
//            }else if(attendance.get(num.toString())==false){
//                ButtonState.PRESENT
//            }else{
//                return ButtonState.UNKNOWN
//            }
//        }
//    }
//    return ButtonState.UNKNOWN
//}
//
//@Preview
//@Composable
//fun ItemListPreview(){
//    val attendans =
//        AttendanceGroup(
//            "namestydent",
//            1,
//            1,
//            JSONObject()
//        )
//    ItemList(
//        numLesson = 1,
//        attendanceGroup = attendans,
//        dataUser = DataPut(
//            idStudent = "1",
//            idGroup = getIDGroup(),
//            data = formattedDateDayAttendance(DataNow()),
//            numLesson = "1"
//        )
//    )
//}
//
//@Preview
//@Composable
//fun AttendanceButtonPreview(){
//
//    AttendanceButton(
//        state = ButtonState.ABSENT,
//        dataUser = DataPut(
//            idStudent = "1",
//            idGroup = getIDGroup(),
//            data = CalendarUtils.formattedDateDayAttendance(CalendarUtils.DataNow()),
//            numLesson = "1"
//        ))
//}
