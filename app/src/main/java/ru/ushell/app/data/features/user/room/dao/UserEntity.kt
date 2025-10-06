package ru.ushell.app.data.features.user.room.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ushell.app.data.features.user.remote.auth.UserGroup
import ru.ushell.app.data.features.user.room.dao.UserEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String,
    val active: Boolean = true,

    val firstName: String,
    val lastName: String,
    val patronymic: String,

    val groupId: Int,
    val title: String,
    val specialization: String,
    val profile: String,
    val institute: String,

//    val roles: String,

    val accessToken: String,
    val refreshToken: String,

    ){
    companion object {
        const val TABLE_NAME = "user_entities_table"
    }

    fun toUserGroup(): UserGroup = UserGroup(
        id = groupId,
        title = title ,
        specialization = specialization,
        profile = profile,
        institute = institute
    )

}