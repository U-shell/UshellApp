package ru.ushell.app.screens.timetable.calendar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ushell.app.ui.theme.DayCellItemStyle
import ru.ushell.app.screens.timetable.calendar.week.CalendarDate
import ru.ushell.app.screens.timetable.calendar.week.CalendarDateSource
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun NameDayCell(){
    val daysOfWeek = DayOfWeek.entries.toTypedArray()

    LazyVerticalGrid(
        GridCells.Fixed(CalendarUtils.DaysInAWeek),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(CalendarUtils.DaysInAWeek) { index ->
            NameDayCellItem(
                text = daysOfWeek[index].getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                )
            )
        }
    }
}

@Composable
fun NameDayCellItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = Color.White,
        )
    }
}

@Composable
fun DayCellItem(
    data: CalendarDate.Date,
    modifier: Modifier = Modifier,
){
    Row(
        modifier = Modifier
            .padding(
                start = 5.dp,
                end = 5.dp
            )
            .size(34.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .dayCellItem(data.isSelected),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = data.date.dayOfMonth.toString(),
                style = DayCellItemStyle,
                color = if (data.isLesson) Color.White else Color.Gray,
            )
        }
    }
}

@Composable
fun Modifier.dayCellItem(isSelected: Boolean) = this then
        Modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                width = (0.9).dp,
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) Color.White else Color.Transparent
            )

@Preview
@Composable
fun NameDayCellPreview(){
    NameDayCell()
}

@Preview
@Composable
fun DayPreview(){
    val data by remember { mutableStateOf(CalendarDateSource().getDataWeek(selectedDate = LocalDate.now())) }

    DayCellItem(
        data = data.selectedDate,
    )
}