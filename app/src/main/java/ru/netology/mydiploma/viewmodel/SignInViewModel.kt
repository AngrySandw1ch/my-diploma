package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.mydiploma.api.AuthApiService
import ru.netology.mydiploma.auth.AuthState
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authApiService: AuthApiService) : ViewModel() {
    private val _data = MutableLiveData(AuthState())
    val data: LiveData<AuthState> get() = _data

    fun signIn(login: String, pass: String, name: String) = viewModelScope.launch {
        try {
            val response = authApiService.registration(login, pass, name)
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