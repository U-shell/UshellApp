package ru.ushell.app.data.features.user.room.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ushell.app.data.features.user.room.dao.UserEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
//    val group: UserGroup,
//    val roles: MutableList<String?>,
    val accessToken: String,
    val refreshToken: String,

){
    companion object {
        const val TABLE_NAME = "user_entities_table"
    }
}