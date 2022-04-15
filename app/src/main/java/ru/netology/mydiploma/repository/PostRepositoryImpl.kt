package ru.netology.mydiploma.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.mydiploma.api.PostApi
import ru.netology.mydiploma.dto.Post

class PostRepositoryImpl() : PostRepository {
    private val _data: MutableLiveData<List<Post>> = MutableLiveData()
    override val data: LiveData<List<Post>> get() = _data
    init {
        CoroutineScope(Dispatchers.IO).launch {
            getPosts()
        }
    }


    override suspend fun getPosts() {
        try {
            val response = PostApi.service.getPosts()
            if (!response.isSuccessful) {
                throw Exception(response.code().toString() + response.message())
            }
            val body = response.body() ?: throw Exception("Body is null")
            _data.postValue(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun save(post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun getPostById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun likePostById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun dislikePostById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removePostById(id: Long) {
        TODO("Not yet implemented")
    }
}