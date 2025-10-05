package ru.ushell.app.data.features.user.remote.auth

import retrofit2.http.GET
import retrofit2.http.Header

interface AuthApi {

    @GET("./auth/login")
    suspend fun loginUser(
        @Header("Authorization") basic: String
    ): AuthInfoUserResponse


    @GET("./auth/logout")
    fun logoutUser()
}

