package ru.netology.mydiploma.repository.postRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mydiploma.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getPosts()
    suspend fun save(post: Post)
    suspend fun likePostById(id: Long)
    suspend fun dislikePostById(id: Long)
    suspend fun removePostById(id: Long)
}