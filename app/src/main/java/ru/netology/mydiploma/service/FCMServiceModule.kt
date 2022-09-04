package ru.netology.mydiploma.service

import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FCMServiceModule {

    @Provides
    @Singleton
    fun provideFCMService(): FirebaseMessaging = FirebaseMessaging.getInstance()
}