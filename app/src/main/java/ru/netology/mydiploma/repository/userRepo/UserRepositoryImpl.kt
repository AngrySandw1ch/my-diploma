package ru.netology.mydiploma.repository.userRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.mydiploma.api.UserApi
import ru.netology.mydiploma.dto.User
import ru.netology.mydiploma.roomdb.dao.UserDao
import ru.netology.mydiploma.roomdb.entity.UserEntity
import ru.netology.mydiploma.roomdb.entity.toEntity

class UserRepositoryImpl(private val dao: UserDao) : UserRepository {

    override val data: LiveData<List<User>> = dao.getUsers().map { userEntityList ->
        userEntityList.map { userEntity ->
            userEntity.toDto()
        }

    }

    override suspend fun getUsers() {
        try {
            val response = UserApi.service.getUsers()
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body =
                response.body() ?: throw Exception("${response.code()} ${response.message()}")
            dao.insert(body.toEntity())
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
            dao.insert(UserEntity.fromDto(body))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dao.getUserById(id).toDto()
    }
}