package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.mydiploma.dto.User
import ru.netology.mydiploma.model.ModelState
import ru.netology.mydiploma.repository.userRepo.UserRepository
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    private val repository = userRepository
    val data: LiveData<List<User>> get() = repository.data

    private val _dataState: MutableLiveData<ModelState> = MutableLiveData()
    val dataState: LiveData<ModelState> get() = _dataState

    init {
        viewModelScope.launch {
            try {
                _dataState.postValue(ModelState(loading = true))
                repository.getUsers()
                _dataState.postValue(ModelState())
            } catch (e: Exception) {
                _dataState.postValue(ModelState(error = true))
            }
        }
    }

    fun refreshUsers() = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(refreshing = true))
            repository.getUsers()
            _dataState.postValue(ModelState())
        }
        catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }
}