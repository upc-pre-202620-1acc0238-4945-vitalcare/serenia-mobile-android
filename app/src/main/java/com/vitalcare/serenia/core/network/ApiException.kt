package com.vitalcare.serenia.core.network

import com.google.gson.Gson
import retrofit2.Response

class ApiException(
    val status: Int,
    val code: String?,
    override val message: String
) : Exception(message)

// Turns an unsuccessful response into an exception carrying the backend message
fun Response<*>.toApiException(): ApiException {
    val error = errorBody()?.string()?.let { body ->
        runCatching { Gson().fromJson(body, ErrorDto::class.java) }.getOrNull()
    }
    return ApiException(
        status = code(),
        code = error?.code,
        message = error?.message ?: "Error ${code()}"
    )
}
