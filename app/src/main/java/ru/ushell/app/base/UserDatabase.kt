package ru.ushell.app.base

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ushell.app.data.features.user.room.dao.UserDao
import ru.ushell.app.data.features.user.room.dao.UserEntity

@Database(
    entities = [
        UserEntity::class
    ], version = 1, exportSchema = true
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}