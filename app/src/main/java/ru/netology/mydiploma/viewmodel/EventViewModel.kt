package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.model.ModelState
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

    private val _dataState: MutableLiveData<ModelState> = MutableLiveData()
    val dataState: LiveData<ModelState> get() = _dataState

    init {
        viewModelScope.launch {
            try {
                _dataState.postValue(ModelState(loading = true))
                repository.getEvents()
                _dataState.postValue(ModelState())
            } catch (e: Exception) {
                _dataState.postValue(ModelState(error = true))
            }
        }
    }

    fun refreshEvents() = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(refreshing = true))
            repository.getEvents()
            _dataState.postValue(ModelState())
        } catch(e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun likeEvent(id: Long) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.likeEvent(id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun dislikeEvent(id: Long) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.dislikeEvent(id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun joinEvent(id: Long) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.joinEvent(id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun leaveEvent(id: Long) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.leaveEvent(id)
            _dataState.postValue(ModelState())

        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun removeEvent(id: Long) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.removeEvent(id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

}