package ru.ushell.app.data.features.user.remote.auth

import retrofit2.http.GET

interface AuthApi {

    @GET("./auth/login")
    fun loginUser(): AuthInfoUserResponse

    @GET("./auth/logout")
    fun logoutUser()
}

