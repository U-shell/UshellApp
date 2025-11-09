package ru.ushell.app.data.features.user.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import ru.ushell.app.data.features.user.remote.dto.AuthInfoUserResponse
import ru.ushell.app.data.features.user.remote.dto.RefreshAccessTokenResponse

interface AuthApi {

    @GET("./auth/login")
    suspend fun loginUser(
        @Header("Authorization") basic: String
    ): AuthInfoUserResponse


    @GET("./auth/logout")
    fun logoutUser()


    @PUT("./auth/refresh/access")
    suspend fun refreshAccessToken(
        @Header("Authorization") refreshToken: String
    ): RefreshAccessTokenResponse
}