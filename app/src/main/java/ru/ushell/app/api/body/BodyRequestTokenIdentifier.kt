package ru.ushell.app.api.body

import ru.ushell.app.models.User

class BodyRequestTokenIdentifier {
    var accessToken: String? = null
    var typeToken: String? = null


    fun setToken(type: String?, access: String?): String {
        return String.format("%s %s", type, access)
    }

    val token: String
        get() = String.format("%s %s", User.getTypeToken(), User.getAccessToken())
}
