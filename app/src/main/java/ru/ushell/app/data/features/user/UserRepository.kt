package ru.ushell.app.data.features.user

import okhttp3.Credentials
import ru.ushell.app.data.features.user.remote.dto.RefreshAccessTokenResponse
import ru.ushell.app.data.features.user.remote.webSocket.Connect
import ru.ushell.app.data.features.user.room.dao.UserEntity
import ru.ushell.app.domain.service.token.TokenService

class UserRepository(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenService: TokenService,
) {
    private var connect: Connect? = null

    suspend fun activeUser(): Boolean {
        return userLocalDataSource.activeUser()
    }

    suspend fun loginUser(email: String, password: String) =
        userRemoteDataSource.getLoginUser(basic = Credentials.basic(email, password))
            .also {
                userLocalDataSource.saveRemoteResponse(it)
                tokenService.saveAccessToken(it.accessToken, it.accessValid)
            }

    suspend fun logoutUser() =  userLocalDataSource.logoutUser(username = getUsername())

    suspend fun getInfoUser(): UserEntity = userLocalDataSource.getInfoUser()

    suspend fun getUsername(): String = userLocalDataSource.getUsername()

    suspend fun getGroupId(): Int = userLocalDataSource.getGroupId()

    suspend fun setChatId(chatId: String) = userLocalDataSource.setChatId(chatId)

    suspend fun getChatId(): String = userLocalDataSource.getChaId()

    private suspend fun saveAccessToken(token: String) = userLocalDataSource.saveAccessToken(token)

    private suspend fun getAccessToken() = userLocalDataSource.getAccessToken()

    private suspend fun getRefreshToken() = userLocalDataSource.getRefreshToken()

    suspend fun refreshAccessToken(): RefreshAccessTokenResponse {
        val tokens: RefreshAccessTokenResponse = userRemoteDataSource.refreshAccessToken(getRefreshToken())
        saveAccessToken(tokens.accessToken)
        return tokens
    }

    suspend fun connectWebSocket() {
        if (connect?.isConnected() == true) return
        connect = Connect(username = getUsername())
        connect?.connect()
        val isConnected = connect?.awaitConnection() == true
        if (!isConnected) {
            throw IllegalStateException("Failed to connect WebSocket")
        }
    }

    fun disconnectWebSocket() {
        connect?.disconnect()
        connect = null
    }

    suspend fun sendMessage(code: String) {
        connect?.sendQrMessage(code, getAccessToken())
    }

    suspend fun sendFile(code: String, resenderId:String, fileName:String) {
        connect?.sendFileMessage(
            qrCode = code,
            senderId = userLocalDataSource.getChaId(),
            resenderId = resenderId,
            fileName = fileName
        )
    }


}