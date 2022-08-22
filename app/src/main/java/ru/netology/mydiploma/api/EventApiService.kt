package ru.netology.mydiploma.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.dto.Media

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

    @POST("api/events/{eventId}/participants")
    suspend fun joinEventById(@Path("eventId") eventId: Long): Response<Event>

    @DELETE("api/events/{eventId}/participants")
    suspend fun leaveEventById(@Path("eventId") eventId: Long): Response<Event>

    @DELETE("api/events/{eventId}")
    suspend fun removeEventById(@Path("eventId") eventId: Long): Response<Unit>

    @Multipart
    @POST("api/media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>
}