package com.vitalcare.serenia.features.iam.presentation

import com.vitalcare.serenia.core.network.ApiException
import java.io.IOException

const val CONNECTION_ERROR = "No pudimos conectarnos. Revisa tu internet e intenta de nuevo."
const val UNEXPECTED_ERROR = "Algo salió mal. Intenta de nuevo en un momento."

fun Throwable.toAuthMessage(): String {
    return when (this) {
        is IOException -> CONNECTION_ERROR
        is ApiException -> message
        else -> UNEXPECTED_ERROR
    }
}
