package ru.ushell.app.data.features.timetabel

import ru.ushell.app.data.features.timetabel.remote.timetable.TimetableResponse
import ru.ushell.app.data.features.timetabel.room.dao.lesson.Lesson
import ru.ushell.app.data.features.timetabel.room.dao.main.TimetableEntity
import ru.ushell.app.data.features.timetabel.room.dao.secondary.TimetableSecondaryEntity
import ru.ushell.app.data.features.timetabel.room.toLessonItem
import ru.ushell.app.data.features.user.UserRepository
import ru.ushell.app.screens.timetable.calendar.CalendarUtils
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.ParityWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimetableRepository(
    private val timetableLocalDataSource: TimetableLocalDataSource,
    private val timetableRemoteDataSource: TimetableRemoteDataSource,
    private val userRepository: UserRepository
) {

    suspend fun saveTimetable(){
        processingTimetable(
            timetableRemoteDataSource.getTimetableGroup(
                userRepository.getGroupId()
            )
        )
    }

    suspend fun getTimetable(data: LocalDate): List<Lesson> {
        return getListLesson(
            timetableLocalDataSource.getPrimaryTimetable(),
            timetableLocalDataSource.getSecondaryTimetable(),
            data
        )
    }

    private suspend fun processingTimetable(response: TimetableResponse){
        response.mainSchedule.forEach { (weekStr, days) ->
            val week = weekStr.toIntOrNull() ?: return@forEach
            days.forEach { (daysOfWeek, lesson) ->
                lesson.forEach { (numLessonStr, lesson) ->
                    val numLesson = numLessonStr.toIntOrNull() ?: return@forEach
                    timetableLocalDataSource.savePrimaryTimetable(
                        TimetableEntity(
                            week = week,
                            dayOfWeek = daysOfWeek,
                            numLesson = numLesson,
                            subject = lesson.subjectName,
                            type = lesson.fullType,
                            withWhom = lesson.withWhom,
                            timeStart = parseTime(lesson.timeStart),
                            timeEnd = parseTime(lesson.timeEnd),
                            classroom = lesson.classroom,
                            subgroup = lesson.subgroup
                        )
                    )
                }
            }
        }

        response.secondarySchedule.forEach { (dateLessonStr, days) ->
            val dateLesson = parseDate(dateLessonStr)
            days.forEach { (daysOfWeek, lesson) ->
                lesson.forEach { (numLessonStr, lesson) ->
                    val numLesson = numLessonStr.toIntOrNull() ?: return@forEach
                    timetableLocalDataSource.saveSecondaryTimetable(
                        TimetableSecondaryEntity(
                            dateLesson = dateLesson,
                            dayOfWeek = daysOfWeek,
                            numLesson = numLesson,
                            subject = lesson.subjectName,
                            type = lesson.fullType,
                            withWhom = lesson.withWhom,
                            timeStart = parseTime(lesson.timeStart),
                            timeEnd = parseTime(lesson.timeEnd),
                            classroom = lesson.classroom,
                            subgroup = lesson.subgroup
                        )
                    )
                }
            }
        }
    }

    private fun parseDate(dateLesson: String): String {
        return LocalDate.parse(dateLesson, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
    }

    private fun parseTime(timeLesson: String): String {
        return LocalTime.parse(timeLesson, DateTimeFormatter.ofPattern("HH:mm:ss")).toString()
    }

    private fun getListLesson(
        primaryTimetable: List<TimetableEntity>,
        secondaryTimetable: List<TimetableSecondaryEntity>,
        date: LocalDate
    ): List<Lesson> {

        val targetWeek = ParityWeek(date)
        val targetDay = CalendarUtils.DayOfWeek(date)
        val targetDate = CalendarUtils.formattedDateToDbWeek(date)

        val filteredSecondary = secondaryTimetable
            .filter { it.dateLesson == targetDate && it.dayOfWeek.lowercase() == targetDay }
            .associateBy { it.numLesson }

        val usedSecondary = mutableSetOf<Int>()

        val result = primaryTimetable
            .filter { it.week == targetWeek && it.dayOfWeek.lowercase() == targetDay }
            .map { primary ->
                val secondary = filteredSecondary[primary.numLesson]
                if (secondary != null) {
                    usedSecondary.add(primary.numLesson)
                    secondary.toLessonItem(targetWeek, targetDay)
                } else {
                    primary.toLessonItem(targetWeek, targetDay)
                }
            }
            .toMutableList()

        val remainingSecondary = filteredSecondary
            .filterKeys { it !in usedSecondary }
            .values
            .map { it.toLessonItem(targetWeek, targetDay) }

        result.addAll(remainingSecondary)

        return result.sortedBy { it.numLesson }
    }
}