package ru.ushell.app.data.features.timetabel.room.dao.secondary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ushell.app.data.features.timetabel.room.dao.secondary.TimetableSecondaryEntity.Companion.TABLE_NAME

@Dao
interface TimetableSecondaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun saveUser(timetableEntity: TimetableSecondaryEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getTimeTable(): List<TimetableSecondaryEntity>

}