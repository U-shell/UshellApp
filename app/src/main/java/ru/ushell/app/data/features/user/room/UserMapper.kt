package ru.ushell.app.data.features.user.room

import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserEntity

fun AuthInfoUserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        patronymic = this.patronymic,
        groupId = this.group.id,
        title = this.group.title,
        specialization = this.group.specialization,
        profile = this.group.profile,
        institute = this.group.profile,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}