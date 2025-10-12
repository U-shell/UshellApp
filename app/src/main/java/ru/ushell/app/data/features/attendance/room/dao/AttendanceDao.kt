package ru.ushell.app.data.features.attendance.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ushell.app.data.features.attendance.room.dao.AttendanceEntity.Companion.TABLE_NAME

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAttendance(attendanceEntity: AttendanceEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE username =:username")
    suspend fun getAttendance(username: String): AttendanceEntity

    @Query("SELECT statistic FROM $TABLE_NAME WHERE username =:username")
    suspend fun getStatistic(username: String): Int

}