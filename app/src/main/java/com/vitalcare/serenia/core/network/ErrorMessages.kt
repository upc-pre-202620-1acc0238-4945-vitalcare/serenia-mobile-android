package com.vitalcare.serenia.core.network

import java.io.IOException

const val CONNECTION_ERROR = "No pudimos conectarnos. Revisa tu internet e intenta de nuevo."
const val UNEXPECTED_ERROR = "Algo salió mal. Intenta de nuevo en un momento."

fun Throwable.toUserMessage(): String {
    return when (this) {
        is IOException -> CONNECTION_ERROR
        is ApiException -> message
        else -> UNEXPECTED_ERROR
    }
}
