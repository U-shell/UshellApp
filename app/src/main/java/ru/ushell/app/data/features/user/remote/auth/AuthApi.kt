package ru.ushell.app.data.features.user.remote.auth

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Header
import ru.ushell.app.data.features.user.remote.dto.AuthInfoUserResponse
import ru.ushell.app.data.features.user.remote.dto.RefreshAccessTokenResponse

interface AuthApi {

    @GET("./auth/login")
    suspend fun loginUser(
        @Header("Authorization") basic: String
    ): AuthInfoUserResponse


    @GET("./auth/logout")
    fun logoutUser()


    @PUT("./refresh/access")
    suspend fun refreshAccessToken(
        @Header("Authorization") refreshToken: String
    ): RefreshAccessTokenResponse
}

