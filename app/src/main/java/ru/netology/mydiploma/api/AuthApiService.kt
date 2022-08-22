package ru.netology.mydiploma.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.netology.mydiploma.auth.AuthState

interface AuthApiService {

    @FormUrlEncoded
    @POST("api/users/authentication")
    suspend fun auth(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<AuthState>

    @FormUrlEncoded
    @POST("api/users/registration")
    suspend fun registration(
        @Field("login") login: String,
        @Field("pass") pass: String,
        @Field("name") name: String
    ): Response<AuthState>

}