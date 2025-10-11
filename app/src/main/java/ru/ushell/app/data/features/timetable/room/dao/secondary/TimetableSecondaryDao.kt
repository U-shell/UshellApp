package ru.ushell.app.data.features.timetable.room.dao.secondary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimetableSecondaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun saveUser(timetableEntity: TimetableSecondaryEntity)

    @Query("SELECT * FROM ${TimetableSecondaryEntity.Companion.TABLE_NAME}")
    suspend fun getTimeTable(): List<TimetableSecondaryEntity>

}