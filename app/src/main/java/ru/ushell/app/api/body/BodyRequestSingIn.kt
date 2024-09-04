package ru.ushell.app.api.body

class BodyRequestSingIn {
    private var email: String? = null
    private var password: String? = null

    fun setEmail(email: String?): BodyRequestSingIn {
        this.email = email
        return this
    }

    fun setPassword(password: String?): BodyRequestSingIn {
        this.password = password
        return this
    }
}
