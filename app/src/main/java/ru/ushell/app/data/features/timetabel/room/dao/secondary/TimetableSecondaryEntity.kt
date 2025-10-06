package ru.ushell.app.data.features.timetabel.room.dao.secondary

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ushell.app.data.features.timetabel.room.dao.secondary.TimetableSecondaryEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(
            value = ["dateLesson", "dayOfWeek", "numLesson", "subject"],
            unique = true
        )
    ]
)
data class TimetableSecondaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateLesson: String,
    val dayOfWeek: String,
    val numLesson: Int,
    val subject: String,
    val type: String,
    val withWhom: String,
    val timeStart: String,
    val timeEnd: String,
    val classroom: String,
    val subgroup: Int
) {
    companion object {
        const val TABLE_NAME = "timetable_secondary_entities_table"
    }
}