package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.repository.eventRepo.EventRepository
import ru.netology.mydiploma.repository.eventRepo.EventRepositoryImpl

class EventViewModel : ViewModel() {
    private val repository: EventRepository = EventRepositoryImpl()
    val data: LiveData<List<Event>>
        get() = AppAuth.getInstance().authLiveData.switchMap { (myId, _) ->
            repository.data.map { events ->
                events.map {
                    it.copy(ownedByMe = it.authorId == myId)
                }
            }
        }

    init {
        viewModelScope.launch {
            repository.getEvents()
        }
    }

    fun likeEvent(id: Long) = viewModelScope.launch {
        repository.likeEvent(id)
    }

    fun dislikeEvent(id: Long) = viewModelScope.launch {
        repository.dislikeEvent(id)
    }

    fun joinEvent(id: Long) = viewModelScope.launch {
        repository.joinEvent(id)
    }

    fun leaveEvent(id: Long) = viewModelScope.launch {
        repository.leaveEvent(id)
    }

    fun removeEvent(id: Long) = viewModelScope.launch {
        repository.removeEvent(id)
    }

}