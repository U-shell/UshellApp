package ru.ushell.app.data.features.user

import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import java.util.StringTokenizer

interface UserRemoteDataSource {

    suspend fun getLoginUser(basic: String): AuthInfoUserResponse
}