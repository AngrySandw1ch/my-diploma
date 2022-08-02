package ru.netology.mydiploma.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.model.ModelState
import ru.netology.mydiploma.repository.jobRepo.JobRepositoryImpl
import ru.netology.mydiploma.roomdb.AppDb

val emptyJob = Job(
    id = 0,
    name = "",
    position = "",
    start = 0
)

class JobViewModel(private var userId: Long?, application: Application) : AndroidViewModel(application) {
    private val repository: JobRepositoryImpl = JobRepositoryImpl(AppDb.getInstance(application).jobDao())

    val data: LiveData<List<Job>> get() = repository.data

    private val edited = MutableLiveData(emptyJob)

    private val _dataState = MutableLiveData(ModelState())
    val dataState: LiveData<ModelState> get() = _dataState

    init {
        try {
            viewModelScope.launch {
                AppAuth.getInstance().authLiveData.value?.id?.let { myId ->
                    _dataState.postValue(ModelState(loading = true))
                    when (myId == userId) {
                        true -> repository.getCurrentUserJobs()
                        false -> userId?.let { repository.getUserJobs(it) }
                    }
                    _dataState.postValue(ModelState())
                }
            }
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }


    fun save() = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            edited.value?.let { job ->
                repository.saveJob(job)
            }
            _dataState.postValue(ModelState())
            edited.postValue(emptyJob)
        } catch (e: Exception) {
            ModelState(error = true)
        }
    }

    fun refreshJobs() = viewModelScope.launch {
        try {
            viewModelScope.launch {
                AppAuth.getInstance().authLiveData.value?.id?.let { myId ->
                    _dataState.postValue(ModelState(refreshing = true))
                    when (myId == userId) {
                        true -> repository.getCurrentUserJobs()
                        false -> userId?.let { repository.getUserJobs(it) }
                    }
                    _dataState.postValue(ModelState())
                }
            }
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun removeJob(id: Long) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.removeJob(id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun edit(job: Job = emptyJob) {
        edited.value = job
    }

    fun clearEdited() {
        edited.value = emptyJob
    }

    fun changeStart(start: Long) {
        edited.value = edited.value?.copy(start = start)
    }

    fun changeFinish(finish: Long? = null) {
        edited.value = edited.value?.copy(finish = finish)
    }

    fun changeNameAndPosition(name: String, position: String) {
        edited.value = edited.value?.copy(name = name, position = position)
    }

    fun changeLink(link: String? = null) {
        edited.value = edited.value?.copy(link = link)
    }
}