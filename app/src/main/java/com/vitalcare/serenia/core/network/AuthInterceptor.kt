package com.vitalcare.serenia.core.network

import com.vitalcare.serenia.core.session.SessionStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionStorage: SessionStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { sessionStorage.session.first()?.token }

        val request = chain.request().newBuilder()
            .header("Accept-Language", LANGUAGE)
            .apply {
                if (token != null) {
                    header("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(request)
    }

    private companion object {
        const val LANGUAGE = "es-PE"
    }
}
