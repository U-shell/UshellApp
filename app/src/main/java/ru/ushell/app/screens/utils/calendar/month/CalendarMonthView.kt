package ru.ushell.app.screens.utils.calendar.month

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout

import ru.ushell.app.R
import ru.ushell.app.screens.theme.CalendarMonthText
import ru.ushell.app.screens.theme.LightBackgroundColor
import ru.ushell.app.screens.utils.calendar.CalendarUtils
import ru.ushell.app.screens.utils.calendar.CalendarUtils.DaysInAWeek
import ru.ushell.app.screens.utils.calendar.DayCellItem
import ru.ushell.app.screens.utils.calendar.NameDayCell
import ru.ushell.app.screens.utils.calendar.week.CalendarData
import ru.ushell.app.screens.utils.calendar.week.CalendarDataSource

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle.FULL_STANDALONE
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarMonthDialog(
    today: LocalDate,
    data: CalendarData,
    showCalendar: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onDataMonth: () -> Unit,
    onDateClickListener: (CalendarData.Date) -> Unit,
){
    Dialog(onDismissRequest = onDismiss) {
        Surface(color = Color.Transparent) {
            CalendarMonthContent(
                data=data,
                today=today,
                showCalendar=showCalendar,
                onDataMonth=onDataMonth,
                onDateClickListener=onDateClickListener
            )
        }
    }
}

@Composable
fun CalendarMonth(
    data: CalendarData,
    showCalendar: MutableState<Boolean>,
    onDataMonth: () -> Unit,
    onDateClickListener: (CalendarData.Date) -> Unit,
) {
    CalendarMonthContent(
        data = data,
        showCalendar=showCalendar,
        onDataMonth=onDataMonth,
        onDateClickListener=onDateClickListener
    )
}

@Composable
fun CalendarMonthContent(
    data: CalendarData,
    today: LocalDate = LocalDate.now(),
    showCalendar: MutableState<Boolean>,
    firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
    calendarState: CalendarState<DynamicSelectionState> = rememberSelectableCalendarState(
        initialMonth = YearMonth.of(today.year, today.month) ,
        initialSelection = today
    ),
    dayContent: @Composable BoxScope.(DayState<DynamicSelectionState>) -> Unit = { DayItem(it) },
    monthContainer: @Composable (content: @Composable (PaddingValues) -> Unit) -> Unit = { content ->
        Box { content(PaddingValues()) }
    },
    onDataMonth: () -> Unit,
    onDateClickListener: (CalendarData.Date) -> Unit
){
    data.selectedDate = CalendarData.Date(
        date=calendarState.selectionState.selection,
        isSelected = false,
        isLesson = false
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = LightBackgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                top = 5.dp,
                bottom = 5.dp
            )
    ) {
        ConstraintLayout(){
            val (topPanel, calendarContext, bottomPanel) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(topPanel){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .zIndex(1f)

            ){
                TopPanelCalendar(monthState = calendarState.monthState)
            }
            Box(
                modifier = Modifier
                    .constrainAs(calendarContext){
                        top.linkTo(topPanel.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                Column {
                    NameDayCell()
                    Calendar(
                        firstDayOfWeek = firstDayOfWeek,
                        today = today,
                        calendarState = calendarState,
                        dayContent = dayContent,
                        monthContainer = monthContainer
                    )
                }
            }
            Box(
                modifier = Modifier
                    .constrainAs(bottomPanel){
                        top.linkTo(calendarContext.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                BottomPanelCalendar(
                    showCalendar = showCalendar,
                    onClick = {
                        onDataMonth()
                        onDateClickListener(data.selectedDate)
                    }
                )
            }
        }
//        TopPanelCalendar(monthState = calendarState.monthState)

//        Spacer(modifier = Modifier.height(8.dp))

//        Column {
//            NameDayCell()
//            Calendar(
//                firstDayOfWeek = firstDayOfWeek,
//                today = today,
//                calendarState = calendarState,
//                dayContent = dayContent,
//                monthContainer = monthContainer
//            )
//        }

//        Spacer(modifier = Modifier.height(8.dp))

//        BottomPanelCalendar(
//            showCalendar = showCalendar,
//            onClick = {
//                onDataMonth()
//                onDateClickListener(data.selectedDate)
//            }
//        )
    }
}

@Composable
fun TopPanelCalendar(
    monthState: MonthState
) {
    var showMonthYearPicker by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(2f)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .padding(start = 10.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            if (showMonthYearPicker) {
                Box() {
                    MonthYearPickerDialog(
                        initialMonth = monthState.currentMonth.month.value - 1,
                        initialYear = monthState.currentMonth.year,
                        onConfirm = { month, year ->
                            monthState.currentMonth = YearMonth.of(year,month)
//                            showMonthYearPicker = false
                        },
                    )
                }

            }
            else {
                Box() {
                    Row() {
                        Text(
                            modifier = Modifier.testTag("MonthLabel"),
                            text = monthState.currentMonth.month
                                .getDisplayName(FULL_STANDALONE, Locale.getDefault())
                                .lowercase()
                                .replaceFirstChar {
                                    it.titlecase()
                                },
                            style = CalendarMonthText
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = monthState.currentMonth.year.toString(),
                            style = CalendarMonthText
                        )
                    }
                }
            }

            IconButton(
                onClick = {showMonthYearPicker = !showMonthYearPicker },
                modifier = Modifier
                    .size(35.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_month_arrow_botton),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Box {
            Row {
                DecrementButton(monthState = monthState)
                IncrementButton(monthState = monthState)
            }
        }
    }
}

@Composable
private fun DecrementButton(
    monthState: MonthState,
) {
    IconButton(
        modifier = Modifier.testTag("Decrement"),
        enabled = monthState.currentMonth > monthState.minMonth,
        onClick = {
            monthState.currentMonth = monthState.currentMonth.minusMonths(1)

        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar_month_arrow_left),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
private fun IncrementButton(
    monthState: MonthState,
) {
    IconButton(
        modifier = Modifier.testTag("Increment"),
        enabled = monthState.currentMonth < monthState.maxMonth,
        onClick = {
            monthState.currentMonth = monthState.currentMonth.plusMonths(1)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar_month_arrow_right),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun BottomPanelCalendar(
    showCalendar: MutableState<Boolean>,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ){
        TextButton(onClick = { showCalendar.value=false }) {
            Text(
                text = "отмена",
                color = Color.White
            )
        }
        TextButton(onClick = {
            onClick()
            showCalendar.value=false
        } ) {
            Text(
                text = "Ок",
                color = Color.White
            )
        }
    }
}

@Composable
fun MonthYearPickerDialog(
    initialMonth: Int,
    initialYear: Int,
    onConfirm: (Int, Int) -> Unit,
) {
    var selectedMonth by remember { mutableIntStateOf(initialMonth) }
    var selectedYear by remember { mutableIntStateOf(initialYear) }

    Box(
        modifier = Modifier
            .height(100.dp)
    ){
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
        ){
            Box() {
                MonthPicker(
                    initialSelectedMonth = selectedMonth,
                    onSelectedMonth = { month ->
                        selectedMonth = month
                    }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box() {
                YearPicker(
                    initialSelectedYear = selectedYear,
                    onSelectedYear = { year ->
                        selectedYear = year
                    }
                )
            }
        }
    }
    onConfirm(selectedMonth,selectedYear)

//    AlertDialog(
//        onDismissRequest = onDismiss,
//        containerColor = Color.Transparent,
//        modifier = Modifier
//            .height(300.dp),
//        text = {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ){
//                MonthPicker(
//                    initialSelectedMonth = selectedMonth,
//                    onSelectedMonth = { month ->
//                        selectedMonth = month
//                    }
//                )
//                YearPicker(
//                    initialSelectedYear = selectedYear,
//                    onSelectedYear = { year ->
//                        selectedYear = year
//                    }
//                )
//            }
//        },
//        confirmButton = {
//            Button(onClick = { onConfirm(selectedMonth + 1, selectedYear) }) {
//                Text("Подтвердить")
//            }
//        }
//    )
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
fun MonthPicker(
    initialSelectedMonth: Int,
    isEndless: Boolean = true,
    onSelectedMonth: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val months = Month.entries
    val monthCount = months.size

    val stateMonth =  rememberLazyListState(
        if (isEndless) initialSelectedMonth else 0
    )

    LazyColumn(
        state = stateMonth,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = stateMonth, snapPosition = SnapPosition.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        items(
            count = if (isEndless) Int.MAX_VALUE else monthCount,
            key = { it % monthCount}
        ){
            val index = (it % monthCount)

            val isSelected = if (isEndless) {
                stateMonth.firstVisibleItemIndex % monthCount == index
            } else {
                stateMonth.firstVisibleItemIndex == index
            }
            Box(
                modifier = Modifier
//                    .fillMaxWidth(0f)

            ){
                Text(
                    text = months[index]
                        .getDisplayName(FULL_STANDALONE, Locale.getDefault())
                        .lowercase()
                        .replaceFirstChar {
                            it.titlecase()
                        }
                    ,
                    color = if (isSelected) Color.White else Color.LightGray.copy(alpha = 0.7f),
                    style = CalendarMonthText,
                )
            }
            LaunchedEffect(isSelected) {
                onSelectedMonth(index)
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation", "FrequentlyChangedStateReadInComposition")
@Composable
fun YearPicker(
    initialSelectedYear: Int,
    isEndless: Boolean = true,
    onSelectedYear: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val selectedYear by remember { mutableStateOf(initialSelectedYear + 1) }
    val yearRange by remember { mutableStateOf(
        Pair(selectedYear - 10, selectedYear + 10)
    )}

    val year = (yearRange.first..yearRange.second).toList()
    val yearCont = year.size

    val stateMonth =  rememberLazyListState(
        if (isEndless) initialSelectedYear  else 0
    )

    LazyColumn(
        state = stateMonth,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = stateMonth, snapPosition = SnapPosition.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(
            count = if (isEndless) Int.MAX_VALUE else yearCont,
            key = { it % yearCont}
        ){
            val index = (it % yearCont)
            val isSelected = if (isEndless) {
                stateMonth.firstVisibleItemIndex % yearCont == index
            } else {
                stateMonth.firstVisibleItemIndex == index
            }

            Text(
                text =  year[index].toString(),
                color = if (isSelected) Color.White else Color.LightGray.copy(alpha = 0.7f),
                style = CalendarMonthText
            )
            LaunchedEffect(isSelected) {
                onSelectedYear(index)
            }
        }
    }
}

@Composable
@SuppressLint("NewApi")
fun <T : SelectionState> Calendar(
    calendarState: CalendarState<T>,
    today: LocalDate = LocalDate.now(),
    showAdjacentMonths: Boolean = false,
    firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
    dayContent: @Composable BoxScope.(DayState<T>) -> Unit = { DayItem(it) },
    monthContainer: @Composable (content: @Composable (PaddingValues) -> Unit) -> Unit = { content ->
        Box { content(PaddingValues()) }
    },
) {
    val daysOfWeek = remember(firstDayOfWeek) {
        DayOfWeek.entries.toTypedArray().rotateRight(CalendarUtils.DaysInAWeek - firstDayOfWeek.ordinal)
    }

    CalendarContent(
        today = today,
        selectionState = calendarState.selectionState,
        currentMonth = calendarState.monthState.currentMonth,
        daysOfWeek = daysOfWeek,
        showAdjacentMonths = showAdjacentMonths,
        dayContent = dayContent,
        monthContainer = monthContainer,
    )
}

@Composable
internal fun <T : SelectionState> CalendarContent(
    today: LocalDate,
    selectionState: T,
    currentMonth: YearMonth,
    daysOfWeek: List<DayOfWeek>,
    showAdjacentMonths: Boolean,
    dayContent: @Composable BoxScope.(DayState<T>) -> Unit,
    monthContainer: @Composable (content: @Composable (PaddingValues) -> Unit) -> Unit,
){
    Column {
        monthContainer { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues),
            ) {
                currentMonth.getWeeks(
                    includeAdjacentMonths = showAdjacentMonths,
                    firstDayOfTheWeek = daysOfWeek.first(),
                    today = today,
                ).forEach { week ->
                    WeekRow(
                        weekDays = week,
                        selectionState =  selectionState,
                        dayItem = dayContent,
                    )
                }
            }
        }
    }
}

@Composable
internal fun <T : SelectionState> WeekRow(
    selectionState: T,
    weekDays: WeekDays,
    modifier: Modifier = Modifier,
    dayItem: @Composable (BoxScope.(DayState<T>) -> Unit),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (weekDays.isFirstWeekOfTheMonth) Arrangement.End else Arrangement.Start
    ) {
        weekDays.days.forEachIndexed{index, day ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f / (CalendarUtils.DaysInAWeek - index))
            ) {
                dayItem(DayState(day, selectionState))
            }
        }
    }
}

@Composable
fun <T : SelectionState> DayItem(
    state: DayState<T>,
    onClick: (LocalDate) -> Unit = {},
) {
    val dataState = state.date
    val selectionState = state.selectionState
    val isSelected = selectionState.onDaySelected(dataState)

    if (isSelected) {
        selectionState.onDateSelected(dataState)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        DayCellItem(
            data = CalendarData.Date(
                date = dataState,
                isSelected = isSelected,
                isLesson = false
//                isLesson = Lesson.LessonIsDate(
//                    dataState
//                )
            ),
            modifier = Modifier
                .clickable {
                    onClick(dataState)
                    selectionState.onDateSelected(dataState)
                },
        )
    }
}

internal const val DefaultCalendarPagerRange = 10_000L

internal infix fun DayOfWeek.daysUntil(other: DayOfWeek) = (7 + (value - other.value)) % DaysInAWeek

internal fun <T> Array<T>.rotateRight(n: Int): List<T> = takeLast(n) + dropLast(n)

@Stable
interface SelectionState {
    fun isDateSelected(date: LocalDate): Boolean = false
    fun onDaySelected(date: LocalDate): Boolean = false
    fun onDateSelected(date: LocalDate){}
}

@Composable
fun rememberSelectableCalendarState(
    initialMonth: YearMonth = YearMonth.now(),
    initialSelection: LocalDate = LocalDate.now(),
    minMonth: YearMonth = initialMonth.minusMonths(DefaultCalendarPagerRange),
    maxMonth: YearMonth = initialMonth.plusMonths(DefaultCalendarPagerRange),
    confirmSelectionChange: (newValue: LocalDate) -> Boolean = { true },
    monthState: MonthState = rememberSaveable(saver = MonthState.Saver()) {
        MonthState(
            initialMonth = initialMonth,
            minMonth = minMonth,
            maxMonth = maxMonth
        )
    },
    selectionState: DynamicSelectionState = rememberSaveable(
        saver = DynamicSelectionState.Saver(confirmSelectionChange),
    ) {
        DynamicSelectionState(confirmSelectionChange,initialSelection)
    },
): CalendarState<DynamicSelectionState> = remember { CalendarState(monthState, selectionState) }

@Stable
class DynamicSelectionState(
    private val confirmSelectionChange: (newValue: LocalDate) -> Boolean = { true },
    selection: LocalDate
) : SelectionState {

    private var _selection by mutableStateOf(selection)

    var selection: LocalDate
        get() = _selection
        set(value) {
            if (value != selection && confirmSelectionChange(value)) {
                _selection = value
            }
        }

    override fun isDateSelected(date: LocalDate): Boolean =
        selection == date

    override fun onDaySelected(date: LocalDate): Boolean =
        selection.dayOfMonth == date.dayOfMonth

    override fun onDateSelected(date: LocalDate) {
        selection = date
    }

    internal companion object {
        fun Saver(
            confirmSelectionChange: (newValue: LocalDate) -> Boolean,
        ): Saver<DynamicSelectionState, Any> =
            listSaver(
                save = { raw -> listOf(listOf(raw.selection).map { it.toString() })},
                restore = { restored ->
                    DynamicSelectionState(
                        confirmSelectionChange = confirmSelectionChange,
                        selection  = restored[0] as LocalDate
                    )
                }
            )
    }
}

@Stable
class CalendarState<T : SelectionState>(
    val monthState: MonthState,
    val selectionState: T,
)


@Preview
@Composable
fun CalendarMonthPreview(){
    val dataSource = CalendarDataSource()
    var data by remember { mutableStateOf(dataSource.getDataWeek(selectedDate=dataSource.today))}
    val showCalendar = remember { mutableStateOf(false) }
    showCalendar.value=true
    CalendarMonth(
        data=data,
        showCalendar=showCalendar,
        onDataMonth = {}
    ){
            date -> data = data.copy(
        selectedDate = date,
        visibleDates = data.visibleDates.map{
            it.copy(
                isSelected = it.date.isEqual(date.date)
            ) })
    }
}

@Preview
@Composable
fun MonthYearPreview(
    today: LocalDate = LocalDate.now(),
    calendarState: CalendarState<DynamicSelectionState> = rememberSelectableCalendarState(
        initialMonth = YearMonth.of(today.year, today.month) ,
        initialSelection = today
    ),
){
    var showMonthYearPicker by remember { mutableStateOf(false) }

    val monthState = calendarState.monthState
    MonthYearPickerDialog(
        initialMonth = monthState.currentMonth.month.value - 1,
        initialYear = monthState.currentMonth.year,
        onConfirm = { month, year ->
//                        onMonthYearSelectяed(1, 1)
            showMonthYearPicker = false
        },
    )
}