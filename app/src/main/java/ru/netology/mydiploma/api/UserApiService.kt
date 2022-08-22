package ru.netology.mydiploma.api

import retrofit2.Response
import retrofit2.http.GET
import ru.netology.mydiploma.dto.User

interface UserApiService {

    @GET("api/users")
    suspend fun getUsers(): Response<List<User>>
}