package ru.ushell.app.data.features.user.room

import ru.ushell.app.data.features.user.UserLocalDataSource
import ru.ushell.app.data.features.user.room.dao.UserDao
import ru.ushell.app.data.features.user.room.dao.UserEntity

class RoomUserDataSource(val userDao: UserDao): UserLocalDataSource {
    override fun loadInfoUser(userEntity: UserEntity) {
        TODO("Not yet implemented")
    }

}