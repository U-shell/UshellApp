package ru.ushell.app.screens.error

fun getErrorInternetMessage(throwable: Throwable): String {
    return when (throwable) {
        is java.net.UnknownHostException,
        is java.net.ConnectException,
        is java.net.SocketTimeoutException,
        is java.net.SocketException -> {
            "Что-то случилось, уже исправляем  "
        }
        is java.io.IOException -> {
            if (throwable.message?.contains("Failed to connect") == true) {
                "Сервер временно недоступен"
            } else {
                "Ошибка сети"
            }
        }
        is retrofit2.HttpException -> {
            when (throwable.code()) {
                401 -> "Неверный email или пароль"
                403 -> "Доступ запрещён"
                404 -> "Сервер не найден"
                500, 502, 503, 504 -> "Сервер временно недоступен"
                else -> "Ошибка сервера (${throwable.code()})"
            }
        }
        else -> {
            "Неизвестная ошибка. Попробуйте позже"
        }
    }
}

