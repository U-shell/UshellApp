package ru.ushell.app.ui.screens.timeTable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import ru.ushell.app.R
import ru.ushell.app.api.Service
import ru.ushell.app.models.User
import ru.ushell.app.ui.screens.TopPanelScreen
import ru.ushell.app.ui.screens.backgroundImagesSmall
import ru.ushell.app.ui.screens.timeTable.lesson.ListLesson
import ru.ushell.app.ui.theme.BottomBackgroundAlfa
import ru.ushell.app.ui.theme.ListColorButton
import ru.ushell.app.ui.theme.TimeTableText
import ru.ushell.app.ui.utils.calendar.CalendarUtils
import ru.ushell.app.ui.utils.calendar.month.CalendarMonthDialog
import ru.ushell.app.ui.utils.calendar.week.CalendarData
import ru.ushell.app.ui.utils.calendar.week.CalendarDataSource
import ru.ushell.app.ui.utils.calendar.week.CalendarWeek
import java.time.temporal.ChronoUnit

//Когда использовать каждый из эффектов
//
//Примеры использования DisposableEffect
//
    //Добавление и удаление слушателей событий
    //Запуск и остановка анимации
    //Привязка и отвязка ресурсов сенсоров, таких как Camera, LocationManager и т.д.
    //Управление соединениями с базой данных
    //
//Примеры использования LaunchedEffect
//
    //Получение данных из сети
    //Выполнение обработки изображений
    //Обновление базы данных
    //
//Примеры использования SideEffect
//
    //Ведение логов и аналитика
    //Выполнение однократной инициализации, например, установка соединения с Bluetooth-устройством, загрузка данных из файла или инициализация библиотеки.
    //
//Приведем пример использования SideEffect для однократной инициализации:

//@Composable
//fun MyComposable() {
//    val isInitialized = remember { mutableStateOf(false) }
//
//    SideEffect {
//        if (!isInitialized.value) {
//             Execute one-time initialization tasks here
//            initializeBluetooth()
//            loadDataFromFile()
//            initializeLibrary()
//
//            isInitialized.value = true
//        }
//    }
//
//     UI code here
//}
@Composable
fun TimeTableScreen() {
    var isRefreshing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dataSource = CalendarDataSource()
    var currantData by remember { mutableStateOf(dataSource.getDataWeek(selectedDate=dataSource.today)) }

    LaunchedEffect(isRefreshing){
        if (isRefreshing) {

            Service(context).updateData()
            delay(2000)
            currantData = dataSource.getDataWeek(selectedDate = currantData.selectedDate.date)

            isRefreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        refreshTriggerDistance=100.dp,
        onRefresh = {
            isRefreshing = true
        }
    ) {
        TimeTableContext(dataSource,currantData)
    }
}


@Composable
fun TimeTableContext(dataSource:CalendarDataSource, initialData: CalendarData) {

    var currantData by remember { mutableStateOf(initialData) }
    var pastData by remember { mutableStateOf(dataSource.getDataWeek(selectedDate=dataSource.today)) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(color = Color(0xFFE7E7E7))
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(
            modifier = Modifier
        ){
            val (topPanel, lesson, backgroundImage) = createRefs()
            val barrier = createBottomBarrier(lesson)
            val height = 250.dp
            Box(
                modifier = Modifier
                    .constrainAs(backgroundImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .backgroundImagesSmall(height = height)
            )
            Box(
                modifier = Modifier
                    .constrainAs(topPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){

                TopPanelCalendar(
                    data = currantData,
                    onDataMonth = {
                        pastData = dataSource.getDataWeek(currantData.selectedDate.date)
                        currantData = dataSource.getDataWeek(
                            currantData.selectedDate.plusWeeks(
                                ChronoUnit.WEEKS.between(
                                    currantData.selectedDate.date, pastData.selectedDate.date)
                            )
                        )

                    },
                    onNavigatePrevious = {
                        currantData = dataSource.getDataWeek(currantData.selectedDate.minusWeeks())
                    },
                    onNavigateNext = {
                        currantData = dataSource.getDataWeek(currantData.selectedDate.plusWeeks())
                    },
                ){
                    date -> currantData = currantData.copy(
                    selectedDate = date,
                    visibleDates = currantData.visibleDates.map{
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        ) })
                }
            }
            Box(
                modifier = Modifier
                    .zIndex(-1f)
                    .constrainAs(lesson) {
                        top.linkTo(backgroundImage.bottom)
//                        top.linkTo(backgroundImage.bottom, margin = ((-20).dp))
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(barrier)
                    }
                    .padding(bottom = height + 70.dp)
                    .fillMaxHeight()
                    .navigationBarsPadding()
            ) {
                ListLesson(
//                    modifier = Modifier.offset(y=20.dp),
                    date=currantData.selectedDate.date
                )
            }
        }
    }
}

@Composable
fun TopPanelCalendar(
    data: CalendarData,
    onDataMonth: () -> Unit,
    onNavigatePrevious: () -> Unit,
    onNavigateNext: () -> Unit,
    onDateClickListener: (CalendarData.Date) -> Unit,
) {
    val showCalendar = remember { mutableStateOf(false) }
    TopPanelScreen(
        titleContext = { TT_title(data = data, showCalendar) }
    ){
        Box(
            modifier = Modifier
                .padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 20.dp,
                )
        ) {
            CalendarWeek(
                data = data,
                onNavigatePrevious = onNavigatePrevious,
                onNavigateNext = onNavigateNext,
                onDateClickListener
            )
        }

        if (showCalendar.value) {
            CalendarMonthDialog(
                today = data.selectedDate.date,
                data = data,
                onDismiss = {
                    showCalendar.value = false
                },
                onDataMonth=onDataMonth,
                showCalendar=showCalendar,
                onDateClickListener=onDateClickListener
            )
        }
    }
}

@Composable
fun TT_title(
    data: CalendarData,
    showCalendar: MutableState<Boolean>,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box{
                Text(
                    text = stringResource(id = R.string.timetable),
                    style = TimeTableText
                )
            }
            Box(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    )
            ){
                CalendarButton(
                    text = CalendarUtils.formattedMonthYear(data.selectedDate.date),
                    onClick = { showCalendar.value = true } // Открываем календарь при нажатии на кнопку
                )
            }
        }

        Box {
            Icon(
                painterResource(id = R.drawable.timetable_mini_logo),
                contentDescription = null,
                tint = Color.White
            )
        }
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
    User.getInstance(LocalContext.current)
    val dataSource = CalendarDataSource()
    var currantData by remember { mutableStateOf(dataSource.getDataWeek(selectedDate=dataSource.today)) }
    TimeTableContext(dataSource=dataSource,initialData=currantData)
}
