package ru.netology.mydiploma.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Event
import java.util.concurrent.TimeUnit

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor { chain ->
        AppAuth.getInstance().authLiveData.value?.token?.let { token ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", token)
                .build()
            return@addInterceptor chain.proceed(newRequest)
        }
        chain.proceed(chain.request())
    }
    .connectTimeout(10, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


interface EventApiService {
    @GET("api/events")
    suspend fun getEvents(): Response<List<Event>>

    @GET("api/events/{eventId}")
    suspend fun getEventById(@Path("eventId") eventId: Long): Response<Event>

    @POST("api/events")
    suspend fun saveEvent(@Body event: Event): Response<Event>

    @POST("api/events/{eventId}/likes")
    suspend fun likeEventById(@Path("eventId") eventId: Long): Response<Event>

    @DELETE("api/events/{eventId}/likes")
    suspend fun dislikeEventById(@Path("eventId") eventId: Long): Response<Event>

    @POST("api/events/{{eventId}}/participants")
    suspend fun joinEventById(@Path("eventId") eventId: Long): Response<Event>

    @POST("api/events/{{eventId}}/participants")
    suspend fun leaveEventById(@Path("eventId") eventId: Long): Response<Event>

    @DELETE("api/events/{eventId}")
    suspend fun removeEventById(@Path("eventId") eventId: Long): Response<Unit>

}

object EventApi {
    val service: EventApiService by lazy {
        retrofit.create(EventApiService::class.java)
    }
}