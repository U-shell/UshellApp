package ru.ushell.app.data.features.user.remote.auth

data class AuthInfoUserResponse(
    val username: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val group: UserGroup,
    val roles: MutableList<String?>,
    val accessToken: String,
    val refreshToken: String,
)

data class UserGroup(
    val id: Int,
    val title: String,
    val specialization: String,
    val profile: String,
    val institute: String,
)