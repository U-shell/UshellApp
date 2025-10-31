package ru.ushell.app.data.features.user.room

import ru.ushell.app.data.features.user.UserLocalDataSource
import ru.ushell.app.data.features.user.remote.dto.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserDao
import ru.ushell.app.data.features.user.room.dao.UserEntity
import ru.ushell.app.data.features.user.mappers.toUserEntity

class RoomUserDataSource(val userDao: UserDao): UserLocalDataSource {

    override suspend fun activeUser(): Boolean = userDao.activeUser()

    override suspend fun logoutUser(username: String) = userDao.setStatusNoActive(username)

    override suspend fun saveAccessToken(token: String) {
        userDao.saveAccessToken(token)
    }

    override suspend fun getAccessToken(): String = userDao.getAccessToken()

    override suspend fun getRefreshToken(): String = userDao.getRefreshToken()

    override suspend fun saveRemoteResponse(userEntity: AuthInfoUserResponse) {
        return userDao.saveUser(userEntity.toUserEntity())
    }

    override suspend fun getInfoUser(): UserEntity = userDao.getInfoUser()

    override suspend fun getUsername(): String = userDao.getUsername()

    override suspend fun getGroupId(): Int = userDao.getGroupId()

    override suspend fun setChatId(chatId: String) = userDao.setChatId(chatId)

    override suspend fun getChaId(): String = userDao.getChatId()

}