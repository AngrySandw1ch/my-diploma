package ru.netology.mydiploma.api.connection

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://app-diploma.herokuapp.com/"

fun okHttpClient(vararg interceptors: Interceptor): OkHttpClient {
    return OkHttpClient.Builder().apply {
        interceptors.forEach {
            this.addInterceptor(it)
        }
    }.build()
}

fun retrofitClient(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(BASE_URL)
        .build()
}