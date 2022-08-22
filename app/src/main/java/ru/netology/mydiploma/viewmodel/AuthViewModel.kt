package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.auth.AuthState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val auth: AppAuth): ViewModel() {
    val data: LiveData<AuthState> = auth.authLiveData
    val authentificated: Boolean get() = auth.authLiveData.value?.id != 0L
}