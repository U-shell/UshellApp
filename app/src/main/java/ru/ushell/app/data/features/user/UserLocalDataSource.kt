package ru.ushell.app.data.features.user

import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserEntity

interface UserLocalDataSource{

    suspend fun activeUser(): Boolean

    suspend fun saveRemoteResponse(userEntity: AuthInfoUserResponse)

    suspend fun getInfoUser(): UserEntity

    suspend fun getGroupId(): Int
}
