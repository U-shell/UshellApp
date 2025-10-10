package ru.ushell.app.data.features.timetabel

import ru.ushell.app.data.common.utils.DateTimeUtil.Companion.parseDate
import ru.ushell.app.data.common.utils.DateTimeUtil.Companion.parseTime
import ru.ushell.app.data.features.timetabel.remote.timetable.TimetableResponse
import ru.ushell.app.data.features.timetabel.room.dao.main.TimetableEntity
import ru.ushell.app.data.features.timetabel.room.dao.secondary.TimetableSecondaryEntity
import ru.ushell.app.data.features.timetabel.dto.lesson.Lesson
import ru.ushell.app.data.features.timetabel.dto.lesson.primaryListLesson
import ru.ushell.app.data.features.timetabel.dto.lesson.secondaryListLesson
import ru.ushell.app.data.features.timetabel.dto.timetable.toLessonItem
import ru.ushell.app.data.features.user.UserRepository
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.DayOfWeek
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.ParityWeek
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.formattedDateToDbWeek
import java.time.LocalDate


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

    suspend fun getTimetable(date: LocalDate): List<Lesson> {

        primaryListLesson.addAll(timetableLocalDataSource.getPrimaryTimetable())
        secondaryListLesson.addAll(timetableLocalDataSource.getSecondaryTimetable())

        return getListLesson(
            timetableLocalDataSource.getPrimaryTimetable(),
            timetableLocalDataSource.getSecondaryTimetable(),
            date
        )
    }

    private suspend fun processingTimetable(response: TimetableResponse){
        if(response.mainSchedule.isEmpty()) return
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

        if(response.secondarySchedule.isEmpty()) return
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

    private fun getListLesson(
        primaryTimetable: List<TimetableEntity>,
        secondaryTimetable: List<TimetableSecondaryEntity>,
        date: LocalDate
    ): List<Lesson> {

        val targetWeek = ParityWeek(date)
        val targetDay = DayOfWeek(date)
        val targetDate = formattedDateToDbWeek(date)

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