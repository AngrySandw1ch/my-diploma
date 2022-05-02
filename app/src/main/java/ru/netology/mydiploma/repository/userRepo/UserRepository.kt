package ru.netology.mydiploma.repository.userRepo

import androidx.lifecycle.LiveData
import ru.netology.mydiploma.dto.User

interface UserRepository {
    val data: LiveData<List<User>>
    suspend fun getUsers()
    suspend fun getUserById(id: Long): User
}