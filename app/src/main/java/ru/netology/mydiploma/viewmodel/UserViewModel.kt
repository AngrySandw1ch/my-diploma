package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.mydiploma.dto.User
import ru.netology.mydiploma.model.ModelState
import ru.netology.mydiploma.repository.userRepo.UserRepositoryImpl
import java.lang.Exception

class UserViewModel: ViewModel() {
    private val repository = UserRepositoryImpl()
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

    fun getUserById(id: Long): User {
        viewModelScope.launch {
            try {
                _dataState.postValue(ModelState(loading = true))
                repository.getUserById(id)
                _dataState.postValue(ModelState())
            } catch (e: Exception) {
                _dataState.postValue(ModelState(error = true))
            }
        }
        return data.value?.first {
            it.id == id
        } ?: throw Exception("Empty data value")
    }
}