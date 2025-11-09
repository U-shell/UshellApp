package ru.ushell.app.data.features.messenger

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import ru.ushell.app.data.features.messenger.mappers.Chat
import ru.ushell.app.data.features.messenger.mappers.ChatList
import ru.ushell.app.data.features.messenger.mappers.Message
import ru.ushell.app.data.features.messenger.dto.BodyRequestMessageChat
import ru.ushell.app.data.features.messenger.dto.MessageChatResponse
import ru.ushell.app.data.features.messenger.dto.MessageType
import ru.ushell.app.data.features.messenger.remote.webSocket.Connect
import ru.ushell.app.data.features.user.UserRepository
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class MessengerRepository(
    private val messengerLocalDataSource: MessengerLocalDataSource,
    private val messengerRemoteDataSource: MessengerRemoteDataSource,
    private val userRepository: UserRepository,
) {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private var connect: Connect? = null

    private val _incomingMessages = Channel<Message>(Channel.UNLIMITED)
    val incomingMessages: Flow<Message> = _incomingMessages.receiveAsFlow()

    suspend fun getInfoUserMessenger() = messengerRemoteDataSource.getInfoUserMessenger().also {
        userRepository.setChatId(it.id)
    }

    suspend fun getMessageChat(recipientId: String, context: Context): MutableList<Message> {
        val senderId = userRepository.getChatId()
        val body = BodyRequestMessageChat(senderId = senderId, recipientId = recipientId)

        val result: List<MessageChatResponse> = messengerRemoteDataSource.getMessageChat(body)
        val messages = mutableListOf<Message>()

        result.forEach {
            if (it.type == MessageType.FILE || it.type == MessageType.IMAGE){
                val uri = downloadFile(
                    recipientId = recipientId,
                    fileName = it.fileName,
                    context = context
                )
                messages.add(
                    Message(
                        author = senderId == it.senderId,
                        message = it.message,
                        type = it.type,
                        uri = uri,
                        timestamp = OffsetDateTime.parse(it.timestamp, formatter)
                    )
                )
            } else{
            messages.add(
                Message(
                    author = senderId == it.senderId,
                    message = it.message,
                    type = it.type,
                    timestamp = OffsetDateTime.parse(it.timestamp, formatter)
                )
            )
                }
        }
        messages.reverse()
        return messages
    }

    suspend fun getAllUser() = messengerRemoteDataSource.getAllUsers().listUsers?.forEach {
        ChatList.add(
            Chat(
                it.username,
                it.name,
                it.id,
                0
//                    getCountNewMessage(userRepository.getChatId(),it.id)
            )
        )
    }

    suspend fun getCountNewMessage(senderId: String, recipientId: String): Int =
        messengerRemoteDataSource.getCountNewMessage(
            BodyRequestMessageChat(
                senderId = senderId,
                recipientId = recipientId
            )
        )

    suspend fun connectWebSocket() {
        if (connect?.isConnected() == true) return

        val chatId = userRepository.getChatId()
        connect = Connect(chatId) { message ->
            _incomingMessages.trySend(message)
        }
        connect?.connect()
    }

    fun disconnectWebSocket() {
        connect?.disconnect()
        connect = null
    }

    suspend fun sendMessage(recipientId:String, message: Message) {
        if (connect?.isConnected() == true) {
            connect?.sendChatMessage(recipientId, message)
            if(message.uri != null) {
                uploadFile(recipientId = recipientId, message = message)
            }
        } else {
            throw IllegalStateException("WebSocket not connected")
        }
    }

    suspend fun uploadFile(recipientId:String, message: Message) {

        val fileBody = message.file!!.toRequestBody("application/octet-stream".toMediaType())
        val file = MultipartBody.Part.createFormData("file", message.fileName, fileBody)

        messengerRemoteDataSource.uploadFile(senderId = userRepository.getChatId(), resenderId = recipientId, file = file)
    }

    suspend fun downloadFile(recipientId:String, fileName: String, context: Context): Uri? {
        val response = messengerRemoteDataSource.getDownloadFile(
            senderId = userRepository.getChatId(),
            resenderId = recipientId,
            fileName)


        return saveFileToDevice(context = context, body = response.body()!!, displayName = fileName)
    }

    suspend fun saveFileToDevice(
        context: Context,
        body: ResponseBody,
        displayName: String,
        mimeType: String? = null
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver

            // Пытаемся определить MIME-тип, если не предоставлен
            val resolvedMimeType = mimeType
                ?: run {
                    // Простая эвристика по расширению (если displayName содержит её)
                    val extension = displayName.substringAfterLast('.', "")
                    if (extension.isNotEmpty()) {
                        when (extension.lowercase()) {
                            "jpg", "jpeg", "png", "gif", "webp" -> "image/${extension.lowercase()}"
                            "mp4", "avi", "mov", "mkv" -> "video/${extension.lowercase()}"
                            "mp3", "wav", "flac" -> "audio/${extension.lowercase()}"
                            "pdf" -> "application/pdf"
                            "txt" -> "text/plain"
                            "doc", "docx" -> "application/msword" // Упрощённо
                            "xls", "xlsx" -> "application/vnd.ms-excel" // Упрощённо
                            else -> "application/octet-stream" // Неизвестный тип
                        }
                    } else {
                        "application/octet-stream" // Неизвестный тип, если нет расширения
                    }
                }

            val contentValues = ContentValues().apply {
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, displayName)
                put(MediaStore.Files.FileColumns.MIME_TYPE, resolvedMimeType)
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Ushell") // Меняйте путь по необходимости
                // put(MediaStore.Files.FileColumns.IS_PENDING, 1) // Можно использовать для улучшения UX
            }

            var outputStream: OutputStream? = null
            var uri: Uri? = null // Объявляем uri как переменную

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ — используем MediaStore
                uri = resolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), contentValues)
                outputStream = uri?.let { resolver.openOutputStream(it) }
            } else {
                // Android 9 и ниже — прямая запись (требует разрешения!)
                val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    // Обычно проверка разрешения делается в Activity/Fragment перед вызовом этой функции
                    throw SecurityException("WRITE_EXTERNAL_STORAGE permission is required on Android < 10")
                }

                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val appDir = File(downloadsDir, "YourAppName") // Меняйте имя папки
                appDir.mkdirs()
                val file = File(appDir, displayName)
                outputStream = FileOutputStream(file)
            }

            if (outputStream != null) {
                body.byteStream().use { inputStream ->
                    outputStream.use { os ->
                        inputStream.copyTo(os)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 0) // Снимаем флаг ожидания
                    uri?.let { resolver.update(it, contentValues, null, null) }
                }
                // Возвращаем uri, если всё прошло успешно
                uri
            } else {
                // Возвращаем null, если не удалось получить поток
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Возвращаем null в случае ошибки
            null
        }
    }

}