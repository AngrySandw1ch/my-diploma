package ru.netology.mydiploma.application

import android.app.Application
import ru.netology.mydiploma.auth.AppAuth

class MyDiplomaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppAuth.initApp(this)
    }

}