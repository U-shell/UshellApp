package ru.ushell.app.data.features.user.room

import ru.ushell.app.data.features.user.UserLocalDataSource
import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserDao
import ru.ushell.app.data.features.user.room.dao.UserEntity
import ru.ushell.app.data.features.user.toUserEntity

class RoomUserDataSource(val userDao: UserDao): UserLocalDataSource {

    fun loadInfoUser(userEntity: UserEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun saveRemoteResponse(userEntity: AuthInfoUserResponse) {
        return userDao.saveUser(userEntity.toUserEntity())
    }

    override suspend fun getInfoUser(): UserEntity {
        return userDao.getInfoUser()
    }

}