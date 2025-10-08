package ru.ushell.app.data.features.attendance.room.dao

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ushell.app.data.features.attendance.room.dao.AttendanceEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(
            value = ["username"],
            unique = true
        )
    ]
)
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String,
    val statistic: Int,
    val attendance: String

) {
    companion object {
        const val TABLE_NAME = "attendance_entity_table"
    }
}
