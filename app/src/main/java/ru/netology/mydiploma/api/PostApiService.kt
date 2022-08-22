package ru.netology.mydiploma.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.mydiploma.dto.Media
import ru.netology.mydiploma.dto.Post

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