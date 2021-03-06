package ru.netology.mydiploma.repository.eventRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import ru.netology.mydiploma.api.EventApi
import ru.netology.mydiploma.dto.Attachment
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.dto.Media
import ru.netology.mydiploma.dto.MediaUpload
import ru.netology.mydiploma.enumeration.AttachmentType
import ru.netology.mydiploma.error.ApiError
import ru.netology.mydiploma.error.NetworkError
import ru.netology.mydiploma.error.UnknownError

class EventRepositoryImpl: EventRepository {

    private val _data: MutableLiveData<List<Event>> = MutableLiveData()
    override val data: LiveData<List<Event>> get() = _data

    override suspend fun getEvents() {
        try {
            val response = EventApi.service.getEvents()
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

    override suspend fun likeEvent(id: Long) {
        try {
            val response = EventApi.service.likeEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(_data.value?.map {
                if (it.id == body.id) body else it
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeEvent(id: Long) {
        try {
            val response = EventApi.service.dislikeEventById(id)
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body = response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(_data.value?.map {
                if (it.id == body.id) body else it
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun joinEvent(id: Long) {
        try {
            val response = EventApi.service.joinEventById(id)
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body = response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(_data.value?.map {
                if (it.id == body.id) body else it
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun leaveEvent(id: Long) {
        try {
            val response = EventApi.service.leaveEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(_data.value?.map {
                if (it.id == body.id) body else it
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeEvent(id: Long) {
        try {
            val response = EventApi.service.removeEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            if (response.body() == null) {
                throw ApiError(response.code(), response.message())
            }
            _data.postValue(_data.value?.filter {
                it.id != id
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(event: Event) {
        try {
            val response = EventApi.service.saveEvent(event)
            if(!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
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

    override suspend fun saveWithAttachment(event: Event, mediaUpload: MediaUpload) {
        try {
            val media: Media = upload(mediaUpload)
            save(event.copy(attachment = Attachment(media.url, AttachmentType.IMAGE)))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                upload.file.name,
                upload.file.asRequestBody()
            )
            val response = EventApi.service.upload(media)
            if (!response.isSuccessful) {
                throw  ApiError(response.code(), response.message())
            }
            return response.body() ?: throw  ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}