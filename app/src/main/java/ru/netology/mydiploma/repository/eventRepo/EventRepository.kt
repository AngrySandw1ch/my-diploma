package ru.netology.mydiploma.repository.eventRepo

import androidx.lifecycle.LiveData
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.dto.Media
import ru.netology.mydiploma.dto.MediaUpload

interface EventRepository {
    val data: LiveData<List<Event>>
    suspend fun getEvents()
    suspend fun likeEvent(id: Long)
    suspend fun dislikeEvent(id: Long)
    suspend fun joinEvent(id: Long)
    suspend fun leaveEvent(id: Long)
    suspend fun removeEvent(id: Long)
    suspend fun save(event: Event)
    suspend fun saveWithAttachment(event: Event, mediaUpload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
}