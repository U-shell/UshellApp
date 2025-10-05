package ru.ushell.app.data.features.user

import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserEntity

fun AuthInfoUserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        patronymic = this.patronymic,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}