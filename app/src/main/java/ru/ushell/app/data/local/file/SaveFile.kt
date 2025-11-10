package ru.ushell.app.data.local.file

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SaveFile() {

    suspend fun saveFileToCache(
        context: Context,
        body: ResponseBody,
        displayName: String
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val cacheDir = File(context.cacheDir, "downloaded_files")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val file = File(cacheDir, displayName)

            // Проверяем, существует ли файл в кэше
            if (file.exists()) {
                // Возвращаем Uri для существующего файла
                return@withContext Uri.fromFile(file)
            }

            // Если файла нет, сохраняем его
            file.outputStream().use { outputStream ->
                body.byteStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            // Возвращаем Uri для нового файла
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Функция для сохранения файла в загрузки (Downloads)
    suspend fun saveFileToDownloads(
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
                    val extension = displayName.substringAfterLast('.', "")
                    if (extension.isNotEmpty()) {
                        when (extension.lowercase()) {
                            "jpg", "jpeg", "png", "gif", "webp" -> "image/${extension.lowercase()}"
                            "mp4", "avi", "mov", "mkv" -> "video/${extension.lowercase()}"
                            "mp3", "wav", "flac" -> "audio/${extension.lowercase()}"
                            "pdf" -> "application/pdf"
                            "txt" -> "text/plain"
                            "doc", "docx" -> "application/msword"
                            "xls", "xlsx" -> "application/vnd.ms-excel"
                            else -> "application/octet-stream"
                        }
                    } else {
                        "application/octet-stream"
                    }
                }

            val contentValues = ContentValues().apply {
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, displayName)
                put(MediaStore.Files.FileColumns.MIME_TYPE, resolvedMimeType)
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            var outputStream: OutputStream? = null
            var uri: Uri? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ — используем MediaStore
                uri = resolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), contentValues)
                outputStream = uri?.let { resolver.openOutputStream(it) }
            } else {
                // Android 9 и ниже — прямая запись (требует разрешения!)
                val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    throw SecurityException("WRITE_EXTERNAL_STORAGE permission is required on Android < 10")
                }

                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, displayName)
                outputStream = FileOutputStream(file)
            }

            if (outputStream != null) {
                body.byteStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
                outputStream.flush()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 0)
                    uri?.let { resolver.update(it, contentValues, null, null) }
                }
                uri
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}