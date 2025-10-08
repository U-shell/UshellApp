package ru.ushell.app.base

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ushell.app.data.features.attendance.room.dao.AttendanceDao
import ru.ushell.app.data.features.attendance.room.dao.AttendanceEntity
import ru.ushell.app.data.features.timetabel.room.dao.main.TimetableDao
import ru.ushell.app.data.features.timetabel.room.dao.main.TimetableEntity
import ru.ushell.app.data.features.timetabel.room.dao.secondary.TimetableSecondaryDao
import ru.ushell.app.data.features.timetabel.room.dao.secondary.TimetableSecondaryEntity
import ru.ushell.app.data.features.user.room.dao.UserDao
import ru.ushell.app.data.features.user.room.dao.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TimetableEntity::class,
        TimetableSecondaryEntity::class,
        AttendanceEntity::class
    ], version = 14, exportSchema = true
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun timetableDao(): TimetableDao

    abstract fun timetableSecondaryDao(): TimetableSecondaryDao

    abstract fun attendanceDao(): AttendanceDao
}