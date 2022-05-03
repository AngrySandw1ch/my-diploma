package ru.netology.mydiploma.repository.eventRepo

import androidx.lifecycle.LiveData
import ru.netology.mydiploma.dto.Event

interface EventRepository {
    val data: LiveData<List<Event>>
    suspend fun getEvents()
    suspend fun likeEvent(id: Long)
    suspend fun dislikeEvent(id: Long)
    suspend fun joinEvent(id: Long)
    suspend fun leaveEvent(id: Long)
    suspend fun removeEvent(id: Long)
}