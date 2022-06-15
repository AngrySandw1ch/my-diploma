package ru.netology.mydiploma.repository.postRepo

import androidx.lifecycle.LiveData
import ru.netology.mydiploma.dto.Media
import ru.netology.mydiploma.dto.MediaUpload
import ru.netology.mydiploma.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getPosts()
    suspend fun save(post: Post)
    suspend fun likePostById(id: Long)
    suspend fun dislikePostById(id: Long)
    suspend fun removePostById(id: Long)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
}