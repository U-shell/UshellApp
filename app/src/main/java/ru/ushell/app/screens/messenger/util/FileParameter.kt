package ru.ushell.app.screens.messenger.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.Formatter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File

var fileNameShare: String = ""
var recipientIdShare: String = ""

@Composable
fun parameterFile(fileUri: Uri): List<String> {
    val context = LocalContext.current

    val fileInfo = remember(fileUri) {
        runCatching {
            when (fileUri.scheme) {
                "file" -> {
                    val file = File(fileUri.path ?: fileUri.toString())
                    Pair(file.name, file.length())
                }
                "content" -> {
                    val cursor = context.contentResolver.query(
                        fileUri,
                        arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                        null,
                        null,
                        null
                    )
                    cursor?.use { c ->
                        if (c.moveToFirst()) {
                            val nameIndex = c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                            val sizeIndex = c.getColumnIndexOrThrow(OpenableColumns.SIZE)
                            val displayName = c.getString(nameIndex)
                            val sizeInBytes = c.getLong(sizeIndex)
                            Pair(displayName ?: "Файл", sizeInBytes)
                        } else {
                            Pair("Файл", 0L)
                        }
                    } ?: Pair("Файл", 0L)
                }
                else -> {
                    Pair("Файл", 0L)
                }
            }
        }.getOrNull() ?: Pair("Файл", 0L)
    }

    val fileName = fileInfo.first
    val fileSize = formatFileSize(context, fileInfo.second)
    var fileExtension = ""

    if (fileName != "Файл" && fileName.contains('.')) {
        fileExtension = fileName.substring(fileName.lastIndexOf('.'))
    }

    return listOf(fileName, fileSize, fileExtension)
}

private fun formatFileSize(context: Context, sizeInBytes: Long): String {
    return Formatter.formatFileSize(context, sizeInBytes)
}