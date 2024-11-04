package ru.ushell.app.api.body.chat

class BodyRequestUserDetails {
    private var username: String? = null

    fun setUsername(username: String?): BodyRequestUserDetails {
        this.username = username
        return this
    }
}