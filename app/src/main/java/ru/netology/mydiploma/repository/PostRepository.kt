package ru.netology.mydiploma.repository

import androidx.lifecycle.LiveData
import ru.netology.mydiploma.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getPosts()
    suspend fun save(post: Post)
    suspend fun getPostById(id: Long)
    suspend fun likePostById(id: Long)
    suspend fun dislikePostById(id: Long)
    suspend fun removePostById(id: Long)
}