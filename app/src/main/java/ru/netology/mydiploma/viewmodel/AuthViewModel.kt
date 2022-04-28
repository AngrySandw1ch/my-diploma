package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.auth.AuthState


class AuthViewModel: ViewModel() {
    val data: LiveData<AuthState> = AppAuth.getInstance().authLiveData
    val authentificated: Boolean get() =  AppAuth.getInstance().authLiveData.value?.id != 0L
}