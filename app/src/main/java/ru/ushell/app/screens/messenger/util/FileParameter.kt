package ru.ushell.app.screens.messenger.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.Formatter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun parameterFile(fileUri: Uri): List<String> {
    val context = LocalContext.current

    val fileInfo = remember(fileUri) {
        runCatching {
            val cursor = context.contentResolver.query(
                fileUri,
                arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                null,
                null,
                null
            )
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = c.getColumnIndex(OpenableColumns.SIZE)
                    val displayName = c.getString(nameIndex)
                    val sizeInBytes = c.getLong(sizeIndex)
                    Pair(displayName ?: "Файл", sizeInBytes)
                } else {
                    Pair("Файл", 0L)
                }
            } ?: Pair("Файл", 0L)
        }.getOrNull() ?: Pair("Файл", 0L)
    }

    val fileName = fileInfo.first
    val fileSize = formatFileSize(context, fileInfo.second)
    val fileExtension = fileName.substring(fileName.lastIndexOf("."))

    return listOf(fileName, fileSize, fileExtension)
}

@Composable
private fun formatFileSize(context: Context, bytes: Long): String {
    return remember(bytes) {
        Formatter.formatShortFileSize(context, bytes)
    }
}