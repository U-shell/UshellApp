package ru.ushell.app.data.features.timetable

import ru.ushell.app.data.features.timetable.room.dao.main.TimetableEntity
import ru.ushell.app.data.features.timetable.room.dao.secondary.TimetableSecondaryEntity

interface TimetableLocalDataSource {

    suspend fun savePrimaryTimetable(timetableEntity: TimetableEntity)
    suspend fun getPrimaryTimetable(): List<TimetableEntity>

    suspend fun saveSecondaryTimetable(timetableEntity: TimetableSecondaryEntity)
    suspend fun getSecondaryTimetable(): List<TimetableSecondaryEntity>

}