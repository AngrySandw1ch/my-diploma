package ru.netology.mydiploma.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.mydiploma.auth.AppAuth

private const val authorization = "Authorization"

fun loggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
    }
}

fun authInterceptor(auth: AppAuth) = fun(chain: Interceptor.Chain): Response {
    auth.authLiveData.value?.token?.let { token ->
        val newRequest = chain.request().newBuilder()
            .addHeader(authorization, token)
            .build()
        return chain.proceed(newRequest)
    }
    return chain.proceed(chain.request())
}