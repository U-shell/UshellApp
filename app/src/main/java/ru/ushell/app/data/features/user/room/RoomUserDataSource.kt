package ru.ushell.app.data.features.user.room

import ru.ushell.app.data.features.user.UserLocalDataSource
import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserDao
import ru.ushell.app.data.features.user.room.dao.UserEntity
import ru.ushell.app.data.features.user.room.dto.toUserEntity

class RoomUserDataSource(val userDao: UserDao): UserLocalDataSource {

    override suspend fun activeUser(): Boolean = userDao.activeUser()

    override suspend fun saveRemoteResponse(userEntity: AuthInfoUserResponse) {
        return userDao.saveUser(userEntity.toUserEntity())
    }

    override suspend fun getInfoUser(): UserEntity = userDao.getInfoUser()

    override suspend fun getUsername(): String = userDao.getUsername()

    override suspend fun getGroupId(): Int = userDao.getGroupId()

}