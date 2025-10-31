package ru.ushell.app.data.features.user.remote.dto

data class RefreshAccessTokenResponse(
    val validationAccess: Long,
    val accessToken: String
)