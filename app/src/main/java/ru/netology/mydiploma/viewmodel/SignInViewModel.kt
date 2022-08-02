package ru.netology.mydiploma.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.netology.mydiploma.api.AuthApi
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.auth.AuthState
import java.lang.Exception

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    private val _data = MutableLiveData(AuthState())
    val data: LiveData<AuthState> get() = _data

    fun signIn(login: String, pass: String, name: String) = viewModelScope.launch {
        try {
            val response = AuthApi.service.registration(login, pass, name)
            if (!response.isSuccessful) {
                throw Exception("${response.code()} ${response.message()}")
            }
            val body = response.body() ?: throw Exception("body is null")
            _data.postValue(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}