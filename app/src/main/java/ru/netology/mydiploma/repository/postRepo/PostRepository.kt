package ru.netology.mydiploma.repository.postRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mydiploma.dto.Post

interface PostRepository {
    val data: MutableLiveData<List<Post>>
    suspend fun getPosts(): List<Post>
    suspend fun save(post: Post)
    suspend fun likePostById(id: Long)
    suspend fun dislikePostById(id: Long)
    suspend fun removePostById(id: Long)
}