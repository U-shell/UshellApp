package ru.ushell.app.data.features.timetable.room

import ru.ushell.app.data.features.timetable.TimetableLocalDataSource
import ru.ushell.app.data.features.timetable.room.dao.main.TimetableDao
import ru.ushell.app.data.features.timetable.room.dao.main.TimetableEntity
import ru.ushell.app.data.features.timetable.room.dao.secondary.TimetableSecondaryDao
import ru.ushell.app.data.features.timetable.room.dao.secondary.TimetableSecondaryEntity

class RoomTimetableDataSource(
    val timetableDao: TimetableDao,
    val timetableSecondaryDao: TimetableSecondaryDao
): TimetableLocalDataSource {

    override suspend fun savePrimaryTimetable(timetableEntity: TimetableEntity) =
        timetableDao.saveUser(timetableEntity)

    override suspend fun saveSecondaryTimetable(timetableEntity: TimetableSecondaryEntity) =
        timetableSecondaryDao.saveUser(timetableEntity)

    override suspend fun getPrimaryTimetable(): List<TimetableEntity> = timetableDao.getTimeTable()

    override suspend fun getSecondaryTimetable(): List<TimetableSecondaryEntity> = timetableSecondaryDao.getTimeTable()


}