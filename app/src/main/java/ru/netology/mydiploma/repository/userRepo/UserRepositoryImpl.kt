package ru.netology.mydiploma.repository.userRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.mydiploma.api.UserApiService
import ru.netology.mydiploma.dto.User
import ru.netology.mydiploma.roomdb.dao.UserDao
import ru.netology.mydiploma.roomdb.entity.UserEntity
import ru.netology.mydiploma.roomdb.entity.fromEntity
import ru.netology.mydiploma.roomdb.entity.toEntity
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val userApiService: UserApiService
    ) : UserRepository {

    override val data: LiveData<List<User>> = dao.getUsers().map { userEntityList ->
        userEntityList.fromEntity()
    }

    override suspend fun getUsers() {
        try {
            val response = userApiService.getUsers()
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
}