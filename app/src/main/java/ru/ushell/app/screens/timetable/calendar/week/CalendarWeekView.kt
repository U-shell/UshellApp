package ru.ushell.app.screens.timetable.calendar.week

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ushell.app.R
import ru.ushell.app.screens.timetable.calendar.CalendarUtils
import ru.ushell.app.screens.timetable.calendar.DayCellItem
import ru.ushell.app.screens.timetable.calendar.NameDayCell
import java.time.LocalDate

@Composable
fun CalendarWeek(
    data: CalendarDate,
    onNavigatePrevious: () -> Unit,
    onNavigateNext: () -> Unit,
    onDateClickListener: (CalendarDate.Date) -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = onNavigatePrevious,
            modifier = Modifier
                .size(35.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.timetable_arrow_left),
                contentDescription = null,
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
        ){
            CalendarDataWeek(
                data = data,
                onDateClickListener=onDateClickListener
            )
        }
        IconButton(
            onClick = onNavigateNext,
            modifier = Modifier
                .size(35.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.timetable_arrow_right),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun CalendarDataWeek(
    data: CalendarDate,
    onDateClickListener: (CalendarDate.Date) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarWeekContent(
            data = data,
            onDateClickListener = onDateClickListener
        )
    }
}

@Composable
fun CalendarWeekContent(
    data: CalendarDate,
    onDateClickListener: (CalendarDate.Date) -> Unit
){
    NameDayCell()
    CalendarView(
        data = data,
        onDateClickListener
    )
}

@Composable
fun CalendarView(
    data: CalendarDate,
    onDateClickListener: (CalendarDate.Date) -> Unit
) {

    LazyVerticalGrid(
        GridCells.Fixed(CalendarUtils.DaysInAWeek),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(data.visibleDates.size) { index ->
            Box(
                modifier = Modifier
                    .size(34.dp),
                contentAlignment = Alignment.Center
            ) {
                DayCellItem(
                    data = data.visibleDates[index],
                    modifier = Modifier
                        .clickable {
                            onDateClickListener(data.visibleDates[index])
                        },
                )
            }
        }
    }
}

@Preview
@Composable
fun CalendarWeekPreview(){
    val dataSource = CalendarDateSource()
    var data by remember { mutableStateOf(dataSource.getDataWeek(selectedDate = LocalDate.now())) }

    CalendarWeek(
        data = data,
        onNavigateNext = { },
        onNavigatePrevious = {}
    ){
        date -> data = data.copy(
            selectedDate = date,
            visibleDates = data.visibleDates.map{
                it.copy(
                    isSelected = it.date.isEqual(date.date)
                )
            }
        )
    }
}

@Preview
@Composable
fun CalendarDataWeekViewPreview(){
    val dataSource = CalendarDateSource()
    var data by remember { mutableStateOf(dataSource.getDataWeek(selectedDate = LocalDate.now())) }

    CalendarDataWeek(
        data = data
    ){
        date -> data = data.copy(
            selectedDate = date,
            visibleDates = data.visibleDates.map{
                it.copy(
                    isSelected = it.date.isEqual(date.date)
                )
            }
        )
    }
}
