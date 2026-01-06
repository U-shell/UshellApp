package ru.ushell.app.screens.schedule.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.ushell.app.data.features.attendance.mappers.Attendance
import ru.ushell.app.screens.schedule.view.timetable.TimetableUiState
import ru.ushell.app.screens.schedule.view.timetable.TimetableViewModel

import java.time.LocalDate

@Composable
fun ListLesson(
    modifier: Modifier = Modifier,
    date: LocalDate,
    viewModel: TimetableViewModel = hiltViewModel()
) {

    LaunchedEffect(date) {
        viewModel.loadTimetable(date)
    }

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is TimetableUiState.Empty,
        is TimetableUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TimetableUiState.Success -> {
            val lessons = (uiState as TimetableUiState.Success).lessons

            if (lessons.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter ) {
                    MessageView()
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lessons) { lesson ->
                        LessonItem(
                            lesson = lesson,
                            date = date,
                            attendance = attendanceStatus(
                                numLesson = lesson.numLesson,
                                attendance = (uiState as TimetableUiState.Success).attendance
                            )
                        )
                    }
                }
            }
        }

        is TimetableUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter ) {
                    MessageView()
                }
                Text((uiState as TimetableUiState.Error).message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

//
///**
// * 0 = false
// * 1 = true
// * 2 = null
// */

fun attendanceStatus(
    numLesson: Int,
    attendance: List<Attendance>?
): Int {
    var status = 2

    if(attendance?.isEmpty() ?: false){
        return status
    }

    if (attendance != null) {
        for(attendance in attendance){
            if(attendance.numLesson == numLesson && !attendance.status){
                status = 0
            } else if (attendance.numLesson == numLesson){
                status = 1
            }
        }
    }

    return status
}
