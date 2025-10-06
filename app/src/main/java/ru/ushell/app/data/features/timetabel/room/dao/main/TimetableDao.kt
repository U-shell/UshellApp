package ru.ushell.app.data.features.timetabel.room.dao.main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ushell.app.data.features.timetabel.room.dao.main.TimetableEntity.Companion.TABLE_NAME

@Dao
interface TimetableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun saveUser(timetableEntity: TimetableEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getTimeTable(): List<TimetableEntity>

}