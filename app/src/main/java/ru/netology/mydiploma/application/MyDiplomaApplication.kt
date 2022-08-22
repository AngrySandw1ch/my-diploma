package ru.netology.mydiploma.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.netology.mydiploma.auth.AppAuth
import javax.inject.Inject

@HiltAndroidApp
class MyDiplomaApplication: Application() {
    @Inject
    lateinit var appAuth: AppAuth
}