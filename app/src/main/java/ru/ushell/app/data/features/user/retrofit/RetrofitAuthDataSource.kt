package ru.ushell.app.data.features.user.retrofit

import ru.ushell.app.data.features.user.UserRemoteDataSource
import ru.ushell.app.data.features.user.remote.auth.AuthApi
import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse

class RetrofitAuthDataSource(val authApi: AuthApi): UserRemoteDataSource {

    override suspend fun getLoginUser(basic: String): AuthInfoUserResponse = authApi.loginUser(basic)
}