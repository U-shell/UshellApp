package ru.ushell.app.screens.schedule

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import ru.ushell.app.R
import ru.ushell.app.ui.theme.BottomBackgroundAlfa
import ru.ushell.app.ui.theme.ListColorButton
import ru.ushell.app.screens.schedule.calendar.CalendarUtils.formattedMonthYear
import ru.ushell.app.screens.schedule.calendar.month.CalendarMonthDialog
import ru.ushell.app.screens.schedule.calendar.week.CalendarDate
import ru.ushell.app.screens.schedule.calendar.week.CalendarDateSource
import ru.ushell.app.screens.schedule.calendar.week.CalendarWeek
import ru.ushell.app.screens.schedule.lesson.ListLesson
import ru.ushell.app.screens.utils.TopPanelScreen
import ru.ushell.app.screens.utils.backgroundImagesSmall

@Composable
fun ScheduleScreen(
) {
    val dataSource = CalendarDateSource()
    var currantData by remember { mutableStateOf(dataSource.getDataWeek(selectedDate=dataSource.today)) }

    ScheduleContent(
        currentData = currantData,
        onDateChange = { newDate ->
            currantData = dataSource.getDataWeek(selectedDate = newDate.date)
        },
        onWeekNavigate = { weeksToAdd ->
            currantData = dataSource.getDataWeek(
                selectedDate = currantData.selectedDate.weeks(weeksToAdd.toLong())
            )
        }
    )
}

@Composable
fun ScheduleContent(
    currentData: CalendarDate,
    onDateChange: (CalendarDate.Date) -> Unit,
    onWeekNavigate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(color = Color(0xFFE7E7E7)),
    ) {
        val (topPanel, lesson, backgroundImage) = createRefs()
        val barrier = createBottomBarrier(lesson)

        Box(
            modifier = Modifier
                .constrainAs(backgroundImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .backgroundImagesSmall(height = 250.dp)
        )
        Box(
            modifier = Modifier
                .constrainAs(topPanel) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {

            TopPanelCalendar(
                data = currentData,
                onMonthClick = { /* опционально */ },
                onNavigatePrevious = { onWeekNavigate(-1) },
                onNavigateNext = { onWeekNavigate(1) },
                onDateClick = onDateChange
            )
        }
        Box(
            modifier = Modifier
                .zIndex(-1f)
                .constrainAs(lesson) {
                    top.linkTo(backgroundImage.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(barrier)
                }
                .padding(bottom = 300.dp)
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
                ListLesson(
                    date = currentData.selectedDate.date,
                )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TopPanelCalendar(
    data: CalendarDate,
    onMonthClick: () -> Unit,
    onNavigatePrevious: () -> Unit,
    onNavigateNext: () -> Unit,
    onDateClick: (CalendarDate.Date) -> Unit,
) {
    var showCalendar by remember { mutableStateOf(false) }

    TopPanelScreen(
        titleContext = {
            TimetableTitle(
                monthYear = formattedMonthYear(data.selectedDate.date),
                onMonthClick = { showCalendar = true }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            CalendarWeek(
                data = data,
                onNavigatePrevious = onNavigatePrevious,
                onNavigateNext = onNavigateNext,
                onDateClickListener = onDateClick
            )
        }

        if (showCalendar) {
            CalendarMonthDialog(
                today = data.selectedDate.date,
                data = data,
                onDismiss = { showCalendar = false },
                onDataMonth = onMonthClick,
                showCalendar = mutableStateOf(showCalendar), // или передайте обратно
                onDateClickListener = onDateClick
            )
        }
    }
}

@Composable
fun TimetableTitle(
    monthYear: String,
    onMonthClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Расписание",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            CalendarButton(
                text = monthYear,
                onClick = onMonthClick
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.timetable_mini_logo),
            contentDescription = "App logo",
            tint = Color.White
        )
    }
}

@Composable
fun CalendarButton(
    text: String,
    onClick:() -> Unit = {},
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = BottomBackgroundAlfa
        ),
        contentPadding = PaddingValues(
            start = 4.dp,
            top = 4.dp,
            end = 4.dp,
            bottom = 4.dp
        ),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = (0.5).dp,
            brush = Brush.linearGradient(
                colors = ListColorButton,
                start = Offset(0f,Float.POSITIVE_INFINITY),
                end = Offset(Float.POSITIVE_INFINITY,0f)
            )
        ),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                painterResource(id = R.drawable.timetable_calendar),
                contentDescription = null,
                tint = Color.White
                )
            Text(
                text = text,
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(
                        start = 5.dp
                    )
            )
        }
    }
}

@Preview
@Composable
fun TopPanelCalendarPreview(){
    val dateSource = remember { CalendarDateSource() }
    val today = remember { dateSource.today }

    var currentData by remember { mutableStateOf(dateSource.getDataWeek(selectedDate = today)) }

    ScheduleContent(
        currentData = currentData,
        onDateChange = { newDate ->
            currentData = dateSource.getDataWeek(selectedDate = newDate.date)
        },
        onWeekNavigate = { weeksToAdd ->
            currentData = dateSource.getDataWeek(
                selectedDate = currentData.selectedDate.weeks(weeksToAdd.toLong())
            )
        }
    )
}
