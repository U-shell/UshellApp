package ru.ushell.app.data.features.user

import ru.ushell.app.data.features.user.room.dao.UserEntity

interface UserLocalDataSource{

    fun loadInfoUser(userEntity: UserEntity)
}