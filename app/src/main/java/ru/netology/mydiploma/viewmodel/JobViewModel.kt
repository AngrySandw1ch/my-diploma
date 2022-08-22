package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.model.ModelState
import ru.netology.mydiploma.repository.jobRepo.JobRepository
import javax.inject.Inject

val emptyJob = Job(
    id = 0,
    name = "",
    position = "",
    start = 0,
    ownerId = 0
)

@HiltViewModel
class JobViewModel @Inject constructor(
    jobRepository: JobRepository,
    private val auth: AppAuth
) : ViewModel() {
    private val repository: JobRepository = jobRepository

    val data: LiveData<List<Job>> get() = repository.data

    private val edited = MutableLiveData(emptyJob)

    private val _dataState = MutableLiveData(ModelState())
    val dataState: LiveData<ModelState> get() = _dataState

    fun save(userId: Long) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            edited.value?.let { job ->
                userId.let {
                    repository.saveJob(job, it)
                }
            }
            _dataState.postValue(ModelState())
            edited.postValue(emptyJob)
        } catch (e: Exception) {
            ModelState(error = true)
        }
    }

    fun getJobs(userId: Long) {
        try {
            viewModelScope.launch {
                auth.authLiveData.value?.id?.let { myId ->
                    _dataState.postValue(ModelState(refreshing = true))
                    when (myId == userId) {
                        true -> repository.getCurrentUserJobs()
                        false -> userId.let { repository.getUserJobs(it) }
                    }
                    _dataState.postValue(ModelState())
                }
            }
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun refreshJobs(userId: Long) = viewModelScope.launch {
        try {
            viewModelScope.launch {
                auth.authLiveData.value?.id?.let { myId ->
                    _dataState.postValue(ModelState(refreshing = true))
                    when (myId == userId) {
                        true -> repository.getCurrentUserJobs()
                        false -> userId.let { repository.getUserJobs(it) }
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