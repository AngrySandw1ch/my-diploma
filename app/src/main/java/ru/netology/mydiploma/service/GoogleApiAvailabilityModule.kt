package ru.netology.mydiploma.service

import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object GoogleApiAvailabilityModule {
    @Singleton
    @Provides
    fun provideGAA(): GoogleApiAvailability = GoogleApiAvailability.getInstance()
}