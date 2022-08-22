package ru.netology.mydiploma.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.mydiploma.R
import ru.netology.mydiploma.api.AuthApiService
import ru.netology.mydiploma.auth.AuthState
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authApiService: AuthApiService) : ViewModel() {
    private val _data = MutableLiveData(AuthState())
    val data: LiveData<AuthState> get() = _data

    fun signUp(login: String, pass: String, context: Context) = viewModelScope.launch {
        try {
            val response = authApiService.auth(login, pass)
            if (!response.isSuccessful) {
                Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show()
                return@launch
            }
            val body = response.body() ?: throw Exception("${response.code()} ${response.message()}")
            _data.postValue(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
