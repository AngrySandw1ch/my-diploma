package ru.netology.mydiploma.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.dto.MediaUpload
import ru.netology.mydiploma.enumeration.EventType
import ru.netology.mydiploma.model.ModelState
import ru.netology.mydiploma.model.PhotoModel
import ru.netology.mydiploma.repository.eventRepo.EventRepository
import ru.netology.mydiploma.repository.eventRepo.EventRepositoryImpl
import ru.netology.mydiploma.roomdb.AppDb
import java.io.File
import java.time.Clock
import java.time.Instant

private val emptyEvent = Event(
    0,
    0,
    "",
    null,
    "",
    null,
    null,
    null,
    EventType.OFFLINE
)

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EventRepository = EventRepositoryImpl(AppDb.getInstance(application).eventDao())
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

    private val noPhoto = PhotoModel()
    private val _photo: MutableLiveData<PhotoModel> = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel> get() = _photo

    private val edited: MutableLiveData<Event> = MutableLiveData(emptyEvent)


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
        } catch (e: Exception) {
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

    fun save() = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))

            edited.value?.let { event ->
                when(_photo.value) {
                    noPhoto -> {
                        val test = edited.value?.copy(published = Instant.now().toString())
                            repository.save(test!!)
                        }
                    else -> {
                        _photo.value?.file?.let {
                            repository.saveWithAttachment(event.copy(published = Instant.now().toString()), MediaUpload(it))
                        }
                    }
                }
            }
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun edit(event: Event) {
        edited.value = event
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun changeContent(content: String) {
        if (content.trim() == edited.value?.content) {
            return
        }
        edited.value = edited.value?.copy(content = content)
    }

    fun changeDatetime(datetime: Long) {
        if (datetime < 0) {
            return
        }
        edited.value = edited.value?.copy(datetime = Instant.ofEpochMilli(datetime).toString())
    }

    fun changeEventType(type: String) {
        when (type) {
            EventType.OFFLINE.toString() -> edited.value = edited.value?.copy(type = EventType.OFFLINE)
            else -> edited.value = edited.value?.copy(type = EventType.ONLINE)
        }
    }

    fun clearEdited() {
        edited.value = emptyEvent
    }
}