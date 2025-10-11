package ru.ushell.app.data.features.timetable.room.dao.main

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ushell.app.data.features.timetable.room.dao.main.TimetableEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(
            value = ["week", "dayOfWeek", "numLesson", "subject"],
            unique = true
        )
    ]
)
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val week: Int,
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
        const val TABLE_NAME = "timetable_entities_table"
    }
}