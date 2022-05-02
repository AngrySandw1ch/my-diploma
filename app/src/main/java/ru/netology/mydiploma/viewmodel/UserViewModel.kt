package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.mydiploma.dto.User
import ru.netology.mydiploma.repository.userRepo.UserRepositoryImpl
import java.lang.Exception

class UserViewModel: ViewModel() {
    private val repository = UserRepositoryImpl()
    val data: LiveData<List<User>> get() = repository.data

    init {
        viewModelScope.launch {
            repository.getUsers()
        }
    }

    fun getUsers() = viewModelScope.launch {
        repository.getUsers()
    }

    fun getUserById(id: Long): User {
        viewModelScope.launch {
            repository.getUserById(id)
        }
        return data.value?.first {
            it.id == id
        } ?: throw Exception("Empty data value")
    }
}