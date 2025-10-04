package ru.ushell.app.data.features.user.auth

import ru.ushell.app.data.features.user.UserLocalDataSource
import ru.ushell.app.data.features.user.UserRemoteDataSource
import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse

class AuthRepository(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) {
    fun loginUser(): AuthInfoUserResponse = userRemoteDataSource.getLoginUser()
}