package ru.netology.mydiploma.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Media
import ru.netology.mydiploma.dto.Post
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://app-diploma.herokuapp.com/"

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
    .connectTimeout(10, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttp)
    .build()

interface PostApiService {

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

    @Multipart
    @POST("api/media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>


}

object PostApi {
    val service: PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}