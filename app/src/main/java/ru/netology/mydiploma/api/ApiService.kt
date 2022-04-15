package ru.netology.mydiploma.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Post

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttp = OkHttpClient.Builder()
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
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(ApiService.BASE_URL)
    .client(okHttp)
    .build()

interface ApiService {
    companion object {
        const val BASE_URL = "https://app-diploma.herokuapp.com/"
    }

    @GET("api/posts")
    suspend fun getPosts(): Response<List<Post>>

    @POST("api/posts")
    suspend fun save(@Body post: Post): Response<Post>

    @GET("api/posts/{postId}")
    suspend fun getPostById(@Path("postId") postId: Long): Response<Post>

    @POST("api/posts/{postId}/likes")
    suspend fun likePostById(@Path("postId") postId: Long): Response<Post>

    @DELETE("api/posts/{postId}/likes")
    suspend fun dislikePostById(@Path("postId") postId: Long): Response<Post>

    @DELETE("api/posts/{postId}")
    suspend fun removePostById(@Path("postId") postId: Long): Response<Unit>
}

object PostApi {
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}