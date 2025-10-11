package ru.ushell.app.data.features.timetable.mappers.timetable

import ru.ushell.app.data.features.timetable.room.dao.main.TimetableEntity
import ru.ushell.app.data.features.timetable.room.dao.secondary.TimetableSecondaryEntity
import ru.ushell.app.data.features.timetable.mappers.lesson.Lesson

fun TimetableEntity.toLessonItem(week: Int, dayOfWeek: String): Lesson {
    return Lesson(
        week = week,
        dayOfWeek = dayOfWeek,
        numLesson = this.numLesson,
        subject = this.subject,
        type = this.type,
        withWhom = this.withWhom,
        time = "${this.timeStart} - ${this.timeEnd}",
        classroom = this.classroom,
        subgroup = this.subgroup
    )
}

fun TimetableSecondaryEntity.toLessonItem(week: Int, dayOfWeek: String): Lesson {
    return Lesson(
        week = week,
        dayOfWeek = dayOfWeek,
        numLesson = this.numLesson,
        subject = this.subject,
        type = this.type,
        withWhom = this.withWhom,
        time = "${this.timeStart} - ${this.timeEnd}",
        classroom = this.classroom,
        subgroup = this.subgroup
    )
}