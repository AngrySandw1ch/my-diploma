package ru.netology.mydiploma.repository.userRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mydiploma.api.UserApi
import ru.netology.mydiploma.dto.User

class UserRepositoryImpl : UserRepository {

    private val _data: MutableLiveData<List<User>> = MutableLiveData()
    override val data: LiveData<List<User>> get() = _data

    override suspend fun getUsers() {
        try {
            val response = UserApi.service.getUsers()
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body =
                response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getUserById(id: Long): User {
        try {
            val response = UserApi.service.getUserById(id)
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body =
                response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(_data.value?.map {
                if (it.id != id) {
                    it
                } else body
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data.value?.first {
            it.id == id
        } ?: throw Exception("Empty data")
    }
}