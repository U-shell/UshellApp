package ru.ushell.app.data.features.user

import ru.ushell.app.data.features.user.remote.dto.AuthInfoUserResponse
import ru.ushell.app.data.features.user.remote.dto.RefreshAccessTokenResponse

interface UserRemoteDataSource {

    suspend fun getLoginUser(basic: String): AuthInfoUserResponse

    suspend fun refreshAccessToken(refreshToken: String): RefreshAccessTokenResponse
}