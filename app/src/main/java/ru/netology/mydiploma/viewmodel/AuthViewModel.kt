package ru.netology.mydiploma.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.auth.AuthState


class AuthViewModel(application: Application): AndroidViewModel(application) {
    val data: LiveData<AuthState> = AppAuth.getInstance().authLiveData
    val authentificated: Boolean get() =  AppAuth.getInstance().authLiveData.value?.id != 0L
}