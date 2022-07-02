package ru.netology.mydiploma.repository.postRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.mydiploma.api.PostApi
import ru.netology.mydiploma.dto.Attachment
import ru.netology.mydiploma.dto.Media
import ru.netology.mydiploma.dto.MediaUpload
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.enumeration.AttachmentType
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
            _data.postValue(_data.value?.map {
                if (it.id == body.id) {
                    body
                } else {
                    it
                }
            })
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

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            save(post.copy(attachment = Attachment(media.url, AttachmentType.IMAGE)))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )

            val response = PostApi.service.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}