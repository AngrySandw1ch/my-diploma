package ru.netology.mydiploma.repository.eventRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mydiploma.api.EventApi
import ru.netology.mydiploma.dto.Event

class EventRepositoryImpl: EventRepository {

    private val _data: MutableLiveData<List<Event>> = MutableLiveData()
    override val data: LiveData<List<Event>> get() = _data

    override suspend fun getEvents() {
        try {
            val response = EventApi.service.getEvents()
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body = response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun likeEvent(id: Long) {
        try {
            val response = EventApi.service.likeEventById(id)
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body = response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(_data.value?.map {
                if (it.id == body.id) body else it
            })
        } catch (e: Exception) {
            e.printStackTrace()
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
        } catch (e: Exception) {
            e.printStackTrace()
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun leaveEvent(id: Long) {
        try {
            val response = EventApi.service.leaveEventById(id)
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body = response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(_data.value?.map {
                if (it.id == body.id) body else it
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeEvent(id: Long) {
        try {
            val response = EventApi.service.removeEventById(id)
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            if (response.body() == null) {
                throw Exception("${response.code()} ${response.message()}")
            }
            _data.postValue(_data.value?.filter {
                it.id != id
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}