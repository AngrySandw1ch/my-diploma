package ru.netology.mydiploma.api

import retrofit2.Response
import retrofit2.http.*
import ru.netology.mydiploma.dto.Job

interface JobApiService {
    @GET("api/{id}/jobs")
    suspend fun getUserJobs(@Path("id") id: Long): Response<List<Job>>

    @GET("api/my/jobs")
    suspend fun getCurrentUserJobs(): Response<List<Job>>

    @POST("api/my/jobs")
    suspend fun saveJob(@Body job: Job): Response<Job>

    @DELETE("api/my/jobs/{jobId}")
    suspend fun removeJob(@Path("jobId") id: Long): Response<Unit>
}