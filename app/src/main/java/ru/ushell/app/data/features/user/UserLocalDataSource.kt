package ru.ushell.app.data.features.user

import ru.ushell.app.data.features.user.remote.dto.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserEntity

interface UserLocalDataSource{

    suspend fun activeUser(): Boolean

    suspend fun logoutUser(username: String)

    suspend fun saveAccessToken(token: String)

    suspend fun getAccessToken(): String

    suspend fun getRefreshToken(): String

    suspend fun saveRemoteResponse(userEntity: AuthInfoUserResponse)

    suspend fun getInfoUser(): UserEntity

    suspend fun getUsername(): String

    suspend fun getGroupId(): Int

    suspend fun setChatId(chatId: String)

    suspend fun getChaId(): String
}
