package ru.ushell.app.data.features.user

import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse

interface UserRemoteDataSource {

    suspend fun getLoginUser(basic: String): AuthInfoUserResponse
}