package ru.netology.mydiploma.repository.postRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mydiploma.api.PostApi
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.error.ApiError
import ru.netology.mydiploma.error.NetworkError
import ru.netology.mydiploma.error.UnknownError
import java.io.IOException

class PostRepositoryImpl() : PostRepository {
    private val _data: MutableLiveData<List<Post>> = MutableLiveData()
    override val data: LiveData<List<Post>> = _data

    override suspend fun getPosts() {
        return try {
            val response = PostApi.service.getPosts()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(body)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostApi.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body =
                response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(data.value?.plus(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likePostById(id: Long) {
        try {
            val response = PostApi.service.likePostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body =
                response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(data.value?.map {
                if (it.id != body.id) it else body
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikePostById(id: Long) {
        try {
            val response = PostApi.service.dislikePostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body =
                response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(data.value?.map {
                if (it.id != body.id) it else body
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removePostById(id: Long) {
        try {
            val response = PostApi.service.removePostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            if (response.body() == null) {
                throw ApiError(response.code(), response.message())
            }
            _data.postValue(data.value?.filter {
                it.id != id
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}