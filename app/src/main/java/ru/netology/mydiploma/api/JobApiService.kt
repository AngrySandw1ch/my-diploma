package ru.netology.mydiploma.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Job
import java.util.concurrent.TimeUnit

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttp = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor { chain ->
        AppAuth.getInstance().authLiveData.value?.token?.let {
            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", it)
                .build()
            return@addInterceptor chain.proceed(newRequest)
        }
        chain.proceed(chain.request())
    }
    .connectTimeout(10, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttp)
    .build()

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

object JobApi {
    val service: JobApiService by lazy {
        retrofit.create(JobApiService::class.java)
    }

}