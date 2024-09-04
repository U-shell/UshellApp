package ru.ushell.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.ushell.app.R

private val regular = FontFamily(Font(R.font.montserrat_regular))
private val medium = FontFamily(Font(R.font.montserrat_medium))
private val semibold = FontFamily(Font(R.font.montserrat_semibold))
private val bold = FontFamily(Font(R.font.montserrat_bold))

val ushellTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val StartScreenTitleText = TextStyle(
    color = Color.White,
    fontSize = 40.sp,
    fontFamily = semibold,
    fontWeight = Bold
)
val StartScreenButtonText = TextStyle(
    color = Color.White,
    fontSize = 23.sp,
    fontFamily = regular
)
val AuthScreenBodyTitle = TextStyle(
    color = Color.White,
    fontSize = 16.sp,
    fontFamily = medium
)

val AuthHelpTextButton = TextStyle(
    color = Color.White,
    fontSize = 15.sp,
    fontFamily = regular
)
val DrawerInfoUserText = TextStyle(
    color = Color.Black,
    fontSize = 16.sp,
    fontFamily = medium
)

val DrawerInfoUserBriefText = TextStyle(
    color = DrawerInfoDescriptionColor,
    fontSize = 16.sp,
    fontFamily = medium
)

val DrawerExitButtonText = TextStyle(
    color = DrawerExitButtonTextColor,
    fontSize = 16.sp,
    fontFamily = medium
)

val ProfileTextUserInfo = TextStyle(
    color = Color.White,
    fontSize = 18.sp,
    fontFamily = regular,
    textAlign = TextAlign.Center
)
val DayCellItemStyle = TextStyle(
    fontSize = 20.sp,
    fontFamily = bold
)

val TimeTableText = TextStyle(
    color = Color.White,
    fontSize = 23.sp,
    fontFamily = bold
)
val TimeTableTextMessage = TextStyle(
    color = Color.White,
    fontSize = 20.sp,
    fontFamily = regular
)
val TimeTableTextLessonItem = TextStyle(
    color = Color.White,
    fontSize = 13.sp,
    fontFamily = regular
)
val TimeTableTextLessonItemBold = TextStyle(
    color = Color.White,
    fontSize = 18.sp,
    fontFamily = bold
)

val CalendarMonthText = TextStyle(
    color = Color.White,
    fontSize = 15.sp,
    fontFamily = semibold
)

val AttendanceDialogTitle = TextStyle(
    color = Color.White,
    fontSize = 20.sp,
    fontFamily = semibold
)
val AttendanceDialogDes= TextStyle(
    color = Color.White,
    fontSize = 11.sp,
    fontFamily = regular
)
val AttendanceDialogBrief= TextStyle(
    color = Color.Black,
    fontWeight = Bold,
    fontSize = 16.sp,
    fontFamily = semibold
)

val AttendanceStudentGroupText = TextStyle(
    color = BottomBackground,
    fontSize = 15.sp,
    fontFamily = regular
)

val NameChatTitle = TextStyle(
    fontSize = 16.sp,
    color = Color.Black,
    fontFamily = medium
)

val NameChatDes = TextStyle(
    fontSize = 14.sp,
    color = Color.Black.copy(alpha = 0.46f),
    fontFamily = medium
)
val NameChatElected = TextStyle(
    fontSize = 16.sp,
    color = Color.White.copy(alpha = 0.50f),
    fontFamily = medium
)
