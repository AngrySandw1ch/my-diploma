package ru.netology.mydiploma.repository

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.mydiploma.api.PostApi
import ru.netology.mydiploma.dto.Post
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class PostRepositoryImpl() : PostRepository {
    override val data: MutableLiveData<List<Post>> = MutableLiveData()

    override suspend fun getPosts(): List<Post> {
        return try {
            val response = PostApi.service.getPosts()
            if (!response.isSuccessful) {
                throw Exception(response.code().toString() + response.message())
            }
            val body = response.body() ?: throw Exception("Body is null")
            data.postValue(body)
            body
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostApi.service.save(post)
            if (!response.isSuccessful) {
                throw Exception(response.code().toString() + response.message())
            }
            val body =
                response.body() ?: throw Exception("${response.code()} ${response.message()}")
            data.postValue(data.value?.plus(body))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun likePostById(id: Long) {
        try {
            val response = PostApi.service.likePostById(id)
            if (!response.isSuccessful) {
                throw Exception(response.code().toString() + response.message())
            }
            val body =
                response.body() ?: throw Exception("${response.code()} ${response.message()}")
            data.postValue(data.value?.map {
                if (it.id != body.id) it else body
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun dislikePostById(id: Long) {
        try {
            val response = PostApi.service.dislikePostById(id)
            if (!response.isSuccessful) {
                throw Exception(response.code().toString() + response.message())
            }
            val body =
                response.body() ?: throw Exception("${response.code()} ${response.message()}")
            data.postValue(data.value?.map {
                if (it.id != body.id) it else body
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removePostById(id: Long) {
        try {
            val response = PostApi.service.removePostById(id)
            if (!response.isSuccessful) {
                throw Exception(response.code().toString() + response.message())
            }
            if (response.body() == null) {
                throw Exception(response.code().toString() + response.message())
            }
            data.postValue(data.value?.filter {
                it.id != id
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}